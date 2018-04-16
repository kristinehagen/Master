package classes;

import enums.ReOptimizationMethod;
import enums.SolutionMethod;
import functions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Input {


    //Input
    private SolutionMethod solutionMethod = SolutionMethod.EXACT_METHOD;
    private ReOptimizationMethod reOptimizationMethod = ReOptimizationMethod.EVERY_VEHICLE_ARRIVAL;
    private int maxVisit = 1;
    private double timeHorizon = 10;
    private double simulationStartTime = 8*60;              //Minutes
    private double simulationStopTime = 9*60;
    private int testInstance = 1;
    private int nrOfVehicles = 2;
    private int nrStationBranching = 3;             //Create n new routes IN each branching

    //Load in Xpress can be load from heuristic +- loadInterval
    private int loadInterval = 0;

    private int numberOfRuns = 2;



    //--------INITIALIZATION--------------

    private int minLoad = 8;                        //Initial vehicle load må være i intervallet [Min max] for å kunne kjøre til positive og negative stasjoner.
    private int maxLoad = 15;




    //----------COLUMN GENERATION-----------
    //Score
    private double weightTimeToViolation = -0.2;
    private double weightViolationRate = 8;
    private double weightDrivingTime = -0.1;
    private double weightOptimalState = 0.25;

    //Xpress objective function
    private double weightViolation = 0.6;
    private double weightDeviation = 0.3;
    private double weightReward = 0.1;
    private double weightDeviationReward  = 0.6;
    private double weightDrivingTimePenalty = 0.4;






    //------------Xpress--------------------
    private String xpressFile;
    private String timedependentInoutFile = "timeDependentInput.txt";
    private String fixedInputFile = "fixedInput.txt";




    //--------OPTIMAL LEVEL IN XPRESS-----------






    //------------Constants----------------
    private double vehicleHandlingTime = 0.25;
    private double vehicleParkingTime = 2;
    private String demandFile = "demand.txt";






    private ArrayList<Integer> stationIdList;
    private HashMap<Integer, Station> stations;
    private HashMap<Integer, Vehicle> vehicles;
    private ArrayList<Station> stationListWithDemand;


    //Constructor
    public Input() throws IOException {

        //Station file
        String initialStationFile = getStationFile(this.testInstance);
        String vehicleInitialFile = getVehicleFile(this.nrOfVehicles);
        this.xpressFile = determineXpressFile();

        this.stationIdList = ReadStationInitialState.readStationInitialState(initialStationFile);
        this.stations = ReadDemandAndNumberOfBikes.readStationInformation(stationIdList, demandFile, initialStationFile);
        ReadCoordinates.lookUpCoordinates(stations, stationIdList);
        this.vehicles = ReadVehicleInput.readVehicleInput(vehicleInitialFile);
        ReadDistanceMatrix.lookUpDrivingTimes(stations, stationIdList);

        if (solutionMethod.equals(SolutionMethod.HEURISTIC_VERSION_3)) {
            this.maxVisit = 1;}
    }


    public Input(double hour) throws FileNotFoundException {
        this.stationListWithDemand = ReadDemandAndNumberOfBikes.readDemandInformationForGeneratingInstances(demandFile, hour);
    }

    //Create demand scenario
    public Input(int testInstance) throws FileNotFoundException {
        this.testInstance = testInstance;
        String initialStationFile = getStationFile(this.testInstance);
        this.stationIdList = ReadStationInitialState.readStationInitialState(initialStationFile);
        this.stations = ReadDemandAndNumberOfBikes.readStationInformation(stationIdList, demandFile, initialStationFile);
    }

    private String getVehicleFile(int nrOfVehicles) throws IllegalArgumentException {
        switch (nrOfVehicles) {
            case 1:
                return "vehicleInitial1.txt";
            case 2:
                return "vehicleInitial2.txt";
            case 3:
                return "vehicleInitial3.txt";
            case 4:
                return "vehicleInitial4.txt";
            default:
                throw new IllegalArgumentException("Ugyldig antall vehicles");
        }
    }

    private String getStationFile(int testInstance) throws IllegalArgumentException {
        switch (testInstance) {
            case 1:
                return "stationInitialInstance1.txt";
            case 2:
                return "stationInitialInstance2.txt";
            case 3:
                return "stationInitialInstance3.txt";
            case 4:
                return "stationInitialInstance4.txt";
            case 5:
                return "stationInitialInstance5.txt";
            default:
                throw new IllegalArgumentException("Ugyldig testinstanse");
        }
    }

    private String determineXpressFile() {
        switch (solutionMethod) {
            case HEURISTIC_VERSION_1:
                return "heuristicVersion1";
            case HEURISTIC_VERSION_2:
                return "heuristicVersion2";
            case HEURISTIC_VERSION_3:
                return "heuristicVersion3";
            case EXACT_METHOD:
                return "exactMethod";
        }
        return null;
    }


    public void updateVehiclesAndStationsToInitialState() {
        for (Station station : this.stations.values()) {
            station.setLoad(station.getInitialLoad());
        }
        for (Vehicle vehicle : this.vehicles.values()) {
            vehicle.setTimeToNextStation(vehicle.getTimeToNextStationInitial());
            vehicle.setNextStation(vehicle.getNextStationInitial());
            vehicle.setLoad(vehicle.getInitialLoad());
        }
    }


    //Getters and setters


    public int getNumberOfVehicles() {
        return vehicles.size();
    }

    public int getNumberOfStations(){
        return stations.size();
    }

    public HashMap<Integer, Vehicle> getVehicles() {
        return vehicles;
    }

    public Vehicle getVehicle(int vehicleID) {
        return this.vehicles.get(vehicleID);
    }

    public String getDemandFile() {
        return demandFile;
    }

    public HashMap<Integer, Station> getStations() {
        return stations;
    }

    public ArrayList<Integer> getStationIdList() {
        return stationIdList;
    }

    public int getNrStationBranching() {
        return nrStationBranching;
    }

    public void setNrStationBranching(int nrStationBranching) {
        this.nrStationBranching = nrStationBranching;
    }

    public double getWeightOptimalState() {
        return weightOptimalState;
    }

    public void setWeightOptimalState(double weightOptimalState) {
        this.weightOptimalState = weightOptimalState;
    }

    public double getWeightDrivingTime() {
        return weightDrivingTime;
    }

    public void setWeightDrivingTime(double weightDrivingTime) {
        this.weightDrivingTime = weightDrivingTime;
    }

    public double getWeightViolationRate() {
        return weightViolationRate;
    }

    public void setWeightViolationRate(double weightViolationRate) {
        this.weightViolationRate = weightViolationRate;
    }

    public double getWeightTimeToViolation() {
        return weightTimeToViolation;
    }

    public void setWeightTimeToViolation(double weightTimeToViolation) {
        this.weightTimeToViolation = weightTimeToViolation;
    }

    public int getMaxLoad() {
        return maxLoad;
    }

    public void setMaxLoad(int maxLoad) {
        this.maxLoad = maxLoad;
    }

    public int getMinLoad() {
        return minLoad;
    }

    public void setMinLoad(int minLoad) {
        this.minLoad = minLoad;
    }

    public double getSimulationStartTime() {
        return simulationStartTime;
    }

    public void setSimulationStartTime(double simulationStartTime) {
        this.simulationStartTime = simulationStartTime;
    }

    public double getTimeHorizon() {
        return timeHorizon;
    }

    public void setTimeHorizon(double timeHorizon) {
        this.timeHorizon = timeHorizon;
    }

    public ArrayList<Station> getStationListWithDemand() {
        return stationListWithDemand;
    }

    public void setStationListWithDemand(ArrayList<Station> stationListWithDemand) {
        this.stationListWithDemand = stationListWithDemand;
    }

    public SolutionMethod getSolutionMethod() {
        return solutionMethod;
    }

    public void setSolutionMethod(SolutionMethod solutionMethod) {
        this.solutionMethod = solutionMethod;
    }

    public double getVehicleHandlingTime() {
        return vehicleHandlingTime;
    }

    public void setVehicleHandlingTime(double vehicleHandlingTime) {
        this.vehicleHandlingTime = vehicleHandlingTime;
    }

    public double getVehicleParkingTime() {
        return vehicleParkingTime;
    }

    public void setVehicleParkingTime(double vehicleParkingTime) {
        this.vehicleParkingTime = vehicleParkingTime;
    }

    public String getTimedependentInoutFile() {
        return timedependentInoutFile;
    }

    public void setTimedependentInoutFile(String timedependentInoutFile) {
        this.timedependentInoutFile = timedependentInoutFile;
    }

    public String getFixedInputFile() {
        return fixedInputFile;
    }

    public void setFixedInputFile(String fixedInputFile) {
        this.fixedInputFile = fixedInputFile;
    }

    public double getWeightViolation() {
        return weightViolation;
    }

    public void setWeightViolation(double weightViolation) {
        this.weightViolation = weightViolation;
    }

    public double getWeightDeviation() {
        return weightDeviation;
    }

    public void setWeightDeviation(double weightDeviation) {
        this.weightDeviation = weightDeviation;
    }

    public double getWeightReward() {
        return weightReward;
    }

    public void setWeightReward(double weightReward) {
        this.weightReward = weightReward;
    }

    public double getWeightDeviationReward() {
        return weightDeviationReward;
    }

    public void setWeightDeviationReward(double weightDeviationReward) {
        this.weightDeviationReward = weightDeviationReward;
    }

    public double getWeightDrivingTimePenalty() {
        return weightDrivingTimePenalty;
    }

    public void setWeightDrivingTimePenalty(double weightDrivingTimePenalty) {
        this.weightDrivingTimePenalty = weightDrivingTimePenalty;
    }

    public int getMaxVisit() {
        return maxVisit;
    }

    public void setMaxVisit(int maxVisit) {
        this.maxVisit = maxVisit;
    }


    public int getLoadInterval() {
        return loadInterval;
    }

    public void setLoadInterval(int loadInterval) {
        this.loadInterval = loadInterval;
    }


    public String getXpressFile() {
        return xpressFile;
    }

    public void setXpressFile(String xpressFile) {
        this.xpressFile = xpressFile;
    }

    public int getNumberOfRuns() {
        return numberOfRuns;
    }

    public void setNumberOfRuns(int numberOfRuns) {
        this.numberOfRuns = numberOfRuns;
    }

    public double getSimulationStopTime() {
        return simulationStopTime;
    }

    public void setSimulationStopTime(double simulationStopTime) {
        this.simulationStopTime = simulationStopTime;
    }

    public ReOptimizationMethod getReOptimizationMethod() {
        return reOptimizationMethod;
    }

    public void setReOptimizationMethod(ReOptimizationMethod reOptimizationMethod) {
        this.reOptimizationMethod = reOptimizationMethod;
    }

    public int getTestInstance() {
        return testInstance;
    }

    public void setTestInstance(int testInstance) {
        this.testInstance = testInstance;
    }


}
