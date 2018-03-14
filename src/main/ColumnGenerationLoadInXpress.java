package main;

import classes.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class ColumnGenerationLoadInXpress {

    private Population population;
    private Individual bestGlobalSolution;
    private HashMap<Integer, Station> stations;

    //Constructor
    public  ColumnGenerationLoadInXpress(Input input) throws FileNotFoundException, UnsupportedEncodingException {
        init(input);
        run(input);
    }



    private static void init(Input input) throws FileNotFoundException, UnsupportedEncodingException {

        //Initialize routes for each vehicle
        for (Vehicle vehicle: input.getVehicles().values()) {
            vehicle.createRoutes(input);
        }

        System.out.println("Initial routes created");

        GraphViewer graphViewer = new GraphViewer();
        graphViewer.displayInitiatedRoutes(input, true);


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


        //Print route to file (to be read by Xpress)
        ArrayList<ArrayList<Integer>> intRepList = new ArrayList<>();

        String filename = "intRep.txt";
        PrintWriter writer = new PrintWriter(filename, "UTF-8");

        for (Vehicle vehicle : input.getVehicles().values()) {
            for (int route = 0; route < vehicle.getInitializedRoutes().size(); route++) {
                for (int stationVisit = 0; stationVisit < vehicle.getInitializedRoutes().get(route).size(); stationVisit++) {
                    ArrayList<Integer> oneLine = new ArrayList<>();
                    //From station
                    oneLine.add(vehicle.getInitializedRoutes().get(route).get(stationVisit).getStation().getId());

                    //To station (or artificial station)
                    if (stationVisit == vehicle.getInitializedRoutes().get(route).size()-1) {
                        oneLine.add(0);
                    } else {
                        oneLine.add(vehicle.getInitializedRoutes().get(route).get(stationVisit+1).getStation().getId());
                    }

                    //Vehicle
                    oneLine.add(vehicle.getId());

                    //Route
                    oneLine.add(route+1);

                    //Add to total list
                    writer.println("( " + oneLine.get(0) + " " + oneLine.get(1) + " " + oneLine.get(2) + " " + oneLine.get(3) + " ) 1");
                }
            }
        }
        writer.close();
    }

    private static void run(Input input) {


    }




}
