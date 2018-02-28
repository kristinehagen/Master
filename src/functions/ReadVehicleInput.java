package functions;

import classes.Station;
import classes.Vehicle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ReadVehicleInput {

    public static HashMap<Integer, Vehicle> readVehicleInput(String vehicleInitialFile) throws FileNotFoundException {

        HashMap<Integer, Vehicle> vehicles = new HashMap<>();

        //Read vehicleInitial.txt file
        File inputFile = new File(vehicleInitialFile);
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()){
            String line = in.nextLine();
            Scanner element = new Scanner(line);
            if (element.hasNextInt()) {
                int id = element.nextInt();
                Vehicle vehicle = new Vehicle(id);
                int nextStationInitial = element.nextInt();
                vehicle.setNextStation(nextStationInitial);
                vehicle.setNextStationInitial(nextStationInitial);
                double timeToNextStationInitial = (Double.parseDouble(element.next()));
                vehicle.setTimeToNextStation(timeToNextStationInitial);
                vehicle.setTimeToNextStationInitial(timeToNextStationInitial);
                int load = element.nextInt();
                vehicle.setLoad(load);
                vehicle.setInitialLoad(load);
                int capacity = element.nextInt();
                vehicle.setCapacity(capacity);

                vehicles.put(id, vehicle);
            }
            element.close();
        }
        in.close();
        return vehicles;
    }
}