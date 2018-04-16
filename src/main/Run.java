package main;

import classes.*;
import com.dashoptimization.XPRMCompileException;
import functions.PrintResults;
import solutionMethods.*;
import xpress.ReadXpressResult;

import java.io.IOException;
import java.util.ArrayList;


public class Run {


    public static void main(String[] args) throws IOException, XPRMCompileException {

        Input input = new Input();

        //Simulate
        if (input.isSimulation()) {
            runSimulation(input);
        } else {
            runOneVehicleRouteGeneration(input);
        }

        System.out.println("algorithm successfully terminated");

    }

    private static void runSimulation(Input input) throws IOException, XPRMCompileException {

        ArrayList<Double> totalViolationList = new ArrayList<>();
        ArrayList<Double> percentageViolationsList = new ArrayList<>();
        ArrayList<Double> numberOfTimesVehicleRouteGeneratedList = new ArrayList<>();
        ArrayList<Double> averageTimeBetweenVehicleRouteGeneratedList = new ArrayList<>();

        for (int i = 1; i <= input.getNumberOfRuns(); i++) {

            String simulationFile = "simulation_Instance"+ input.getTestInstance() + "_Nr" + i + ".txt";
            System.out.println("Run number: " + i);

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
        }

        double averageViolation = average(totalViolationList);
        double averagePercentageviolations = average(percentageViolationsList);
        double sdViolation = sd(totalViolationList, averageViolation);
        double sdPercentageviolations = sd(percentageViolationsList, averagePercentageviolations);
        double averageNumberOfTimesVehicleRouteGenerated = average(numberOfTimesVehicleRouteGeneratedList);
        double avergaeTimeToVehicleRouteGenerated = average(averageTimeBetweenVehicleRouteGeneratedList);

        PrintResults.printOneRouteResultsToExcelFile(averageViolation, averagePercentageviolations, sdViolation, sdPercentageviolations, averageNumberOfTimesVehicleRouteGenerated,
                avergaeTimeToVehicleRouteGenerated, input);

        System.out.println();
        System.out.println("Average violation percentage: " + averagePercentageviolations);

    }

    private static void runOneVehicleRouteGeneration(Input input) throws IOException, XPRMCompileException, IllegalArgumentException {

        input.updateVehiclesAndStationsToInitialState();

        switch (input.getSolutionMethod()) {
            case HEURISTIC_VERSION_1:
                HeuristicVersion1 heuristicVersion1 = new HeuristicVersion1(input);
                break;
            case HEURISTIC_VERSION_2:
                HeuristicVersion2 heuristicVersion2 = new HeuristicVersion2(input);
                break;
            case HEURISTIC_VERSION_3:
                HeuristicVersion3 heuristicVersion3 = new HeuristicVersion3(input);
                break;
            case EXACT_METHOD:
                ExactMethod exactMethod = new ExactMethod(input);
                break;
            case CURRENT_SOLUTION_IN_OSLO:
                throw new IllegalArgumentException("Kan ikke kjøre CURRENT_SOLUTION_IN_OSLO i Xpress");
            case NO_VEHICLES:
                throw new IllegalArgumentException("Kan ikke kjøre NO_VEHICLES i Xpress");
        }

        double objectiveValue = ReadXpressResult.readObjectiveValue();
        PrintResults.printOneRouteResultsToExcelFile(input, objectiveValue);
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




