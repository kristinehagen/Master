package xpress;

import classes.Input;
import classes.Station;
import classes.Vehicle;
import functions.TimeConverter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;


public class WriteXpressFiles {

    public static void printTimeDependentInput (Input input, boolean includeInteriorRepresentation, boolean determineLoadInHeuristic)
            throws FileNotFoundException, UnsupportedEncodingException {
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


        //Interior representation routes

        if (includeInteriorRepresentation) {
            writer.println();
            writer.println("intRep : [");
            for (Vehicle vehicle : input.getVehicles().values()) {
                for (int route = 0; route < vehicle.getInitializedRoutes().size(); route++) {

                    ArrayList<ArrayList<Integer>> allStationPairs = new ArrayList<>();

                    for (int stationVisitNr = 0; stationVisitNr < vehicle.getInitializedRoutes().get(route).size(); stationVisitNr++) {

                        ArrayList<Integer> stationPair = new ArrayList<>();

                        //From station
                        stationPair.add(vehicle.getInitializedRoutes().get(route).get(stationVisitNr).getStation().getId());

                        //To station (or artificial station)
                        if (stationVisitNr == vehicle.getInitializedRoutes().get(route).size()-1) {
                            stationPair.add(0);
                        } else {
                            stationPair.add(vehicle.getInitializedRoutes().get(route).get(stationVisitNr+1).getStation().getId());
                        }

                        allStationPairs.add(stationPair);
                    }

                    ArrayList<ArrayList<Integer>> alreadyPrinted = new ArrayList<>();

                    for (ArrayList<Integer> stationPair : allStationPairs) {

                        //Check if station pair is not already printed
                        if (!alreadyPrinted.contains(stationPair)) {
                            //Count how many identical station pairs there are
                            int count = 0;
                            for (ArrayList<Integer> stationPairToCompareAgainst : allStationPairs) {
                                if (stationPair.equals(stationPairToCompareAgainst)) {
                                    count++;
                                }
                            }

                            //print
                            writer.println("( " + stationPair.get(0) + " " + stationPair.get(1) + " " + vehicle.getId() + " " + route+1 + " ) " + count);
                            alreadyPrinted.add(stationPair);
                        }

                        else {
                            //Do nothing
                        }

                    }

                    writer.println();
                }
            }
            writer.println("]");
        }



        //Interior representation load

        if (determineLoadInHeuristic) {

            writer.println();
            writer.println("intRepLoad : [");


            for (Vehicle vehicle : input.getVehicles().values()) {
                for (int route = 0; route < vehicle.getInitializedRoutes().size(); route++) {

                    HashMap<Integer, Double> pickUpStations = new HashMap<>();
                    HashMap<Integer, Double> deliveryStations = new HashMap<>();

                    for (int stationVisitNr = 0; stationVisitNr < vehicle.getInitializedRoutes().get(route).size(); stationVisitNr++) {

                        int stationId = vehicle.getInitializedRoutes().get(route).get(stationVisitNr).getStation().getId();
                        double load = vehicle.getInitializedRoutes().get(route).get(stationVisitNr).getLoadingQuantity();

                        //Pick up station
                        if (load < 0) {
                            //Check if station is already in hashmap
                            if (pickUpStations.containsKey(stationId)) {
                                double currentLoad = pickUpStations.get(stationId);
                                pickUpStations.put(stationId, currentLoad - load);
                            } else {
                                pickUpStations.put(stationId, -load);
                            }
                        }

                        //Delivery station
                        else {
                            //Check if station is already in hashmap
                            if (deliveryStations.containsKey(stationId)) {
                                double currentLoad = pickUpStations.get(stationId);
                                deliveryStations.put(stationId, currentLoad + load);
                            } else {
                                deliveryStations.put(stationId, load);
                            }
                        }
                    }

                    //Print
                    for (int stationId : pickUpStations.keySet()) {
                        double load = pickUpStations.get(stationId);
                        writer.println("( " + stationId + " " + vehicle.getId()  + " " + route+1 + " ) " + load);
                    }

                    for (int stationId : deliveryStations.keySet()) {
                        double load = deliveryStations.get(stationId);
                        writer.println("( " + stationId + " " + vehicle.getId()  + " " + route+1 + " ) " + load);
                    }
                }
            }
        }
        writer.close();
    }




    public static void printFixedInput (Input input)
            throws FileNotFoundException, UnsupportedEncodingException {
        String filename = input.getFixedInputFile();
        PrintWriter writer = new PrintWriter(filename, "UTF-8");

        writer.println("artificialStation: 0");
        writer.println("visitInterval: 8");

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