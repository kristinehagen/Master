package main;

import classes.Input;
import classes.Station;
import classes.Vehicle;
import com.dashoptimization.XPRMCompileException;

import java.io.IOException;

public class test {

    public static void main(String[] args) throws IOException {

        /*double capacity = 0;
        double nrOfBikes = 0;



        for (Station station : input.getStations().values()) {
            capacity += station.getCapacity();
            nrOfBikes += station.getInitialLoad();
        }

        for (Vehicle vehicle : input.getVehicles().values()) {
            nrOfBikes += vehicle.getInitialLoad();
        }

        System.out.println("Total nr of capacities: " + capacity);
        System.out.println("Total nr of bikes: " + nrOfBikes);

        */

        Input input = new Input();

        for (Station station : input.getStations().values()) {
            System.out.println("Stasjon: " + station.getId() + " optimal state: " + station.getOptimalState(20  ));
        }


    }

}
