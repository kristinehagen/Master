package main;

import classes.*;
import com.dashoptimization.XPRMCompileException;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class HeuristicVersion3 {

    //Constructor
    public HeuristicVersion3(Input input) throws IOException, XPRMCompileException {
        HashMap<Integer, Double> pricingProblemScores = new HashMap<>();

        WriteXpressFiles.printFixedInput(input);
        initiateRoutes(input, pricingProblemScores);
        WriteXpressFiles.printTimeDependentInput(input, SolutionMethod.HEURISTIC_VERSION_3);
        RunXpress.runXpress(input.getXpressFile());

        //Run pricing problem
        int nrOfIterations = 0;
        if (input.isRunPricingProblem()) {
            while (nrOfIterations < input.getNrOfRunsPricingProblem()) {
                runPricingProblem(input, pricingProblemScores);
                input.setNrStationBranching(input.getNrOfBranchingPricingProblem());
                initiateRoutes(input, pricingProblemScores);
                WriteXpressFiles.printTimeDependentInput(input, SolutionMethod.HEURISTIC_VERSION_3);
                RunXpress.runXpress(input.getXpressFile());
            }
        }
    }

    private void runPricingProblem(Input input,HashMap<Integer, Double> pricingProblemScores) throws FileNotFoundException {
        PricingProblem pricingProblem = new PricingProblem();
        pricingProblem.runPricingProblem(input, pricingProblemScores);
        System.out.println("Pricing problem executed");
    }

    private static void initiateRoutes(Input input, HashMap<Integer, Double> pricingProblemScores) throws FileNotFoundException, UnsupportedEncodingException {

        //Initialize routes for each vehicle
        for (Vehicle vehicle: input.getVehicles().values()) {
            vehicle.createRoutes(input, pricingProblemScores);
        }

        System.out.println("Initial routes created");


        //Print initiated routes
        int counter = 1;
        for (Vehicle vehicle: input.getVehicles().values()) {
            for (ArrayList<StationVisit> route : vehicle.getInitializedRoutes()) {
                //Route id
                System.out.print("Route " + counter + ": ");
                counter ++;
                //Station ids
                for (StationVisit stationVisit : route) {
                    System.out.print("(" + stationVisit.getStation().getId() + ", " + stationVisit.getLoadingQuantity() + ", " + stationVisit.getVisitTime() + ") ");
                }
                //Total time
                System.out.println(", total time: " + route.get(route.size()-1).getVisitTime());
            }
        }

    }

}
