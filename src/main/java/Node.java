import java.util.HashMap;
import java.util.Map;

/**
 * Generic Node implementation to model a junction in the network
 *
 * Extensibility Decisions:
 * -------------------------
 * --> Use of generics to allow any kind of node label rather than restricting to A,B,C as mentioned in the problem
 *
 * @param <T>
 */
class Node<T> {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final T label;
    //Map used to maintain the adjacent nodes for a node and their associated weights
    private final Map<Node<T>, Integer> edges;
    //used to maintain temporary distance from source values for Dijkstra's algorithm
    private Integer distanceFromSource = INFINITY;

    public Node(T label) {
        this.label = label;
        this.edges = new HashMap<>();
    }

    public T getLabel() {
        return label;
    }

    public void addDirectedEdge(Node<T> target, int weight) {
        edges.put(target, weight);
    }

    public boolean isConnectedTo(Node<T> target) {
        return edges.containsKey(target);
    }

    public int getEdgeWeight(Node<?> target) {
        return edges.getOrDefault(target, 0);
    }

    public Map<Node<T>, Integer> getEdges() {
        return edges;
    }

    public Integer getDistanceFromSource() {
        return distanceFromSource;
    }

    public void setDistanceFromSource(Integer distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
    }

    public void resetDistanceFromSource(){
        this.distanceFromSource = INFINITY;
    }

    public boolean isDistanceFromSourceFinite(){
        return distanceFromSource < INFINITY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node<?> node = (Node<?>) o;

        return !(label != null ? !label.equals(node.label) : node.label != null);

    }

    @Override
    public int hashCode() {
        return label != null ? label.hashCode() : 0;
    }
}