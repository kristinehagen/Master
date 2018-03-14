package main;

import classes.*;

import java.io.IOException;


public class Run {


    public static void main(String[] args) throws IOException {
        Input input = new Input();
        SolutionMethod solutionMethod = input.getSolutionMethod();

        switch (solutionMethod) {
            case ColumnGenerationLoadInXpress:
                //Print fixedInputFile
                ColumnGenerationLoadInXpress columnGenerationLoadInXpress = new ColumnGenerationLoadInXpress(input);
                break;
            case ColumnGenerationLoadInHeuristic:
                //Run column generation where load is determined with heuristic
                break;
            case GeneticAlgorithm:
                //Run genetic algorithm
                break;
            case Xpress:
                //Run Xpress
                break;
        }

        System.out.println("algoritm successfully terminated");
    }

}
