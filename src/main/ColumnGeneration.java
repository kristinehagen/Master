package main;

import classes.*;

import java.io.FileNotFoundException;
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

    }

    private static void run(Input input) {


    }




    public static void main(String[] args) throws FileNotFoundException {
        Input input = new Input();
        init(input);
        run(input);

        System.out.println("algoritm successfully terminated");
    }

}
