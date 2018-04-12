package main;

import classes.*;
import com.dashoptimization.XPRMCompileException;

import java.io.IOException;


public class Run {


    public static void main(String[] args) throws IOException, XPRMCompileException {
        Input input = new Input();
        SolutionMethod solutionMethod = input.getSolutionMethod();

        switch (solutionMethod) {
            case HEURISTIC_VERSION_1:
                HeuristicVersion1 heuristicVersion1 = new HeuristicVersion1(input);
                break;
            case HEURISTIC_VERSION_2:
                HeuristicVersion2 heuristicVersion2 = new HeuristicVersion2(input);
                break;
            case HEURISTIC_VERSION_3:
                HeuristicVersion3 heuristicVersion3 = new HeuristicVersion3(input);
                break;
            case EXACT_METHOD:
                ExactMethod optimalInXpress = new ExactMethod(input);
                break;
        }

        System.out.println("algorithm successfully terminated");
    }

}
