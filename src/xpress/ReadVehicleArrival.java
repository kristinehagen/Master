package xpress;

import classes.StationVisit;
import classes.Vehicle;
import classes.VehicleArrival;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class ReadVehicleArrival {

    public static ArrayList<VehicleArrival> readVehicleArrivals(double currentTime) throws FileNotFoundException {

        ArrayList<VehicleArrival> vehicleArrivals = new ArrayList<>();

        File inputFile = new File("outputXpress.txt");
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            Scanner element = new Scanner(line);
            if (element.hasNextInt()) {

                int stationId = element.nextInt();
                int stationLoad = element.nextInt();
                double time = Double.parseDouble(element.next());
                int vehicle = element.nextInt();
                int nextStationId = element.nextInt();
                double TimeNextStation = Double.parseDouble(element.next());
                boolean firstVisit = ((element.nextInt()) == 1);

                VehicleArrival vehicleArrival = new VehicleArrival(stationId, stationLoad, time + currentTime, vehicle, nextStationId, TimeNextStation+currentTime, firstVisit);

                vehicleArrivals.add(vehicleArrival);
            }
        }
        in.close();

        //Sort list by arrival time
        Collections.sort(vehicleArrivals, new Comparator<VehicleArrival>() {
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

        return vehicleArrivals;
    }

    public static ArrayList<VehicleArrival> readVehicleArrivalsVersion3(HashMap<Integer, Vehicle> vehicles, double currentTime) throws FileNotFoundException {

        ArrayList<VehicleArrival> vehicleArrivals = new ArrayList<>();

        File inputFile = new File("outputXpress.txt");
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            Scanner element = new Scanner(line);
            if (element.hasNextInt()) {

                int vehicleId = element.nextInt();
                int routeNumber = element.nextInt()-1;

                ArrayList<ArrayList<StationVisit>> initializedRoutes = vehicles.get(vehicleId).getInitializedRoutes();
                ArrayList<StationVisit> stationVisitsInRoute = initializedRoutes.get(routeNumber);
                int numberOfStationVisitInRoute = stationVisitsInRoute.size();

                for (int i = 0; i < numberOfStationVisitInRoute; i++ ) {

                    StationVisit stationVisit = stationVisitsInRoute.get(i);

                    int stationId = stationVisit.getStation().getId();
                    int stationLoad = (int) stationVisit.getLoadingQuantity();
                    double time = stationVisit.getVisitTime();

                    int nextStationId = 0;
                    double timeNextStation = 0;

                    //If not last station visit in route
                    if (i < numberOfStationVisitInRoute-1) {
                        nextStationId = stationVisitsInRoute.get(i+1).getStation().getId();
                        timeNextStation = stationVisitsInRoute.get(i+1).getVisitTime();
                    }

                    boolean firstVisit = (i == 0);

                    VehicleArrival vehicleArrival = new VehicleArrival(stationId, stationLoad, time + currentTime, vehicleId, nextStationId, timeNextStation + currentTime, firstVisit);

                    vehicleArrivals.add(vehicleArrival);
                }


            }
        }
        in.close();

        //Sort list by arrival time
        Collections.sort(vehicleArrivals, new Comparator<VehicleArrival>() {
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

        return vehicleArrivals;

    }
}