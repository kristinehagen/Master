package main;

import classes.*;
import com.dashoptimization.XPRMCompileException;
import enums.SolutionMethod;

import java.io.IOException;
import java.util.ArrayList;


public class Run {


    public static void main(String[] args) throws IOException, XPRMCompileException {
        Input input = new Input();
        SolutionMethod solutionMethod = input.getSolutionMethod();

        switch (solutionMethod) {
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
                ExactMethod optimalInXpress = new ExactMethod(input);
                break;
        }

        System.out.println("algorithm successfully terminated");

        /*

        for (int timehorizon = 10; timehorizon < 20; timehorizon = timehorizon + 10) {
            input.setTimeHorizon(timehorizon);

            ArrayList<Double> violationList = new ArrayList<>();
            ArrayList<Double> percentageViolationsList = new ArrayList<>();
            ArrayList<Double> numberOfXpressRunsList = new ArrayList<>();
            ArrayList<Double> simulationIntervalList = new ArrayList<>();

            for (int i = 1; i <= input.getNumberOfRuns(); i++) {

                String simulationFile = "simulationSet8-" + i + ".txt";
                System.out.println("Time horizon: " + input.getTimeHorizon());
                System.out.println("Run number: " + i);

                Simulation simulation = new Simulation();
                simulation.run(simulationFile, input);
                double totalViolations = simulation.getCongestions() + simulation.getStarvations();
                violationList.add(totalViolations);
                double percentageViolations = (double) totalViolations / (double) simulation.getTotalNumberOfCustomers() * 100;
                percentageViolationsList.add(percentageViolations);
                numberOfXpressRunsList.add(simulation.getNumberOfXpress());
                double averageTimeToNextSimulation = average(simulation.getTimeToNextSimulationList());
                simulationIntervalList.add(averageTimeToNextSimulation);
            }
            double averageViolation = average(violationList);
            double averagePercentageHappyCustomers = average(percentageViolationsList);
            double sdViolation = sd(violationList, averageViolation);
            double sdPercentageHappyCustomers = sd(percentageViolationsList, averagePercentageHappyCustomers);
            double averageNumberOfXpressRuns = average(numberOfXpressRunsList);
            double avergaeTimeToNextSimulation = average(simulationIntervalList);
            print(averageViolation, averagePercentageHappyCustomers, sdViolation, sdPercentageHappyCustomers,
                    timeHorizon, numberOfRuns, M, weightViolation, weightDeviation, weightReward,
                    weightDeviationReward, weightDrivingTimePenalty, averageNumberOfXpressRuns,
                    avergaeTimeToNextSimulation, moselFile, testInstance);

        }

        */
    }


}




