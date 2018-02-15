package Classes;

import functions.ReadDemandAndNumberOfBikes;
import functions.ReadStationInitialState;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Input {

    private int maxNumberOfGenerations = 400;
    private int maxVisitsForEachVehicle = 5;



    private int sizeOfPopulation = 100;
    private int numberOfVehicles = 5;
    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    private String filename = "filename.txt";
    private String demandFile = "demand.txt";
    private String initialStationFile = "stationInitial.txt";

    private HashMap<Integer, Station> stations;
    private ArrayList<Integer> stationIdList;




    //Constructor
    public Input() throws FileNotFoundException {
        this.stationIdList = ReadStationInitialState.readStationInitialState(initialStationFile);
        this.stations = ReadDemandAndNumberOfBikes.readStationInformation(stationIdList, demandFile, initialStationFile);
        //Mangler å lese inn latitude/longitude og distanser

        //Midlertidig
        for (int i = 0; i < numberOfVehicles; i++) {
            Vehicle vehicle = new Vehicle();
            this.vehicles.add(vehicle);
        }
    }


    //Getters and setters

    public int getSizeOfPopulation() {
        return sizeOfPopulation;
    }

    public int getMaxVisitsForEachVehicle() {
        return maxVisitsForEachVehicle;
    }

    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public ArrayList<Vehicle> getVehicles() {
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
