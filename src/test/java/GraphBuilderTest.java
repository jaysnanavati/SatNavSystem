import org.junit.Assert;
import org.junit.Test;

public class GraphBuilderTest {

    private static GraphBuilder graphBuilder = new GraphBuilder();

    @Test
    public void wellFormedArcTest() throws GraphException {
        Graph<Character> graph = graphBuilder.buildDirectedGraph("AB2");
        Assert.assertTrue(graph.edgeWithWeightExists('A','B',2));
    }

    @Test(expected = GraphException.class)
    public void malformedArcDueToMissingNodeTest() throws GraphException {
        graphBuilder.buildDirectedGraph("A2");
    }

    @Test(expected = GraphException.class)
    public void malformedArcDueToMissingWeightTest() throws GraphException {
        graphBuilder.buildDirectedGraph("A");
    }

    @Test(expected = GraphException.class)
    public void malformedArcDueToInvalidNodeLabelTest() throws GraphException{
        graphBuilder.buildDirectedGraph("Zk5");
    }

}
