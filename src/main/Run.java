package main;

import classes.*;
//import com.dashoptimization.XPRMCompileException;

import java.io.IOException;


public class Run {


    public static void main(String[] args) throws IOException {
        Input input = new Input();
        SolutionMethod solutionMethod = input.getSolutionMethod();

        switch (solutionMethod) {
            case ColumnGenerationLoadInXpress:
                ColumnGenerationLoadInXpress columnGenerationLoadInXpress = new ColumnGenerationLoadInXpress(input);
                break;
            case ColumnGenerationLoadInHeuristic:
                ColumnGenerationLoadInHeuristic columnGenerationLoadInHeuristic = new ColumnGenerationLoadInHeuristic(input);
                break;
            case GeneticAlgorithm:
                //Run genetic algorithm
                break;
            case ExactMethod:
                OptimalInXpress optimalInXpress = new OptimalInXpress(input);
                break;
        }

        System.out.println("algorithm successfully terminated");
    }

}
