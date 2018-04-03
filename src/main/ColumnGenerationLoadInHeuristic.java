package main;

import classes.Input;
import classes.StationVisit;
import classes.Vehicle;
import xpress.WriteXpressFiles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ColumnGenerationLoadInHeuristic {

    //Constructor
    public  ColumnGenerationLoadInHeuristic(Input input) throws IOException {
        WriteXpressFiles.printFixedInput(input);
        initiateRoutes(input);
        WriteXpressFiles.printTimeDependentInput(input, true, true);
        //RunXpress.runXpress(input.getXpressFileColumnGenerationLoadInHeuristic());
    }





    private static void initiateRoutes(Input input) throws FileNotFoundException, UnsupportedEncodingException {

        //Initialize routes for each vehicle
        for (Vehicle vehicle: input.getVehicles().values()) {
            vehicle.createRoutes(input);
        }

        System.out.println("Initial routes created");

        /*
        GraphViewer graphViewer = new GraphViewer();
        graphViewer.displayInitiatedRoutes(input, true);
        */


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
