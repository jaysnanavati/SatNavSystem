import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

public class GraphTest {

    private static Graph<Character> graph;
    private static Graph<Character> disconnectedGraph;

    @BeforeClass
    public static void prepare() throws GraphException {
        GraphBuilder graphBuilder = new GraphBuilder();
        graph = graphBuilder.buildDirectedGraph("AB5, BC4, CD7, DC8, DE6, AD5, CE2, EB3, AE7");
        disconnectedGraph = graphBuilder.buildDirectedGraph("AB1,BC4,AC1,DE5");
    }

    @Test(expected = GraphException.class)
    public void addDirectedEdgeForNonExistentNodeTest() throws GraphException {
        graph.addDirectedEdge('Z', 'K', 5);
    }

    @Test(expected = GraphException.class)
    public void addDirectedEdgeWithNegativeWeightTest() throws GraphException {
        graph.addDirectedEdge('A', 'D', -1);
    }

    @Test(expected = GraphException.class)
    public void addDirectedEdgeWithZeroWeightTest() throws GraphException {
        graph.addDirectedEdge('A', 'D', 0);
    }

    @Test
    public void calculateRouteDistanceSuccessTest() throws GraphException {
        Map<List<Character>, Integer> testData = new HashMap<>();
        testData.put(Arrays.asList('A', 'B', 'C'), 9);
        testData.put(Arrays.asList('A', 'D'), 5);
        testData.put(Arrays.asList('A', 'D', 'C'), 13);
        testData.put(Arrays.asList('A', 'E', 'B','C','D'), 21);

        for (List<Character> input : testData.keySet()) {
            int result = graph.calculateRouteDistance(input);
            Assert.assertSame(testData.get(input), result);
        }
    }

    @Test
    public void calculateRouteDistanceNoRouteTest() throws GraphException {
        Assert.assertSame(0, graph.calculateRouteDistance(Arrays.asList('A', 'E', 'D')));
    }

    @Test
    public void calculateRouteDistanceEdgeCasesTest() throws GraphException {
        Assert.assertSame(0, graph.calculateRouteDistance(Collections.singletonList('A')));
        Assert.assertSame(0, graph.calculateRouteDistance(Collections.emptyList()));
    }

    @Test
    public void calculateNumberOfPathsBetweenNodesMaxJunctionTest() throws GraphException {
        Assert.assertSame(2, graph.calculateNumberOfPathsBetweenNodes('C', 'C', PathConstraint.buildMaxJunctionConstraint(3)));
    }

    @Test
    public void calculateNumberOfPathsBetweenNodesExactJunctionTest() throws GraphException {
        Assert.assertSame(3, graph.calculateNumberOfPathsBetweenNodes('A', 'C', PathConstraint.buildExactJunctionConstraint(4)));
    }

    @Test
    public void calculateNumberOfPathsBetweenNodesLessThanDistanceTest() throws GraphException {
        Assert.assertSame(9, graph.calculateNumberOfPathsBetweenNodes('C', 'C', PathConstraint.buildLessThanDistanceConstraint(30)));
    }

    @Test
    public void calculateNumberOfPathsBetweenNodesEdgeCasesTest() throws GraphException {
        Assert.assertSame(0, graph.calculateNumberOfPathsBetweenNodes('A', 'C', PathConstraint.buildLessThanDistanceConstraint(0)));
    }

    @Test
    public void calculateNumberOfPathsBetweenNodesNoPathFoundTest() throws GraphException {
        Assert.assertSame(0, disconnectedGraph.calculateNumberOfPathsBetweenNodes('A', 'D', PathConstraint.buildLessThanDistanceConstraint(10)));
    }

    @Test
    public void calculateLengthOfShortestRouteTest() throws GraphException {
        Assert.assertSame(9, graph.calculateLengthOfShortestRoute('A', 'C'));
    }

    @Test
    public void calculateLengthOfShortestRouteSameSourceAndTargetTest() throws GraphException {
        Assert.assertSame(9, graph.calculateLengthOfShortestRoute('B', 'B'));
    }

    @Test
    public void calculateLengthOfShortestRouteNoRouteTest() throws GraphException {
        Assert.assertSame(0,disconnectedGraph.calculateLengthOfShortestRoute('A', 'D'));
    }
}
