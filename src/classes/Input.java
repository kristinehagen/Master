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
    private SolutionMethod solutionMethod = SolutionMethod.HEURISTIC_VERSION_3;
    private ReOptimizationMethod reOptimizationMethod = ReOptimizationMethod.EVERY_VEHICLE_ARRIVAL;
    private int maxVisit = 1;
    private double timeHorizon = 20;
    private double simulationStartTime = 7*60;              //Minutes
    private double simulationStopTime = 11*60;
    private int testInstance = 5;
    private int nrOfVehicles = 2;
    private int nrStationBranching = 0;             //Create n new routes in each branching
    private int loadInterval = 11;                   //Load in Xpress can be load from heuristic 2 +- loadInterval
    private int numberOfRuns = 10;                   //Vanlig med 15
    private boolean simulation = true;


    //--------CLUSTER-----------

    private  boolean clustering = false;
    private boolean dynamicClustering = false;
    private double highDemand = 35;
    private double mediumDemand = 11.5;


    //--------PRICING PROBLEM---------------

    private boolean runPricingProblem = false;
    private int nrOfRunsPricingProblem = 5;         //OBS! Have to be 1 or larger
    private int nrOfBranchingPricingProblem = 5;
    private int probabilityOfChoosingUnvisitedStation = 50;     //40%






    //----------WEIGHTS-----------
    //Criticality score
    private double weightCritScTimeToViolation = 0.0;
    private double weightCritScViolationRate = 0.7;
    private double weightCritScDrivingTime = 0.0;
    private double weightCritScOptimalState = 0.3;
    private double weightPricingProblemScore = 8;

    //Criticality score Current solution in Oslo
    private double weightCritScTimeToViolationCurrent = 0.7;
    private double weightCritScViolationRateCurrent = 0.3;
    private double weightCritScDrivingTimeCurrent = 0;
    private double weightCritScOptimalStateCurrent = 0;
    private double weightPricingProblemScoreCurrent = 0;

    //Xpress objective function
    private double weightViolation = 0.6;
    private double weightDeviation = 0.3;
    private double weightReward = 0.1;
    private double weightDeviationReward  = 0.6;
    private double weightDrivingTimePenalty = 0.4;

    //Cluster
    private double weightClusterNetDemand = 0.1;
    private double weightClusterDrivingTime = 0.3;
    private double weightClusterEqualSize = 0.4;



    //--------INITIALIZATION--------------

    private int minLoad = 5;                        //Initial vehicle load må være i intervallet [Min max] for å kunne kjøre til positive og negative stasjoner.
    private int maxLoad = 18;

    private double currentMinute;
    private double tresholdLengthRoute = 5;



    //------------Xpress--------------------
    private String xpressFile;
    private String timedependentInputFile = "timeDependentInput.txt";
    private String fixedInputFile = "fixedInput.txt";
    private int visitInterval = 0;                                          //Bilen må ha load på med enn visitInterval for å besøke en delivery stason som siste besøk og vice verca



    //------------Constants----------------
    private double vehicleHandlingTime = 0.25;
    private double vehicleParkingTime = 2;
    private String demandFile = "demand.txt";

    private boolean isNowRunningPricingProblem = false;





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
        currentMinute = simulationStartTime;

        this.stationIdList = ReadStationInitialState.readStationInitialState(initialStationFile);
        this.stations = ReadDemandAndNumberOfBikes.readStationInformation(stationIdList, demandFile, initialStationFile);
        ReadCoordinates.lookUpCoordinates(stations, stationIdList);
        this.vehicles = ReadVehicleInput.readVehicleInput(vehicleInitialFile);
        ReadDistanceMatrix.lookUpDrivingTimes(stations, stationIdList);

        if (solutionMethod.equals(SolutionMethod.HEURISTIC_VERSION_3)) {
            this.maxVisit = 1;}

        //Update to initial values
        updateVehiclesAndStationsToInitialState();

        //No cluster if only one vehicle
        if (nrOfVehicles == 1) {
            clustering = false;
        }

    }

    //Generate test instance
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
            case 5:
                return "vehicleInitial5.txt";
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

    public double getWeightCritScOptimalState() {
        return weightCritScOptimalState;
    }

    public void setWeightCritScOptimalState(double weightCritScOptimalState) {
        this.weightCritScOptimalState = weightCritScOptimalState;
    }

    public double getWeightCritScDrivingTime() {
        return weightCritScDrivingTime;
    }

    public void setWeightCritScDrivingTime(double weightCritScDrivingTime) {
        this.weightCritScDrivingTime = weightCritScDrivingTime;
    }

    public double getWeightCritScViolationRate() {
        return weightCritScViolationRate;
    }

    public void setWeightCritScViolationRate(double weightCritScViolationRate) {
        this.weightCritScViolationRate = weightCritScViolationRate;
    }

    public double getWeightCritScTimeToViolation() {
        return weightCritScTimeToViolation;
    }

    public void setWeightCritScTimeToViolation(double weightCritScTimeToViolation) {
        this.weightCritScTimeToViolation = weightCritScTimeToViolation;
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

    public String getTimedependentInputFile() {
        return timedependentInputFile;
    }

    public void setTimedependentInputFile(String timedependentInputFile) {
        this.timedependentInputFile = timedependentInputFile;
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

    public int getNrOfRunsPricingProblem() {
        return nrOfRunsPricingProblem;
    }

    public boolean isRunPricingProblem() {
        return runPricingProblem;
    }


    public double getWeightPricingProblemScore() {
        return weightPricingProblemScore;
    }

    public int getNrOfBranchingPricingProblem() {
        return nrOfBranchingPricingProblem;
    }

    public boolean isNowRunningPricingProblem() {
        return isNowRunningPricingProblem;
    }

    public void setNowRunningPricingProblem(boolean nowRunningPricingProblem) {
        isNowRunningPricingProblem = nowRunningPricingProblem;
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


    public boolean isSimulation() {
        return simulation;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    public double getCurrentMinute() {
        return currentMinute;
    }

    public void setCurrentMinute(double currentMinute) {
        this.currentMinute = currentMinute;
    }

    public int getVisitInterval() {
        return visitInterval;
    }

    public void setVisitInterval(int visitInterval) {
        this.visitInterval = visitInterval;
    }

    public double getTresholdLengthRoute() {
        return tresholdLengthRoute;
    }

    public void setTresholdLengthRoute(double tresholdLengthRoute) {
        this.tresholdLengthRoute = tresholdLengthRoute;
    }

    public int getProbabilityOfChoosingUnvisitedStation() {
        return probabilityOfChoosingUnvisitedStation;
    }

    public void setProbabilityOfChoosingUnvisitedStation(int probabilityOfChoosingUnvisitedStation) {
        this.probabilityOfChoosingUnvisitedStation = probabilityOfChoosingUnvisitedStation;
    }

    public double getWeightCritScTimeToViolationCurrent() {
        return weightCritScTimeToViolationCurrent;
    }

    public void setWeightCritScTimeToViolationCurrent(double weightCritScTimeToViolationCurrent) {
        this.weightCritScTimeToViolationCurrent = weightCritScTimeToViolationCurrent;
    }

    public double getWeightCritScViolationRateCurrent() {
        return weightCritScViolationRateCurrent;
    }

    public void setWeightCritScViolationRateCurrent(double weightCritScViolationRateCurrent) {
        this.weightCritScViolationRateCurrent = weightCritScViolationRateCurrent;
    }

    public double getWeightCritScDrivingTimeCurrent() {
        return weightCritScDrivingTimeCurrent;
    }

    public void setWeightCritScDrivingTimeCurrent(double weightCritScDrivingTimeCurrent) {
        this.weightCritScDrivingTimeCurrent = weightCritScDrivingTimeCurrent;
    }

    public double getWeightCritScOptimalStateCurrent() {
        return weightCritScOptimalStateCurrent;
    }

    public void setWeightCritScOptimalStateCurrent(double weightCritScOptimalStateCurrent) {
        this.weightCritScOptimalStateCurrent = weightCritScOptimalStateCurrent;
    }

    public double getWeightClusterNetDemand() {
        return weightClusterNetDemand;
    }

    public void setWeightClusterNetDemand(double weightClusterNetDemand) {
        this.weightClusterNetDemand = weightClusterNetDemand;
    }

    public double getWeightClusterDrivingTime() {
        return weightClusterDrivingTime;
    }

    public void setWeightClusterDrivingTime(double weightClusterDrivingTime) {
        this.weightClusterDrivingTime = weightClusterDrivingTime;
    }

    public double getWeightClusterEqualSize() {
        return weightClusterEqualSize;
    }

    public void setWeightClusterEqualSize(double weightClusterEqualSize) {
        this.weightClusterEqualSize = weightClusterEqualSize;
    }

    public double getHighDemand() {
        return highDemand;
    }

    public void setHighDemand(double highDemand) {
        this.highDemand = highDemand;
    }

    public double getMediumDemand() {
        return mediumDemand;
    }

    public void setMediumDemand(double mediumDemand) {
        this.mediumDemand = mediumDemand;
    }

    public boolean isClustering() {
        return clustering;
    }

    public void setClustering(boolean clustering) {
        this.clustering = clustering;
    }

    public boolean isDynamicClustering() {
        return dynamicClustering;
    }

    public void setDynamicClustering(boolean dynamicClustering) {
        this.dynamicClustering = dynamicClustering;
    }
}
