package solutionMethods;

import classes.Input;
import classes.PricingProblem;
import classes.StopWatch;
import classes.Vehicle;
import enums.SolutionMethod;
import com.dashoptimization.XPRMCompileException;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class HeuristicVersion3 {

    private double computationalTimeXpress;
    private double computationalTimeIncludingInitialization;

    //Constructor
    public HeuristicVersion3(Input input) throws IOException, XPRMCompileException {

        //Start timer
        StopWatch stopWatchIncludingInitialization = new StopWatch();
        stopWatchIncludingInitialization.start();

        HashMap<Integer, Double> pricingProblemScores = new HashMap<>();
        initiateRoutes(input, pricingProblemScores);
        WriteXpressFiles.printTimeDependentInput(input, SolutionMethod.HEURISTIC_VERSION_3);

        //Start timer and run Xpress
        StopWatch stopWatchXpress = new StopWatch();
        stopWatchXpress.start();

        RunXpress.runXpress(input.getXpressFile());

        stopWatchIncludingInitialization.stop();
        stopWatchXpress.stop();

        //Run pricing problem
        if (input.isRunPricingProblem()) {
            int initialBranchingConstant = input.getNrStationBranching();
            input.setNowRunningPricingProblem(true);

            for (int i = 0; i < input.getNrOfRunsPricingProblem(); i ++) {

                runPricingProblem(input, pricingProblemScores);
                input.setNrStationBranching(input.getNrOfBranchingPricingProblem());
                initiateRoutes(input, pricingProblemScores);
                WriteXpressFiles.printTimeDependentInput(input, SolutionMethod.HEURISTIC_VERSION_3);
                RunXpress.runXpress(input.getXpressFile());
            }

            input.setNrStationBranching(initialBranchingConstant);
            input.setNowRunningPricingProblem(false);
        }

        this.computationalTimeXpress = stopWatchXpress.getElapsedTimeSecs();
        this.computationalTimeIncludingInitialization = stopWatchIncludingInitialization.getElapsedTimeSecs();
        this.computationalTimeIncludingInitialization = stopWatchIncludingInitialization.getElapsedTimeSecs();
    }


    private void runPricingProblem(Input input, HashMap<Integer, Double> pricingProblemScores) throws FileNotFoundException {
        PricingProblem pricingProblem = new PricingProblem();
        pricingProblem.runPricingProblem(input, pricingProblemScores);
        System.out.println("Pricing problem executed");
    }

    private static void initiateRoutes(Input input, HashMap<Integer, Double> pricingProblemScores) throws FileNotFoundException, UnsupportedEncodingException {

        //Initialize routes for each vehicle
        for (Vehicle vehicle: input.getVehicles().values()) {
            vehicle.createRoutes(input, pricingProblemScores);
        }


    }

    public double getComputationalTimeXpress() {
        return computationalTimeXpress;
    }

    public void setComputationalTimeXpress(double computationalTimeXpress) {
        this.computationalTimeXpress = computationalTimeXpress;
    }

    public double getComputationalTimeIncludingInitialization() {
        return computationalTimeIncludingInitialization;
    }

    public void setComputationalTimeIncludingInitialization(double computationalTimeIncludingInitialization) {
        this.computationalTimeIncludingInitialization = computationalTimeIncludingInitialization;
    }
}
