package classes;

import com.dashoptimization.XPRMCompileException;
import enums.SolutionMethod;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HeuristicVersion1 {

    //Constructor
    public HeuristicVersion1(Input input) throws IOException, XPRMCompileException {
        WriteXpressFiles.printFixedInput(input);

        initiateRoutes(input);
        WriteXpressFiles.printTimeDependentInput(input, SolutionMethod.HEURISTIC_VERSION_1);
        //RunXpress.runXpress(input.getXpressFile());
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
