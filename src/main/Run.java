package main;

import classes.*;
import com.dashoptimization.XPRMCompileException;
import functions.PrintResults;

import java.io.IOException;
import java.util.ArrayList;


public class Run {


    public static void main(String[] args) throws IOException, XPRMCompileException {
        Input input = new Input();

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

            totalViolationList.add(simulation.getCongestions() + simulation.getStarvations());
            percentageViolationsList.add((double) simulation.getCongestions() + simulation.getStarvations() / (double) simulation.getTotalNumberOfCustomers() * 100);
            numberOfTimesVehicleRouteGeneratedList.add(simulation.getNumberOfTimesVehicleRouteGenerated());
            averageTimeBetweenVehicleRouteGeneratedList.add(average(simulation.getTimeToNextSimulationList()));
        }

        double averageViolation = average(totalViolationList);
        double averagePercentageviolations = average(percentageViolationsList);
        double sdViolation = sd(totalViolationList, averageViolation);
        double sdPercentageviolations = sd(percentageViolationsList, averagePercentageviolations);
        double averageNumberOfTimesVehicleRouteGenerated = average(numberOfTimesVehicleRouteGeneratedList);
        double avergaeTimeToVehicleRouteGenerated = average(averageTimeBetweenVehicleRouteGeneratedList);

        PrintResults.printToExcelFile(averageViolation, averagePercentageviolations, sdViolation, sdPercentageviolations, averageNumberOfTimesVehicleRouteGenerated,
                avergaeTimeToVehicleRouteGenerated, input);

        System.out.println("Average violation percentage: " + averagePercentageviolations);

        System.out.println("algorithm successfully terminated");

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




