package classes;

import functions.ReadCoordinates;
import functions.ReadDemandAndNumberOfBikes;
import functions.ReadStationInitialState;
import functions.ReadVehicleInput;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Input {

    private int maxNumberOfGenerations = 400;
    private int numberOfVehicles = 5;
    private int maxVisitsForEachVehicle = 3;
    private int sizeOfPopulation = 20;

    private String filename = "filename.txt";
    private String demandFile = "demand.txt";
    private String initialStationFile = "stationInitial.txt";
    private String vehicleInitialFile = "vehicleInitial.txt";

    private ArrayList<Integer> stationIdList;
    private HashMap<Integer, Station> stations;
    private HashMap<Integer, Vehicle> vehicles;

    private int tournamentParticipants = 2;
    private double crossoverProbability = 0.8;
    private double intraMutationProbability = 1;


    //Constructor
    public Input() throws FileNotFoundException {
        this.stationIdList = ReadStationInitialState.readStationInitialState(initialStationFile);
        this.stations = ReadDemandAndNumberOfBikes.readStationInformation(stationIdList, demandFile, initialStationFile);
        ReadCoordinates.lookUpCoordinates(stations, stationIdList);

        this.vehicles = ReadVehicleInput.readVehicleInput(vehicleInitialFile, stations);
    }

    //Getters and setters

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


}
