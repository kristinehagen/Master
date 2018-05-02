package classes;


import enums.SolutionMethod;
import functions.TimeConverter;
import xpress.ReadXpressResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PricingProblem {


    public PricingProblem(){
    }

    public void runPricingProblem(Input input, HashMap<Integer, Double> pricingProblemScores) throws FileNotFoundException {
        pricingProblemScores.clear();

        ArrayList<VehicleArrival> vehicleArrivals;
        if (input.getSolutionMethod().equals(SolutionMethod.HEURISTIC_VERSION_3)) {
            vehicleArrivals = ReadXpressResult.readVehicleArrivalsVersion3(input.getVehicles(), input.getCurrentMinute());
        } else if (input.getSolutionMethod().equals(SolutionMethod.HEURISTIC_VERSION_1) || input.getSolutionMethod().equals(SolutionMethod.HEURISTIC_VERSION_2) || input.getSolutionMethod().equals(SolutionMethod.EXACT_METHOD)) {
            vehicleArrivals = ReadXpressResult.readVehicleArrivals(input.getCurrentMinute());
        } else {
            vehicleArrivals = null;
        }

        for (Station station: input.getStations().values()) {
            for (VehicleArrival vehicleArrival : vehicleArrivals) {
                if (vehicleArrival.getStationId() != station.getId()) {
                    double ViolationsIfNoVisit = 0;
                    double DeviationsIfNoVisit = 0;
                    double initialLoad = station.getLoad();
                    double demandPerMinute = station.getNetDemand(TimeConverter.convertMinutesToHourRounded(input.getCurrentMinute()))/60;
                    double loadAtHorizon = initialLoad + demandPerMinute*input.getTimeHorizon();
                    double optimalState = station.getOptimalState(TimeConverter.convertMinutesToHourRounded(input.getCurrentMinute()));

                    if (loadAtHorizon > station.getCapacity()) {
                        ViolationsIfNoVisit = loadAtHorizon-station.getCapacity();
                        loadAtHorizon = station.getCapacity();
                    }
                    if (loadAtHorizon < 0) {
                        ViolationsIfNoVisit = -loadAtHorizon;
                        loadAtHorizon = 0;
                    }
                    double diffFromOptimalState = Math.abs(optimalState-loadAtHorizon);
                    DeviationsIfNoVisit = diffFromOptimalState;

                    pricingProblemScores.put(station.getId(), ViolationsIfNoVisit + DeviationsIfNoVisit);
                }
            }
        }

        //Read Xpress input and assign pricingProblemScores
        /*
        File inputFile = new File("outputXpressViolationStatistics.txt");
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            Scanner element = new Scanner(line);
            if (element.hasNextInt()) {
                int stationId = element.nextInt();
                int visited = element.nextInt();
                double deviation = Double.parseDouble(element.next());
                double totalViolation = Double.parseDouble(element.next());
                if (visited == 0) {
                    pricingProblemScores.put(stationId, deviation + totalViolation );
                }
            }
        }
        in.close();
        */
    }
}
