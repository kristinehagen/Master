package main;

import classes.*;
import com.dashoptimization.XPRMCompileException;
import enums.SolutionMethod;
import functions.PrintResults;
import functions.TimeConverter;
import solutionMethods.*;
import xpress.ReadXpressResult;
import xpress.WriteXpressFiles;

import java.io.IOException;
import java.util.ArrayList;


public class Run {


    public static void main(String[] args) throws IOException, XPRMCompileException, InterruptedException {

        Input input = new Input();
        input.setCurrentMinute(input.getSimulationStartTime());

        WriteXpressFiles.printFixedInput(input);

        //Simulate
        if (input.isSimulation()) {
            runSimulation(input);
        } else {
            runOneVehicleRouteGeneration(input);
        }

        System.out.println("algorithm successfully terminated");

    }

    private static void runSimulation(Input input) throws IOException, XPRMCompileException, InterruptedException {

        //for (gjentatte kjøringer) {

        ArrayList<Double> totalViolationList = new ArrayList<>();
        ArrayList<Double> percentageViolationsList = new ArrayList<>();
        ArrayList<Double> numberOfTimesVehicleRouteGeneratedList = new ArrayList<>();
        ArrayList<Double> averageTimeBetweenVehicleRouteGeneratedList = new ArrayList<>();
        ArrayList<Double> computationalTimeXpress = new ArrayList<>();
        ArrayList<Double> computationalTimeXpressPlussInitialization = new ArrayList<>();


        for (int i = 1; i <= input.getNumberOfRuns(); i++) {

            String simulationFile = "simulation_Instance" + input.getTestInstance() + "_Nr" + i + ".txt";
            System.out.println("Run number: " + i);

            //Run simulation
            input.updateVehiclesAndStationsToInitialState();
            Simulation simulation = new Simulation();
            simulation.run(simulationFile, input);

            double totalViolations = simulation.getCongestions() + simulation.getStarvations();
            System.out.println("Total violation run " + i + ": " + totalViolations);
            System.out.println("Total nr of customers " + i + ": " + simulation.getTotalNumberOfCustomers());

            totalViolationList.add(totalViolations);
            percentageViolationsList.add((double) totalViolations / (double) simulation.getTotalNumberOfCustomers() * 100);
            numberOfTimesVehicleRouteGeneratedList.add(simulation.getNumberOfTimesVehicleRouteGenerated());
            averageTimeBetweenVehicleRouteGeneratedList.add(average(simulation.getTimeToNextSimulationList()));
            computationalTimeXpress.add(average(simulation.getComputationalTimesXpress()));
            computationalTimeXpressPlussInitialization.add(average(simulation.getComputationalTimesXpressPlussInitialization()));
        }

        double averageViolation = average(totalViolationList);
        double averagePercentageviolations = average(percentageViolationsList);
        double sdViolation = sd(totalViolationList, averageViolation);
        double sdPercentageviolations = sd(percentageViolationsList, averagePercentageviolations);
        double averageNumberOfTimesVehicleRouteGenerated = average(numberOfTimesVehicleRouteGeneratedList);
        double avergaeTimeToVehicleRouteGenerated = average(averageTimeBetweenVehicleRouteGeneratedList);
        double averageComputationalTimeXpress = average(computationalTimeXpress);
        double averageComputationalTimeXpressPlussInitialization = average(computationalTimeXpressPlussInitialization);

        PrintResults.printSimulationResultsToExcelFile(averageViolation, averagePercentageviolations, sdViolation, sdPercentageviolations, averageNumberOfTimesVehicleRouteGenerated,
                avergaeTimeToVehicleRouteGenerated, averageComputationalTimeXpress, averageComputationalTimeXpressPlussInitialization, input);

        System.out.println();
        System.out.println("Average violation percentage: " + averagePercentageviolations);

        //}
    }



    private static void runOneVehicleRouteGeneration(Input input) throws IOException, XPRMCompileException, IllegalArgumentException {

        input.updateVehiclesAndStationsToInitialState();

        double computationalTimeXpress = 0;
        double computationalTimeIncludingInitialization = 0;
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
                computationalTimeXpress = exactMethod.getComputationalTimeXpress();
                computationalTimeIncludingInitialization = exactMethod.getComputationalTimeIncludingInitialization();
                objectiveValue = ReadXpressResult.readObjectiveValue();
                PrintResults.printOneRouteResultsToExcelFile(input, objectiveValue, computationalTimeXpress, computationalTimeIncludingInitialization);
                break;

            case CURRENT_SOLUTION_IN_OSLO:
                throw new IllegalArgumentException("Kan ikke kjøre CURRENT_SOLUTION_IN_OSLO i Xpress");

            case NO_VEHICLES:
                calculateObjectiveFunction(input);
                break;


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




