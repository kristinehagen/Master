/*package functions;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class basicORtool {

    static { System.loadLibrary("jniortools"); }

    private static MPSolver createSolver (String solverType) {
        return new MPSolver("CalculateFitness", MPSolver.OptimizationProblemType.valueOf(solverType));
    }

    public static Double runSolver(String solverType) {
        MPSolver solver = createSolver(solverType);
        double infinity = MPSolver.infinity();

        //Set variables
        MPVariable x1 = solver.makeNumVar(0.0, infinity, "x1");
        MPVariable x2 = solver.makeNumVar(0.0, infinity, "x2");

        //Maximize objective = 2 * x1 + x2
        MPObjective objective = solver.objective();
        objective.setCoefficient(x1, 2);
        objective.setCoefficient(x2, 1);
        objective.setMaximization();

        //Constraints: x1 + x2 <= 8
        MPConstraint constraint1 = solver.makeConstraint(8, infinity);
        constraint1.setCoefficient(x1, 1);
        constraint1.setCoefficient(x1, 1);

        solver.solve();
        return solver.objective().value();
    }

}
*/