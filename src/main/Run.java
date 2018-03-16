package main;

import classes.*;
import com.dashoptimization.XPRMCompileException;

import java.io.IOException;


public class Run {


    public static void main(String[] args) throws IOException, XPRMCompileException {
        Input input = new Input();
        SolutionMethod solutionMethod = input.getSolutionMethod();

        switch (solutionMethod) {
            case ColumnGenerationLoadInXpress:
                ColumnGenerationLoadInXpress columnGenerationLoadInXpress = new ColumnGenerationLoadInXpress(input);
                break;
            case ColumnGenerationLoadInHeuristic:
                //Run column generation where load is determined with heuristic
                break;
            case GeneticAlgorithm:
                //Run genetic algorithm
                break;
            case Xpress:
                OptimalInXpress optimalInXpress = new OptimalInXpress(input);
                break;
        }

        System.out.println("algorithm successfully terminated");
    }

}
