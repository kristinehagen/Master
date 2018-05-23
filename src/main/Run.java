package main;

import classes.*;
import com.dashoptimization.XPRMCompileException;
import enums.SolutionMethod;

import functions.PrintResults;
import functions.ReadClusterList;
import functions.TimeConverter;
import solutionMethods.*;
import xpress.ReadXpressResult;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Run {


    public static void main(String[] args) throws IOException, XPRMCompileException, InterruptedException {
        //generateCluster(input);
        Input input = new Input();
        //WriteXpressFiles.printFixedInput(input);

        //Simulate
        if (input.isSimulation()) {
            runSimulation();
        } else {
            runOneVehicleRouteGeneration();
        }

        System.out.println("algorithm successfully terminated");

    }

    private static void generateCluster(Input input) throws IOException, XPRMCompileException {

        //Clear already created cluster
        for (Vehicle vehicle : input.getVehicles().values()) {
            vehicle.getClusterStationList().clear();
        }

        if (input.getSolutionMethod() == SolutionMethod.CURRENT_SOLUTION_IN_OSLO) {
            if (input.getTestInstance() == 4) {
                ReadClusterList.readClusterListExcel(input, "clusterCurrentSolution.xlsx");
            } else {
                //Returnerer alle stasjonene
                for (Vehicle vehicle : input.getVehicles().values()) {
                    HashMap<Integer, Station> stations = input.getStations();
                    ArrayList<Station> stationsList = new ArrayList<>();
                    stationsList.addAll(stations.values());
                    vehicle.setClusterStationList(stationsList);
                }
            }

        } else if (input.getSolutionMethod() == SolutionMethod.HEURISTIC_VERSION_1 || input.getSolutionMethod() == SolutionMethod.HEURISTIC_VERSION_2 || input.getSolutionMethod() == SolutionMethod.HEURISTIC_VERSION_3) {

            if (input.isClustering() && input.getTestInstance()> 1) {
                String xpressOutputFile = "clusterOutput.txt";
                WriteXpressFiles.writeClusterInformation(input);
                RunXpress.runXpress("createCluster");
                ReadClusterList.readClusterListTextFile(input, xpressOutputFile);

            } else {
                //Returnerer alle stasjonene
                for (Vehicle vehicle : input.getVehicles().values()) {
                    HashMap<Integer, Station> stations = input.getStations();
                    ArrayList<Station> stationsList = new ArrayList<>();
                    stationsList.addAll(stations.values());
                    vehicle.setClusterStationList(stationsList);
                }
            }
        }
    }


    private static void runSimulation() throws IOException, XPRMCompileException, InterruptedException {

        int testInstance = 4;
        int time = 7;
        int nrOfVehicles = 5;
        SolutionMethod solutionMethod = SolutionMethod.HEURISTIC_VERSION_3;

        for (int run = 1; run <= 12; run++ ) {

            Input input = new Input(testInstance, time, nrOfVehicles, solutionMethod);

            int geofenceFactor = 0;

            if (run == 1) {
                adjustNrOfBikesInSystem(input.getStations(), input.getVehicles(), 1500);
            } else if (run == 2) {
                adjustNrOfBikesInSystem(input.getStations(), input.getVehicles(), 2000);
            } else if (run == 3) {
                adjustNrOfBikesInSystem(input.getStations(), input.getVehicles(), 2500);
            } else if (run == 4) {
                allowGeoFencing(input.getStations(), 2);
                geofenceFactor = 2;
            } else if (run == 5) {
                allowGeoFencing(input.getStations(), 3);
                geofenceFactor = 3;
            } else if (run == 6) {
                allowGeoFencing(input.getStations(), 10);
                geofenceFactor = 10;
            } else if (run == 7) {
                allowGeoFencing(input.getStations(), 2);
                geofenceFactor = 2;
                adjustNrOfBikesInSystem(input.getStations(), input.getVehicles(), 2000);
            } else if (run == 8) {
                allowGeoFencing(input.getStations(), 2);
                geofenceFactor = 2;
                adjustNrOfBikesInSystem(input.getStations(), input.getVehicles(), 2500);
            } else if (run == 9) {
                input.setWeightStarvation(0.5);
                input.setWeightCongestion(0.7);
            } else if (run == 10) {
                input.setWeightStarvation(0.4);
                input.setWeightCongestion(0.8);
            } else if (run == 11) {
                input.setWeightStarvation(0.3);
                input.setWeightCongestion(0.9);
            } else {
                input.setWeightStarvation(0.2);
                input.setWeightCongestion(1);
            }

            System.out.println("Test nr " + run);


            generateCluster(input);
            WriteXpressFiles.printFixedInput(input);

            ArrayList<Double> totalViolationList = new ArrayList<>();
            ArrayList<Double> happyCustomersList = new ArrayList<>();
            ArrayList<Double> totalCustomersList = new ArrayList<>();
            ArrayList<Double> totalCongestionsList = new ArrayList<>();
            ArrayList<Double> totalStarvationsList = new ArrayList<>();
            ArrayList<Double> percentageViolationsList = new ArrayList<>();
            ArrayList<Double> numberOfTimesVehicleRouteGeneratedList = new ArrayList<>();
            ArrayList<Double> averageTimeBetweenVehicleRouteGeneratedList = new ArrayList<>();
            ArrayList<Double> computationalTimeXpress = new ArrayList<>();
            ArrayList<Double> computationalTimeXpressPlusInitialization = new ArrayList<>();
            ArrayList<Double> numberOfTimesPPImprovement = new ArrayList<>();

            double numberOfHappyCustomersWhenNoVehicles = findNrOfHappyCustomersWithNoVehicles(testInstance, time);

            for (int i = 1; i <= input.getNumberOfRuns(); i++) {

                String simulationFile = "simulation_Instance" + input.getTestInstance() + "_T" + (int)(input.getSimulationStartTime()/60) + "_Nr" + i + ".txt";
                System.out.println("Run number: " + i);

                //Run simulation
                input.updateVehiclesAndStationsToInitialState();
                Simulation simulation = new Simulation();
                simulation.run(simulationFile, input);

                double totalViolations = simulation.getCongestions() + simulation.getStarvations();
                happyCustomersList.add(simulation.getHappyCustomers());
                totalViolationList.add(totalViolations);
                totalCustomersList.add(simulation.getTotalNumberOfCustomers());
                totalCongestionsList.add(simulation.getCongestions());
                totalStarvationsList.add(simulation.getStarvations());
                percentageViolationsList.add(totalViolations / (double) simulation.getTotalNumberOfCustomers() * 100);
                numberOfTimesVehicleRouteGeneratedList.add(simulation.getNumberOfTimesVehicleRouteGenerated());
                averageTimeBetweenVehicleRouteGeneratedList.add(average(simulation.getTimeToNextSimulationList()));
                computationalTimeXpress.add(average(simulation.getComputationalTimesXpress()));
                computationalTimeXpressPlusInitialization.add(average(simulation.getComputationalTimesXpressPlusInitialization()));
                numberOfTimesPPImprovement.add(average(simulation.getNumberOfTimesPPImproved()));
            }

            double averageViolation = average(totalViolationList);
            double averagePercentageViolations = average(percentageViolationsList);
            double sdViolation = sd(totalViolationList, averageViolation);
            double sdPercentageViolations = sd(percentageViolationsList, averagePercentageViolations);
            double averageNumberOfTimesVehicleRouteGenerated = average(numberOfTimesVehicleRouteGeneratedList);
            double averageTimeToVehicleRouteGenerated = average(averageTimeBetweenVehicleRouteGeneratedList);
            double averageComputationalTimeXpress = average(computationalTimeXpress);
            double averageComputationalTimeXpressPlusInitialization = average(computationalTimeXpressPlusInitialization);
            double averageTimePPImprovement = average(numberOfTimesPPImprovement);
            double averageHappyCustomers = average(happyCustomersList);
            System.out.println("Total customers: " + average(totalCustomersList));
            System.out.println("Total happy custeroms: " + average(happyCustomersList));
            System.out.println("Total violations: " + averageViolation);
            System.out.println("Total violations percentage: " + averagePercentageViolations);

            PrintResults.printSimulationResultsToExcelFile(averageViolation, averagePercentageViolations, percentageViolationsList, sdViolation, sdPercentageViolations,
                    averageNumberOfTimesVehicleRouteGenerated, averageTimeToVehicleRouteGenerated, averageComputationalTimeXpress,
                    averageComputationalTimeXpressPlusInitialization, input, averageTimePPImprovement, averageHappyCustomers, numberOfHappyCustomersWhenNoVehicles,
                    average(totalStarvationsList), average(totalCongestionsList), average(totalCustomersList), geofenceFactor);
        }





    }

    private static void allowGeoFencing(HashMap<Integer, Station> stations, int times) {

        for (Station station : stations.values()) {
            station.setCapacity(station.getCapacity() * times);
        }

    }

    private static void adjustNrOfBikesInSystem(HashMap<Integer, Station> stations, HashMap<Integer, Vehicle> vehicles, int nrOfBicyclesUpdated) {
        double currentNumberOfBicycles = 0;

        for (Station station : stations.values()) {
            currentNumberOfBicycles += station.getInitialLoad();
        }
        for (Vehicle vehicle : vehicles.values()) {
            currentNumberOfBicycles += vehicle.getInitialLoad();
        }

        if (currentNumberOfBicycles > nrOfBicyclesUpdated) {

            //Reduce nr of bicycles on every station
            while (currentNumberOfBicycles - nrOfBicyclesUpdated > 158) {
                //Remove one bicycle at each station
                for (Station station : stations.values()) {
                    if (station.getInitialLoad()>0) {
                        station.setInitialLoad(station.getInitialLoad()-1);
                        currentNumberOfBicycles--;
                    }
                }
            }
            while (currentNumberOfBicycles > nrOfBicyclesUpdated) {
                //Pick a random station, and reduce load by 1
                for (Station station : stations.values()) {
                    if (station.getInitialLoad() > 0) {
                        station.setInitialLoad(station.getInitialLoad()-1);
                        currentNumberOfBicycles--;
                        if (currentNumberOfBicycles == nrOfBicyclesUpdated) {
                            break;
                        }
                    }
                }
            }


        }


        else if (currentNumberOfBicycles < nrOfBicyclesUpdated) {
            //Increase nr of bicycles
            while (nrOfBicyclesUpdated - currentNumberOfBicycles > 158) {
                //Add one bicycle to each station
                for (Station station : stations.values()) {
                    if (station.getCapacity() > station.getInitialLoad()) {
                        station.setInitialLoad(station.getInitialLoad()+1);
                        currentNumberOfBicycles++;
                    }
                }
            }
            while (currentNumberOfBicycles < nrOfBicyclesUpdated) {
                //Pick a random station, and increase load by 1
                for (Station station : stations.values()) {
                    if (station.getCapacity() > station.getInitialLoad()) {
                        station.setInitialLoad(station.getInitialLoad()+1);
                        currentNumberOfBicycles++;
                        if (currentNumberOfBicycles == nrOfBicyclesUpdated) {
                            break;
                        }
                    }
                }
            }
        }

    }

    private static void checkIfValidInstance(int testInstance) throws IllegalArgumentException {
        if (!(testInstance == 1 || testInstance == 4)) {
            throw new IllegalArgumentException("Ugyldig test instance for simulering");
        }
    }

    private static double findNrOfHappyCustomersWithNoVehicles(int testInstance, int time) {

        if (testInstance == 1) {
            if (time == 7) {
                return 678.0;
            } else if (time == 17) {
                return 283.9;
            }
        } else if (testInstance == 4){
            if (time == 7) {
                return 6559.0;
            } else if (time == 17) {
                return 4927.5;
            }
        }
        return 0;
    }


    private static void runOneVehicleRouteGeneration() throws IOException, XPRMCompileException, IllegalArgumentException {

        //for (int nrOfVehicles = 2; nrOfVehicles <= 3; nrOfVehicles ++) {
        for (int sol = 1; sol <= 2; sol++) {
            SolutionMethod solutionMethod;
            if (sol == 1) {
                solutionMethod = SolutionMethod.EXACT_METHOD;
            } else if (sol == 2) {
                solutionMethod = SolutionMethod.HEURISTIC_VERSION_3;
            } else {
                solutionMethod = SolutionMethod.HEURISTIC_VERSION_3;
            }

            //for (int instance = 1; instance <= 4; instance ++) {
            for (int time = 7; time <= 7; time += 10) {

                //System.out.println("Solution method: " + sol);
                //System.out.println("Instance: " + 1);
                //System.out.println("Time: " + time);
                //System.out.println("Nr of vehicles: " + 5);


                Input input = new Input(2, 7, 3, SolutionMethod.HEURISTIC_VERSION_1);

                generateCluster(input);
                WriteXpressFiles.printFixedInput(input);

                double computationalTimeXpress;
                double computationalTimeIncludingInitialization;
                double objectiveValue;

                switch (input.getSolutionMethod()) {
                    case HEURISTIC_VERSION_1:
                        HeuristicVersion1 heuristicVersion1 = new HeuristicVersion1(input);
                        computationalTimeXpress = heuristicVersion1.getComputationalTimeXpress();
                        computationalTimeIncludingInitialization = heuristicVersion1.getComputationalTimeIncludingInitialization();
                        objectiveValue = ReadXpressResult.readObjectiveValue();
                        PrintResults.printOneRouteResultsToExcelFile(input, objectiveValue, computationalTimeXpress, computationalTimeIncludingInitialization);
                        break;

                    case HEURISTIC_VERSION_2:
                        HeuristicVersion2 heuristicVersion2 = new HeuristicVersion2(input);
                        computationalTimeXpress = heuristicVersion2.getComputationalTimeXpress();
                        computationalTimeIncludingInitialization = heuristicVersion2.getComputationalTimeIncludingInitialization();
                        objectiveValue = ReadXpressResult.readObjectiveValue();
                        PrintResults.printOneRouteResultsToExcelFile(input, objectiveValue, computationalTimeXpress, computationalTimeIncludingInitialization);
                        break;

                    case HEURISTIC_VERSION_3:
                        HeuristicVersion3 heuristicVersion3 = new HeuristicVersion3(input);
                        computationalTimeXpress = heuristicVersion3.getComputationalTimeXpress();
                        computationalTimeIncludingInitialization = heuristicVersion3.getComputationalTimeIncludingInitialization();
                        objectiveValue = ReadXpressResult.readObjectiveValue();
                        PrintResults.printOneRouteResultsToExcelFile(input, objectiveValue, computationalTimeXpress, computationalTimeIncludingInitialization);
                        break;

                    case EXACT_METHOD:
                        ExactMethod exactMethod = new ExactMethod(input);
                        computationalTimeXpress = exactMethod.getComputationalTime();
                        objectiveValue = ReadXpressResult.readObjectiveValue();
                        PrintResults.printOneRouteResultsToExcelFile(input, objectiveValue, computationalTimeXpress, computationalTimeXpress);
                        break;

                    case CURRENT_SOLUTION_IN_OSLO:
                        throw new IllegalArgumentException("Kan ikke kjøre CURRENT_SOLUTION_IN_OSLO i Xpress");

                    case NO_VEHICLES:
                        calculateObjectiveFunction(input);
                        break;


                }
            }
        }
    }


    private static void calculateObjectiveFunction(Input input) throws IOException {
        double totalViolationsIfNoVisit = 0;
        double totalDeviationsIfNoVisit = 0;

        for (Station station: input.getStations().values()) {

            double initialLoad = station.getLoad();
            double demandPerMinute = station.getNetDemand(TimeConverter.convertMinutesToHourRounded(input.getCurrentMinute()))/60;
            double loadAtHorizon = initialLoad + demandPerMinute*input.getTimeHorizon();
            double optimalState = station.getOptimalState(TimeConverter.convertMinutesToHourRounded(input.getCurrentMinute()));

            if (loadAtHorizon > station.getCapacity()) {
                totalViolationsIfNoVisit += loadAtHorizon-station.getCapacity();
                loadAtHorizon = station.getCapacity();
            }
            if (loadAtHorizon < 0) {
                totalViolationsIfNoVisit += -loadAtHorizon;
                loadAtHorizon = 0;
            }
            double diffFromOptimalState = Math.abs(optimalState-loadAtHorizon);
            totalDeviationsIfNoVisit += diffFromOptimalState;


        }

        double objectiveValue = input.getWeightViolation()*totalViolationsIfNoVisit + input.getWeightDeviation()*totalDeviationsIfNoVisit;

        PrintResults.printOneRouteResultsToExcelFile(input, objectiveValue, 0, 0 );
    }

    private static double average(ArrayList<Double> list) {
        double sum = 0;
        int numberOfElements = list.size();
        for (Double element:list) {
            sum += element;
        }
        return sum/numberOfElements;
    }


    private static double sd(ArrayList<Double> list, double mean) {
        double temp = 0;
        double size = list.size();
        for(double a :list)
            temp += (a-mean)*(a-mean);
        double var = temp/(size-1);
        return Math.sqrt(var);
    }

}




