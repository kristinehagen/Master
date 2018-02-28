package main;

import classes.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ColumnGeneration {

    static Population population;
    static GraphViewer graphViewer;
    static Individual bestGlobalSolution;
    static HashMap<Integer, Station> stations;

    private static void init(Input input) {

        //Initialize routes for each vehicle
        for (Vehicle vehicle: input.getVehicles().values()) {
            vehicle.createRoutes(input);
        }

        System.out.println("Initial routes created");

        graphViewer = new GraphViewer();
        graphViewer.displayInitiatedRoutes(input, true);

        int counter = 0;

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

    }

    private static void run(Input input) {


    }




    public static void main(String[] args) throws IOException {
        Input input = new Input();
        init(input);
        run(input);

        System.out.println("algoritm successfully terminated");
    }

}
