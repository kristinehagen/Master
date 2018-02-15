package classes;

import java.util.ArrayList;
import java.util.HashMap;

public class Vehicle {

    private int id;
    private int nextStation;
    private int nextStationInitial;
    private double timeToNextStation;
    private double timeToNextStationInitial;
    private int capacity;
    private int load;
    private int initialLoad;
    private ArrayList<Station> cluster = new ArrayList<>();

    public Vehicle(int id, HashMap<Integer, Station> stations) {
        this.id = id;
        createCluster(stations);
    }

    //MIDLERTIDIG - Skal returnere stasjonenen som denne bilen har lov til å besøke
    public void createCluster(HashMap<Integer, Station> stations){
        ArrayList<Station> stationsList = new ArrayList<>();
        for (Station station : stations.values()) {
            stationsList.add(station);
        }
        this.cluster = stationsList;
    }



    //Getter and setters

    public ArrayList<Station> getCluster(HashMap<Integer, Station> stations) {
        return cluster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNextStation() {
        return nextStation;
    }

    public void setNextStation(int nextStation) {
        this.nextStation = nextStation;
    }

    public double getTimeToNextStation() {
        return timeToNextStation;
    }

    public void setTimeToNextStation(double timeToNextStation) {
        this.timeToNextStation = timeToNextStation;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public void addLoad(int newLoad) {
        this.load = load + newLoad;
    }

    public int getInitialLoad() {
        return initialLoad;
    }

    public void setInitialLoad(int initialLoad) {
        this.initialLoad = initialLoad;
    }

    public int getNextStationInitial() {
        return nextStationInitial;
    }

    public void setNextStationInitial(int nextStationInitial) {
        this.nextStationInitial = nextStationInitial;
    }

    public double getTimeToNextStationInitial() {
        return timeToNextStationInitial;
    }

    public void setTimeToNextStationInitial(double timeToNextStationInitial) {
        this.timeToNextStationInitial = timeToNextStationInitial;
    }


}
