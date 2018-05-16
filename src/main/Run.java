package main;

import classes.*;
import com.dashoptimization.XPRMCompileException;
import enums.SolutionMethod;

import functions.PrintResults;
import functions.ReadClusterList;
import functions.ReadVehicleInput;
import functions.TimeConverter;
import solutionMethods.*;
import xpress.ReadXpressResult;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Run {


    public static void main(String[] args) throws IOException, XPRMCompileException, InterruptedException {

        Input input = new Input();
        //createClusters(input);

        //WriteXpressFiles.printFixedInput(input);

        //Simulate
        if (input.isSimulation()) {
            runSimulation(input);
        } else {
            runOneVehicleRouteGeneration();
        }

        System.out.println("algorithm successfully terminated");

    }

    private static void createClusters(Input input) throws IOException, XPRMCompileException {

        if (input.getSolutionMethod() == SolutionMethod.CURRENT_SOLUTION_IN_OSLO) {
            ReadClusterList.readClusterListExcel(input, "clusterCurrentSolution.xlsx");

        } else if (input.getSolutionMethod() == SolutionMethod.HEURISTIC_VERSION_1 || input.getSolutionMethod() == SolutionMethod.HEURISTIC_VERSION_2 || input.getSolutionMethod() == SolutionMethod.HEURISTIC_VERSION_3) {

            if (input.isClustering()) {
                String xpressOutputFile = "clusterOutput-Instance" + input.getTestInstance() + "-V" + input.getVehicles().size() + ".txt";
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


    private static void runSimulation(Input input) throws IOException, XPRMCompileException, InterruptedException {


        ArrayList<Double> totalViolationList = new ArrayList<>();
        ArrayList<Double> percentageViolationsList = new ArrayList<>();
        ArrayList<Double> numberOfTimesVehicleRouteGeneratedList = new ArrayList<>();
        ArrayList<Double> averageTimeBetweenVehicleRouteGeneratedList = new ArrayList<>();
        ArrayList<Double> computationalTimeXpress = new ArrayList<>();
        ArrayList<Double> computationalTimeXpressPlusInitialization = new ArrayList<>();
        double maxComputationalTimeIncludingInitialization = -1;
        double minComputationalTimeIncludingInitialization = -1;
        ArrayList<Double> numberOfTimesPPImprovement = new ArrayList<>();


        for (int i = 1; i <= input.getNumberOfRuns(); i++) {

            String simulationFile = "simulation_Instance" + input.getTestInstance() + "_Nr" + i + ".txt";
            System.out.println("Run number: " + i);

            //Run simulation
            input.updateVehiclesAndStationsToInitialState();
            Simulation simulation = new Simulation();
            simulation.run(simulationFile, input);

            double totalViolations = simulation.getCongestions() + simulation.getStarvations();
            totalViolationList.add(totalViolations);
            percentageViolationsList.add((double) totalViolations / (double) simulation.getTotalNumberOfCustomers() * 100);
            numberOfTimesVehicleRouteGeneratedList.add(simulation.getNumberOfTimesVehicleRouteGenerated());
            averageTimeBetweenVehicleRouteGeneratedList.add(average(simulation.getTimeToNextSimulationList()));
            computationalTimeXpress.add(average(simulation.getComputationalTimesXpress()));
            computationalTimeXpressPlusInitialization.add(average(simulation.getComputationalTimesXpressPlusInitialization()));
            numberOfTimesPPImprovement.add(average(simulation.getNumberOfTimesPPImproved()));

            double maxTime = simulation.getMaxComputationalTimeIncludingInitialization();
            double minTime = simulation.getMinComputationalTimeIncludingInitialization();

            if (maxComputationalTimeIncludingInitialization == -1 || maxTime > maxComputationalTimeIncludingInitialization) {
                maxComputationalTimeIncludingInitialization = maxTime;
            }
            if (minComputationalTimeIncludingInitialization == -1 || minTime < minComputationalTimeIncludingInitialization) {
                minComputationalTimeIncludingInitialization = minTime;
            }
        }

        System.out.println("Min time: " + minComputationalTimeIncludingInitialization);
        System.out.println("Max time: " + maxComputationalTimeIncludingInitialization);

        double averageViolation = average(totalViolationList);
        double averagePercentageViolations = average(percentageViolationsList);
        double sdViolation = sd(totalViolationList, averageViolation);
        double sdPercentageViolations = sd(percentageViolationsList, averagePercentageViolations);
        double averageNumberOfTimesVehicleRouteGenerated = average(numberOfTimesVehicleRouteGeneratedList);
        double averageTimeToVehicleRouteGenerated = average(averageTimeBetweenVehicleRouteGeneratedList);
        double averageComputationalTimeXpress = average(computationalTimeXpress);
        double averageComputationalTimeXpressPlusInitialization = average(computationalTimeXpressPlusInitialization);
        double averageTimePPImprovement = average(numberOfTimesPPImprovement);

        PrintResults.printSimulationResultsToExcelFile(averageViolation, averagePercentageViolations, percentageViolationsList, sdViolation, sdPercentageViolations,
            averageNumberOfTimesVehicleRouteGenerated, averageTimeToVehicleRouteGenerated, averageComputationalTimeXpress,
            averageComputationalTimeXpressPlusInitialization, input, averageTimePPImprovement);

    }




    private static void runOneVehicleRouteGeneration() throws IOException, XPRMCompileException, IllegalArgumentException {

        int testInstance;
        int time;

        for (int instance = 2; instance <= 4; instance ++) {
            testInstance = instance;
            for (int t = 7; t <= 17; t+=10) {
                time = t;

                Input input = new Input(testInstance, time);

                for (double weightDT = 0; weightDT <= 1; weightDT += 0.10) {
                    input.setWeightClusterDrivingTime(weightDT);
                    for (double weightND = 0; weightND <= 1 - weightDT; weightND += 0.10) {
                        input.setWeightClusterNetDemand(weightND);
                        input.setWeightClusterEqualSize(1 - weightDT - weightND);

                        System.out.println("Instance: " + instance);
                        System.out.println("Time: " + time);
                        System.out.println("Weights: " + weightDT + ", " + weightND + ", " + (1-weightDT-weightND));

                        createClusters(input);
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




