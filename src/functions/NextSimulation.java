package functions;

import classes.*;
import enums.ReOptimizationMethod;

import java.util.ArrayList;

public class NextSimulation {

    public static double determineTimeToNextSimulation(ArrayList<VehicleArrival> stationVisitsToSimulate, double timeHorizon, ReOptimizationMethod reOptimizationMethod) {
        double firstVisit = timeHorizon;
        double secondVisit = timeHorizon;
        double thirdVisit = timeHorizon;

        for (VehicleArrival vehicleArrival : stationVisitsToSimulate) {
            double time = vehicleArrival.getTime();
            if (time > 0.01 & time < firstVisit) {
                firstVisit = time;
            }
            if (time > 0.01 & time < secondVisit & time > firstVisit) {
                secondVisit = time;
            }
            if (time > 0.01 & time < thirdVisit & time > secondVisit) {
                thirdVisit = time;
            }
        }

        double nextSimulation = 0;

        switch (reOptimizationMethod) {
            case EVERY_VEHICLE_ARRIVAL:
                nextSimulation = firstVisit;
                break;
            case EVERY_SECOND_VEHICLE_ARRIVAL:
                nextSimulation = secondVisit;
                break;
            case EVERY_THIRD_VEHICLE_ARRIVAL:
                nextSimulation = thirdVisit;
                break;
            case TEN_MIN:
                nextSimulation = 10;
                break;
            case TWENTY_MIN:
                nextSimulation = 20;
                break;
            case THIRTY_MIN:
                nextSimulation = 30;
                break;
        }

        System.out.println("Next simulation " + nextSimulation);
        System.out.println();
        return nextSimulation;
    }
}