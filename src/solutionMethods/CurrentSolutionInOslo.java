package solutionMethods;

import classes.Input;
import classes.Station;
import classes.Vehicle;
import classes.VehicleArrival;
import functions.ReadClusterList;

import java.io.IOException;
import java.util.ArrayList;

public class CurrentSolutionInOslo {

    private ArrayList<VehicleArrival> vehicleArrivals = new ArrayList<>();

    //Constructor
    public CurrentSolutionInOslo(Input input) throws IOException {

        generateVehicleArrivals(input);

    }

    private void generateVehicleArrivals(Input input) throws IOException {


        for (Vehicle vehicle : input.getVehicles().values()) {

            //Leser inn clusterliste



        }

    }



    public ArrayList<VehicleArrival> getVehicleArrivals() {
        return vehicleArrivals;
    }

}
