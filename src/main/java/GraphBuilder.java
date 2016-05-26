import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * Class used to build graphs, allows the responsibility of building a graph and representing the graph/algorithms to be separated
 *
 * Extensibility Decisions:
 * -------------------------
 * --> This class can be extended to handle creating of different kinds of graphs eg: graphs with nodes that have numbers as labels easily
 *
 * Readability/Interface Decisions:
 * --------------------------------
 * --> Regular expressions allow better handling of the graph construction requirements as defined in the problem (eg: A-E nodes, arc definition: Node1Node2Weight)
 */
public class GraphBuilder {

    private static final Pattern SIMPLE_ARC_PATTERN = Pattern.compile("[A-E][A-E]\\d+");

    public Graph<Character> buildDirectedGraph(String input) throws GraphException {
        Graph<Character> graph = new Graph<>();
        List<String> edges = Arrays.stream(input.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        for (String e : edges) {
            Matcher m = SIMPLE_ARC_PATTERN.matcher(e);
            if (m.find()) {
                String edge = m.group(0);
                char sourceLabel = edge.charAt(0);
                char targetLabel = edge.charAt(1);
                int weight = Integer.parseInt(edge.substring(2));
                graph.addNodeIfAbsent(sourceLabel)
                        .addNodeIfAbsent(targetLabel)
                        .addDirectedEdge(sourceLabel, targetLabel, weight);
            } else {
                throw GraphException.foundMalformedArc(e);
            }
        }
        return graph;
    }
}
