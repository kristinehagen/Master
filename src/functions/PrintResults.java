package functions;

import classes.Input;

import java.util.ArrayList;

public class PrintResults {

    public static void printSimulationResultsToExcelFile(double averageViolation, double averagePercentageVoilation, double sdViolations, double sdPercentageViolation,
                                                       double averageNumberOfTimesVehicleRouteGenerated, double avergageTimeToVehicleRouteGenerated,
                                                       double avergaeComputationalTimeXpress, double averageComputationalTimeXpressPlussInitialization, Input input) {

        //Marte :D
        System.out.println("avergaeComputationalTimeXpress: " + avergaeComputationalTimeXpress);
        System.out.println("averageComputationalTimeXpressPlussInitialization" + averageComputationalTimeXpressPlussInitialization);

    }

    public static void printOneRouteResultsToExcelFile(Input input, double objectiveValue, double computationalTimeXpress, double computationalTimeIncludingInitialization) {

        System.out.println("Objective value: " + objectiveValue);
        System.out.println("Computational time: " + computationalTimeXpress);
        System.out.println("Computational time including initialization: " + computationalTimeIncludingInitialization);
        //Marte :D

    }

}
