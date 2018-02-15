package classes;

import functions.ReadDemandAndNumberOfBikes;
import functions.ReadStationInitialState;
import functions.ReadVehicleInput;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Input {

    private int maxNumberOfGenerations = 400;
    private int maxVisitsForEachVehicle = 3;
    private int sizeOfPopulation = 20;

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
        //Mangler å lese inn latitude/longitude og distanser

        this.vehicles = ReadVehicleInput.readVehicleInput(vehicleInitialFile, stations);
    }


    //Getters and setters

    public int getSizeOfPopulation() {
        return sizeOfPopulation;
    }

    public int getMaxVisitsForEachVehicle() {
        return maxVisitsForEachVehicle;
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
