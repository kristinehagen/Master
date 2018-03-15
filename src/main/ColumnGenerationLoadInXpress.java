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

public class ColumnGenerationLoadInXpress {

    //Constructor
    public  ColumnGenerationLoadInXpress(Input input) throws IOException, XPRMCompileException {
        WriteXpressFiles.printFixedInput(input);
        initiateRoutes(input);
        WriteXpressFiles.printTimeDependentInput(input, true);
        //RunXpress.runXpress(input.getXpressFileColumnGenerationLoadInXpress());
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

        /*
        //Print initiated routes
        int counter = 1;
        for (Vehicle vehicle: input.getVehicles().values()) {
            for (ArrayList<StationVisit> route : vehicle.getInitializedRoutes()) {
                //Route id
                System.out.print("Route " + counter + ": ");
                counter ++;
                //Station ids
                for (StationVisit stationVisit : route) {
                    System.out.print(stationVisit.getStation().getId() + " ");
                }
                //Total time
                System.out.println(", total time: " + route.get(route.size()-1).getVisitTime());
            }
        }
        */

    }






}
