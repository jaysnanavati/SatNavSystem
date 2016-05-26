/**
 * Exception class for domain-specific exceptions
 *
 * Readability/Maintainability Decisions:
 * --------------------------------
 * --> factory methods used to handle the actual building of the exception in the class rather than the location of invocation to aid readability and allow standardised exceptions accross the codebase
 */
public class GraphException extends Exception {

    private GraphException(String message){
        super(message);
    }

    public static GraphException foundNegativeOrZeroEdgeWeight(){return new GraphException("ONLY POSITIVE (>0) EDGE WEIGHTS ARE SUPPORTED");}
    public static GraphException foundNullNodeLabel(){return new GraphException("PLEASE PROVIDE A NON NULL NODE LABEL");}
    public static GraphException nodeDoesNotExistForLabel(Object label){return new GraphException("NODE: " + label.toString()+ " DO NOT EXIST IN THE GRAPH");}
    public static GraphException foundMalformedArc(Object arc){return new GraphException("The arc " + arc.toString() + " is malformed. Arcs must take the form Node1Node2Weight where nodes must be labeled A-E, (eg: AB4) which represents a road from A to B (but not from B to A), with a length of 4");}
}
