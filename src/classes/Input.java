package classes;

import functions.ReadDemandAndNumberOfBikes;
import functions.ReadStationInitialState;
import functions.ReadVehicleInput;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Input {

    private int maxNumberOfGenerations = 400;
    private int numberOfVehicles = 5;
    //private ArrayList<Vehicle> vehicles = new ArrayList<>();
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
    private double intraMutationProbability = 0.7;


    //Constructor
    public Input() throws FileNotFoundException {
        this.stationIdList = ReadStationInitialState.readStationInitialState(initialStationFile);
        this.stations = ReadDemandAndNumberOfBikes.readStationInformation(stationIdList, demandFile, initialStationFile);
        //Mangler å lese inn latitude/longitude og distanser

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
    public Station getStation(int stationID){
        return this.stations.get(stationID);
    }

    public HashMap<Integer, Vehicle> getVehicles() {
        return vehicles;
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

    public Integer getStationIdList(int index) {
        return stationIdList.get(index);
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
