package functions;

import classes.Input;
import classes.Station;
import classes.Vehicle;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class WriteXpressFiles {

    public static void printTimeDependentInput (Input input) throws FileNotFoundException, UnsupportedEncodingException {
        String filename = input.getTimedependentInoutFile();
        PrintWriter writer = new PrintWriter(filename, "UTF-8");

        //weightViolation
        writer.print("weightViolation : ");
        writer.println(input.getWeightViolation());

        //weightDeviation
        writer.print("weightDeviation : ");
        writer.println(input.getWeightDeviation());

        //weightReward
        writer.print("weightReward : ");
        writer.println(input.getWeightReward());

        //weightDeviation
        writer.print("weightDeviationReward : ");
        writer.println(input.getWeightDeviationReward());

        //weightReward
        writer.print("weightDrivingTimePenalty : ");
        writer.println(input.getWeightDrivingTimePenalty());
        writer.println();

        //lengthOfPlanningHorizon
        writer.print("lengthOfPlanningHorizon : ");
        writer.println(input.getTimeHorizon());

        //MaxVisit
        writer.print("maxVisits : ");
        writer.println(input.getMaxVisit());
        writer.println();

        //maxRoute
        writer.print("maxRoute : ");
        int maxRoute = 0;
        for(Vehicle vehicle : input.getVehicles().values()) {
            if (vehicle.getInitializedRoutes().size() > maxRoute) {
                maxRoute = vehicle.getInitializedRoutes().size();
            }
        }
        writer.println(maxRoute);
        writer.println();

        //vehicleInitialStation
        writer.println("vehicleInitialStation : [");
        for (Vehicle vehicle : input.getVehicles().values()) {
            writer.println(vehicle.getNextStation());
        }
        writer.println("]");

        //vehicleRemainingTimeToInitialStation
        writer.println();
        writer.println("vehicleRemainingTimeToInitialStation : [");
        for (Vehicle vehicle : input.getVehicles().values()) {
            writer.println(vehicle.getTimeToNextStation());
        }
        writer.println("]");

        //vehicleInitialLoad
        writer.println();
        writer.println("vehicleInitialLoad : [");
        for (Vehicle vehicle : input.getVehicles().values()) {
            writer.println(vehicle.getLoad());
        }
        writer.println("]");

        //stationsInitialLoad
        writer.println();
        writer.println("stationsInitialLoad : [");
        for (Station station : input.getStations().values()) {
            writer.println(station.getLoad());
        }
        writer.println("0");
        writer.println("]");

        //optimalState
        writer.println();
        writer.println("optimalState : [");
        for (Station station : input.getStations().values()) {
            writer.println(station.getOptimalState(TimeConverter.convertSecondsToHourRounded(input.getCurrentMinute()*60)));
        }
        writer.println("0");
        writer.println("]");

        //stationDemand - net demand per minute
        writer.println();
        writer.println("stationDemand : [");
        for (Station station : input.getStations().values()) {
            double bikeWanted = station.getBikeWantedMedian(TimeConverter.convertSecondsToHourRounded(input.getCurrentMinute()*60));
            double bikeReturned = station.getBikeReturnedMedian(TimeConverter.convertSecondsToHourRounded(input.getCurrentMinute()*60));
            writer.println((bikeReturned-bikeWanted)/60);
        }
        writer.println("0");
        writer.println("]");

        //StarvationStations - station with negative net demand
        writer.println();
        writer.println("StarvationStations : [");
        for (Station station : input.getStations().values()) {
            double bikeWanted = station.getBikeWantedMedian(TimeConverter.convertSecondsToHourRounded(input.getCurrentMinute()*60));
            double bikeReturned = station.getBikeReturnedMedian(TimeConverter.convertSecondsToHourRounded(input.getCurrentMinute()*60));
            if (bikeReturned-bikeWanted <= 0 ) {
                writer.println(station.getId());
            }
        }
        writer.println("]");

        //CongestionStations - station with positive net demand
        writer.println();
        writer.println("CongestionStations : [");
        for (Station station : input.getStations().values()) {
            double bikeWanted = station.getBikeWantedMedian(TimeConverter.convertSecondsToHourRounded(input.getCurrentMinute()*60));
            double bikeReturned = station.getBikeReturnedMedian(TimeConverter.convertSecondsToHourRounded(input.getCurrentMinute()*60));
            if (bikeReturned-bikeWanted >= 0 ) {
                writer.println(station.getId());
            }
        }
        writer.println("]");
        writer.println("intRep : [");
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

        //Interior representation routes
        writer.println();

        writer.close();
    }




    public static void printFixedInput (Input input)
            throws FileNotFoundException, UnsupportedEncodingException {
        String filename = input.getFixedInputFile();
        PrintWriter writer = new PrintWriter(filename, "UTF-8");

        writer.println("artificialStation: 0");

        //Station IDs
        writer.println();
        writer.println("Stations : [");
        for (Station station : input.getStations().values()) {
            writer.println(station.getId());
        }
        writer.println("0");
        writer.println("]");

        //Station capacities
        writer.println();
        writer.println("stationsCapacities : [");
        for (Station station : input.getStations().values()) {
            writer.println(station.getCapacity());
        }
        writer.println("0");
        writer.println("]");

        //VEHICLES
        writer.println("vehicleParkingTime: " + input.getVehicleParkingTime());
        writer.println("unitHandlingTime: " + input.getVehicleHandlingTime());

        //Vehicle IDs
        writer.println();
        writer.println("Vehicles : [");
        for (Vehicle vehicle : input.getVehicles().values()) {
            writer.println(vehicle.getId());
        }
        writer.println("]");

        //vehicleCapacity
        writer.println();
        writer.println("vehicleCapacity : [");
        for (Vehicle vehicle : input.getVehicles().values()) {
            writer.println(vehicle.getCapacity());
        }
        writer.println("]");

        //DrivingTime
        writer.println();
        writer.println("drivingTime : [");
        for (Station stationOrigin : input.getStations().values()) {
            for (Station stationDestination : input.getStations().values()) {
                writer.print(stationOrigin.getDrivingTimeToStation(stationDestination.getId()) + " ");
            }
            writer.println("0");
        }
        for (Station station : input.getStations().values()) {
            writer.print("0 ");
        }

        writer.println("0");
        writer.println("]");

        writer.close();
    }
}