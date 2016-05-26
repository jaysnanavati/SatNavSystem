import java.util.*;

/**
 * Generic Graph implementation to model the network of junctions in the town
 *
 * Extensibility Decisions:
 * -------------------------
 * --> Use of generics to allow any kind of node label rather than restricting to A,B,C as mentioned in the problem
 * --> Use of dynamic data-structures (HashMap) instead of size constraint data-structures (two-dimensional array) to allow nodes/edges to be added dynamically to the same graph
 *
 * Readability/Interface Decisions:
 * --------------------------------
 * --> adding of edge/node encapsulated in the Graph so users only need to provide labels and weights
 * --> use of fluent interface for addDirectedEdge and addNodeIfAbsent to make graph construction more readable
 *
 * @param <T>
 */
public class Graph<T> {

    private final Map<T, Node<T>> nodes = new HashMap<>();

    public Graph<T> addDirectedEdge(T sourceLabel, T targetLabel, int weight) throws GraphException {
        if (weight > 0) {
            getNodeForLabel(sourceLabel).addDirectedEdge(getNodeForLabel(targetLabel), weight);
            return this;
        } else {
            throw GraphException.foundNegativeOrZeroEdgeWeight();
        }
    }

    public Graph<T> addNodeIfAbsent(T nodeLabel) throws GraphException {
        nodes.computeIfAbsent(Optional.ofNullable(nodeLabel).orElseThrow(GraphException::foundNullNodeLabel), Node::new);
        return this;
    }

    public int calculateRouteDistance(List<T> route) throws GraphException {
        int totalDistance = 0;
        for (int i = 0; i + 1 < route.size(); i++) {
            Node<T> currentNode = getNodeForLabel(route.get(i));
            Node<T> nextNode = getNodeForLabel(route.get(i + 1));
            if (currentNode.isConnectedTo(nextNode)) {
                totalDistance += currentNode.getEdgeWeight(nextNode);
            } else {
                return 0;
            }
        }
        return totalDistance;
    }

    /**
     *
     * Uses Dijkstra's algorithm with the following tweak to identify the distance of the shortest route between two nodes:
     * --> An adjacent node's distance is updated if its existing distance value is 0 and the source,target node are the same to calculate the length of the shortest cycle when source = target
     *
     * @param source
     * @param target
     * @return
     * @throws GraphException
     */
    public int calculateLengthOfShortestRoute(T source, T target) throws GraphException {
        Node<T> sourceNode = getNodeForLabel(source);
        Node<T> targetNode = getNodeForLabel(target);
        //reset node distance from source (to clear results from previous executions on the same graph)
        nodes.values().stream().forEach(Node::resetDistanceFromSource);
        //PriorityQueue data structure used to use a min-heap approach to find the node with the lowest distance from source in O(1) time
        PriorityQueue<Node<T>> minHeap = new PriorityQueue<>((x, y) -> x.getDistanceFromSource().compareTo(y.getDistanceFromSource()));
        sourceNode.setDistanceFromSource(0);
        minHeap.add(sourceNode);

        while (!minHeap.isEmpty()) {
            Node<T> nodeWithMinDistanceFromSource = minHeap.poll();
            nodeWithMinDistanceFromSource.getEdges().keySet().stream().forEach(adjacentNode -> {
                int newDistance = nodeWithMinDistanceFromSource.getDistanceFromSource() + nodeWithMinDistanceFromSource.getEdgeWeight(adjacentNode);
                if (newDistance < adjacentNode.getDistanceFromSource() || (adjacentNode.getDistanceFromSource() == 0 && sourceNode.equals(targetNode))) {
                    adjacentNode.setDistanceFromSource(newDistance);
                    minHeap.add(adjacentNode);
                }
            });
        }
        return targetNode.isDistanceFromSourceFinite() ? targetNode.getDistanceFromSource() : 0;
    }

    /**
     *  Uses a Depth First Approach to calculate the number of paths between nodes with the following tweak:
     *  --> Nodes are not marked "visited" to enable re-entry which is required as part of the problem
     *
     * @param source
     * @param target
     * @param pathConstraint
     * @return
     * @throws GraphException
     */
    public int calculateNumberOfPathsBetweenNodes(T source, T target, PathConstraint pathConstraint) throws GraphException {
        return performDfsPathSearch(getNodeForLabel(source), getNodeForLabel(target), pathConstraint, 0);
    }

    /**
     *  Each stack frame of the recursion represents the number of routes found by traversing all adjacent nodes for each adjacent node of the start node, the results are summed up in the recursion step
     *  and returned in the base case which is when the provided constraint is exceeded
     * @param source
     * @param target
     * @param pathConstraint
     * @param currentTraversalCost
     * @return
     * @throws GraphException
     */
    private int performDfsPathSearch(Node<T> source, Node<T> target, PathConstraint pathConstraint, Integer currentTraversalCost) throws GraphException {
        int result = 0;
        if (source.equals(target) && currentTraversalCost > 0 && pathConstraint.isPathConstraintMet(currentTraversalCost)) {
            result++;
        }
        Iterator<Node<T>> i = source.getEdges().keySet().iterator();
        while (i.hasNext() && pathConstraint.isCostBelowConstraint(currentTraversalCost)) {
            Node<T> nextNode = i.next();
            int traversalCost = pathConstraint.calculateTraversalCost(source, nextNode);
            result += performDfsPathSearch(nextNode, target, pathConstraint, currentTraversalCost + traversalCost);
        }
        return result;
    }

    private Node<T> getNodeForLabel(T label) throws GraphException {
        return Optional.ofNullable(nodes.get(label)).orElseThrow(()->GraphException.nodeDoesNotExistForLabel(label));
    }

    public boolean edgeWithWeightExists (T sourceLabel, T targetLabel,int weight) throws GraphException {
        return getNodeForLabel(sourceLabel).getEdgeWeight(getNodeForLabel(targetLabel)) == weight;
    }
}
