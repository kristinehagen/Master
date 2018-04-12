package classes;

import com.dashoptimization.XPRMCompileException;
import enums.NextEvent;
import functions.NextSimulation;
import xpress.ReadVehicleArrival;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Simulation {

    private ArrayList<VehicleArrival> vehicleArrivals;
    private double numberOfTimesVehicleRouteGenerated = 0;
    private double congestions = 0;
    private double starvations = 0;
    private int totalNumberOfCustomers = 0;
    private int happyCustomers = 0;
    private double timeToNextSimulation;                                   //in seconds
    private ArrayList<Double> timeToNextSimulationList = new ArrayList<>();

    //Constructor
    public Simulation() {
    }

    public void run(String simulationFile, Input input) throws IOException, XPRMCompileException {

        double currentTime = input.getSimulationStartTime();
        boolean simulating = true;

        //Xpress
        WriteXpressFiles.printFixedInput(input);

        //Demand input file
        CustomerArrival nextCustomerArrival = new CustomerArrival();
        File inputFile = new File(simulationFile);
        Scanner in = new Scanner(inputFile);

        //Fixed values
        double simulationStopTime = input.getSimulationStopTime();                              //Actual time minutes

        while (simulating) {

            //Generate routes for service vehicles
            generateVehicleRoute(input);
            numberOfTimesVehicleRouteGenerated += 1;
            this.vehicleArrivals = ReadVehicleArrival.readVehicleArrivals(currentTime);         //Actual arrival times minutes

            //Determine next vehicle arrival
            int vehicleArrivalCounter = 0;
            VehicleArrival nextVehicleArrival = vehicleArrivals.get(vehicleArrivalCounter);         //Actual time minutes

            //Determine next customer arrival
            nextCustomerArrival.updateNextCustomerArrival(in, currentTime, nextCustomerArrival, simulationStopTime);        //Actual time minutes

            //Determine time to next vehicle routes
            double timeToNewVehicleRoutes = NextSimulation.determineTimeToNextSimulation(vehicleArrivals, input.getTimeHorizon(), input.getReOptimizationMethod()) + currentTime;      //Actual time minutes
            this.timeToNextSimulationList.add(timeToNewVehicleRoutes);


            //Determine next event (simulation stop, new vehicle routes, customer arrival, vehicle arrival)
            boolean determineNextEvent = true;

            while (determineNextEvent) {

                double nextEventTime = simulationStopTime;
                NextEvent nextEvent = NextEvent.SIMULATION_STOP;

                if (timeToNewVehicleRoutes < nextEventTime) {
                    nextEventTime = timeToNewVehicleRoutes;
                    nextEvent = NextEvent.NEW_VEHICLE_ROUTES;
                }
                if (nextCustomerArrival.getTime() < nextEventTime) {
                    nextEventTime = nextCustomerArrival.getTime();
                    nextEvent = NextEvent.CUSTOMER_ARRIVAL;
                }
                if (nextVehicleArrival.getTime() < nextEventTime) {
                    nextEventTime = nextVehicleArrival.getTime();
                    nextEvent = NextEvent.VEHICLE_ARRIVAL;
                }


                switch (nextEvent) {

                    case SIMULATION_STOP:
                        determineNextEvent = false;
                        simulating = false;
                        break;

                    case NEW_VEHICLE_ROUTES:
                        currentTime = timeToNewVehicleRoutes;
                        determineRemainingDrivingTimeAndStation(timeToNewVehicleRoutes, input.getVehicles() );
                        determineNextEvent = false;
                        break;

                    case CUSTOMER_ARRIVAL:
                        //TBC

                        determineNextEvent = false;
                        simulating = false;
                        break;

                    case VEHICLE_ARRIVAL:
                        //TBC

                        determineNextEvent = false;
                        simulating = false;
                        break;

                }

            }

        }
    }

    private void generateVehicleRoute(Input input) throws IOException, XPRMCompileException {
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
        }
    }

    private void determineRemainingDrivingTimeAndStation(double timeForNewVehicleRoutes, HashMap<Integer, Vehicle> vehicles) {
        for (VehicleArrival vehicleArrival : vehicleArrivals) {

            boolean vehicleArrivalBeforeSimulationStopTime = vehicleArrival.getTime() < timeForNewVehicleRoutes;
            boolean nextVehicleArrivalAfterOrAtSimulationStopTime = vehicleArrival.getTimeNextVisit() >= timeForNewVehicleRoutes;
            boolean vehicleArrivalFirstVisit = vehicleArrival.isFirstvisit();
            boolean vehicleArrivalAfterOrAtSimulationStopTime = vehicleArrival.getTime() >= timeForNewVehicleRoutes;
            boolean nextStationIsArtificialStation = vehicleArrival.getNextStationId() == 0;

            if ( vehicleArrivalBeforeSimulationStopTime & nextVehicleArrivalAfterOrAtSimulationStopTime & !nextStationIsArtificialStation) {
                int vehicleId = vehicleArrival.getVehicle();
                Vehicle vehicle = vehicles.get(vehicleId);
                double timeToNextStation = vehicleArrival.getTimeNextVisit()-timeForNewVehicleRoutes;
                vehicle.setTimeToNextStation(timeToNextStation);
                vehicle.setNextStation(vehicleArrival.getNextStationId());

            } else if (vehicleArrivalFirstVisit & vehicleArrivalAfterOrAtSimulationStopTime){
                int vehicleId = vehicleArrival.getVehicle();
                Vehicle vehicle = vehicles.get(vehicleId);
                double timeToNextStation = vehicleArrival.getTime()-timeForNewVehicleRoutes;
                vehicle.setTimeToNextStation(timeToNextStation);
                vehicle.setNextStation(vehicleArrival.getStationId());

            }  else if (nextStationIsArtificialStation & vehicleArrivalBeforeSimulationStopTime ) {
                int vehicleId = vehicleArrival.getVehicle();
                Vehicle vehicle = vehicles.get(vehicleId);
                vehicle.setTimeToNextStation(0);
                vehicle.setNextStation(vehicleArrival.getStationId());
            }
        }
    }
}






















