/**
 * An abstraction to represent the user provided traversal constraints and provide useful utility methods to increase readability in the rest of the code
 *
 * Extensibility Decisions:
 * -------------------------
 * --> Use of generics to allow any kind of node label rather than restricting to A,B,C as mentioned in the problem
 * --> Use of dynamic data-structures (HashMap) instead of size constraint data-structures (two-dimensional array) to allow nodes/edges to be added dynamically to the same graph
 * --> New constraints can be added easily by adding new enums and associated factory methods, which also make the construction of PathConstraint more readable in the caller code
 * --> calculateTraversalCost and isPathConstraintMet allow the calculation logic to be defined/maintained in a single place
 *
 * Readability/Interface Decisions:
 * --------------------------------
 * --> Use an Object to contain the constraint as well as utility methods such as calculation of values based on the constraint type to prevent duplication and verboseness in the traversal code
 *
 */
public class PathConstraint {

    private enum ConstraintType{
        MAX_JUNCTIONS,
        EXACT_JUNCTIONS,
        LESS_THAN_DISTANCE
    }

    private static final int UNIT_COST = 1;
    private final ConstraintType constraintType;
    private final int constraint;

    public static PathConstraint buildMaxJunctionConstraint(int constraint){
        return new PathConstraint(ConstraintType.MAX_JUNCTIONS,constraint);
    }

    public static PathConstraint buildExactJunctionConstraint(int constraint){
        return new PathConstraint(ConstraintType.EXACT_JUNCTIONS,constraint);
    }

    public static PathConstraint buildLessThanDistanceConstraint(int constraint){
        return new PathConstraint(ConstraintType.LESS_THAN_DISTANCE,constraint);
    }

    private PathConstraint(ConstraintType constraintType, int constraint) {
        this.constraintType = constraintType;
        this.constraint = constraint;
    }

    public int calculateTraversalCost(Node<?> source, Node<?> target){
        return constraintType == ConstraintType.LESS_THAN_DISTANCE ? source.getEdgeWeight(target): UNIT_COST;
    }

    boolean isPathConstraintMet(Integer cost){
        int comparisonResult = cost.compareTo(constraint);
        switch (constraintType){
            case MAX_JUNCTIONS:
                return comparisonResult <= 0;
            case EXACT_JUNCTIONS:
                return comparisonResult == 0;
            case LESS_THAN_DISTANCE:
                return comparisonResult < 0;
            default:
                throw new IllegalStateException("no behaviour defined for path constraint" + constraintType.name());
        }
    }

    boolean isCostBelowConstraint(int cost){
        return cost < constraint;
    }
}