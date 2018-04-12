package xpress;

import classes.VehicleArrival;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


public class ReadVehicleArrival {

    public static ArrayList<VehicleArrival> readVehicleArrivals(double currentTime) throws FileNotFoundException {

        ArrayList<VehicleArrival> stationVisitsToSimulate = new ArrayList<>();

        File inputFile = new File("outputXpress.txt");
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            Scanner element = new Scanner(line);
            if (element.hasNextInt()) {

                int stationId = element.nextInt();
                int stationVisitNr = element.nextInt();
                int stationLoad = element.nextInt();
                double time = Double.parseDouble(element.next());
                int vehicle = element.nextInt();
                int nextStationId = element.nextInt();
                int nextStationVisit = element.nextInt();
                double TimeNextStation = Double.parseDouble(element.next());
                boolean firstVisit = ((element.nextInt()) == 1);

                VehicleArrival vehicleArrival = new VehicleArrival(stationId, stationVisitNr, stationLoad, time + currentTime, vehicle, nextStationId, nextStationVisit, TimeNextStation+currentTime, firstVisit);

                stationVisitsToSimulate.add(vehicleArrival);
            }
        }
        in.close();

        //Sort list by arrival time
        Collections.sort(stationVisitsToSimulate, new Comparator<VehicleArrival>() {
            @Override
            public int compare(VehicleArrival vehicleArrival1, VehicleArrival vehicleArrival2) {
                double diff = vehicleArrival1.getTime() - vehicleArrival2.getTime();

                if( diff < 0 ){
                    return -1;
                } else if ( diff > 0 ) {
                    return 1;
                }
                return 0;
            }
        });

        return stationVisitsToSimulate;
    }
}