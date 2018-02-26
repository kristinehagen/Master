package classes;

import functions.ReadCoordinates;
import functions.ReadDemandAndNumberOfBikes;
import functions.ReadStationInitialState;
import functions.ReadVehicleInput;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Input {

    private int currentHour = 8;
    private int maxNumberOfGenerations = 400;
    private int sizeOfPopulation = 20;
    private int maxVisitsForEachVehicle = 3;
    private int minVisitsForEachVehicle = 2;

    //Score
    private double weightTimeToViolation = 0.25;
    private double weightViolationRate = 0.25;
    private double weightDrivingTime = 0.25;
    private double weightOptimalState = 0.25;

    //Brancing
    private int nrStationBranching = 3;                 //Create n new routes for each branching
    private double maxTimeForOneBranch = 40;            //minutes


    //Vehicle load må være i intervallet [Min max] for å kunne kjøre til positive og negative stasjoner.
    private int minLoad = 8;
    private int maxLoad = 15;

    private int tournamentParticipants = 2;
    private double crossoverProbability = 0.8;
    private double intraMutationProbability = 1;

    private String filename = "filename.txt";
    private String demandFile = "demand.txt";
    private String initialStationFile = "stationInitial.txt";
    private String vehicleInitialFile = "vehicleInitial.txt";

    private ArrayList<Integer> stationIdList;
    private HashMap<Integer, Station> stations;
    private HashMap<Integer, Vehicle> vehicles;



    //Constructor
    public Input() throws FileNotFoundException {
        this.stationIdList = ReadStationInitialState.readStationInitialState(initialStationFile);
        this.stations = ReadDemandAndNumberOfBikes.readStationInformation(stationIdList, demandFile, initialStationFile);
        ReadCoordinates.lookUpCoordinates(stations, stationIdList);

        this.vehicles = ReadVehicleInput.readVehicleInput(vehicleInitialFile, stations);
    }

    //Getters and setters

    public int getMinVisitsForEachVehicle() {
        return minVisitsForEachVehicle;
    }

    public void setMinVisitsForEachVehicle(int minVisitsForEachVehicle) {
        this.minVisitsForEachVehicle = minVisitsForEachVehicle;
    }

    public double getIntraMutationProbability() {
        return intraMutationProbability;
    }

    public int getTournamentParticipants() {
        return tournamentParticipants;
    }

    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    public int getSizeOfPopulation() {
        return sizeOfPopulation;
    }

    public int getMaxVisitsForEachVehicle() {
        return maxVisitsForEachVehicle;
    }

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

    public String getInitialStationFile() {
        return initialStationFile;
    }

    public HashMap<Integer, Station> getStations() {
        return stations;
    }

    public ArrayList<Integer> getStationIdList() {
        return stationIdList;
    }

    public int getMaxNumberOfGenerations() {
        return maxNumberOfGenerations;
    }

    public void setMaxNumberOfGenerations(int maxNumberOfGenerations) {
        this.maxNumberOfGenerations = maxNumberOfGenerations;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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

    public double getMaxTimeForOneBranch() {
        return maxTimeForOneBranch;
    }

    public void setMaxTimeForOneBranch(double maxTimeForOneBranch) {
        this.maxTimeForOneBranch = maxTimeForOneBranch;
    }

    public int getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;
    }
}
