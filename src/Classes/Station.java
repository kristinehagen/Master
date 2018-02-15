package Classes;

import java.util.HashMap;

public class Station {

    private int id;
    private HashMap<Double, Double> bikeReturnedMedian;
    private HashMap<Double, Double> bikeReturnedStd;
    private HashMap<Double, Double> bikeWantedMedian;
    private HashMap<Double, Double> bikeWantedStd;
    private HashMap<Double, Double> optimalState;
    private String locationName;
    private double currentLoad;
    private double initialLoad;
    private int capacity;
    private HashMap<Integer, Double> drivingTime;
    private double latitude;
    private double longitude;


    public Station(int id, int numberOfBikes) {
        this.id = id;
        this.currentLoad = numberOfBikes;
        this.bikeReturnedMedian = new HashMap<>();
        this.bikeReturnedStd = new HashMap<>();
        this.bikeWantedMedian = new HashMap<>();
        this.bikeWantedStd = new HashMap<>();
        this.drivingTime = new HashMap<>();
        this.optimalState = new HashMap<>();
        this.initialLoad = numberOfBikes;
    }

    //Number of bikes
    public double getLoad() {
        return currentLoad;
    }

    public void setLoad(double load) {
        this.currentLoad = load;
    }

    public void addBikeToStation(double bikes) {
        this.currentLoad = currentLoad + bikes;
    }

    //Id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    //BikeReturnedMedian
    public double getBikeReturnedMedian(double hour) {
        return bikeReturnedMedian.get(hour);
    }

    public void setBikeReturnedMedian(double hour, double bikeReturnedStd) {
        this.bikeReturnedMedian.put(hour, bikeReturnedStd);
    }

    //BikeReturnedStd
    public double getBikeReturnedStd(double hour) {
        return bikeReturnedStd.get(hour);
    }

    public void setBikeReturnedStd(double hour, double bikeReturnedStd) {
        this.bikeReturnedStd.put(hour, bikeReturnedStd);
    }

    //BikeWantedMedian
    public double getBikeWantedMedian(double hour) {
        return bikeWantedMedian.get(hour);
    }

    public void setBikeWantedMedian(double hour, double bikeWantedMedian) {
        this.bikeWantedMedian.put(hour, bikeWantedMedian);
    }

    //BikeWantedStd
    public double getBikeWantedStd(double hour) {
        return bikeWantedStd.get(hour);
    }

    public void setBikeWantedStd(double hour, double bikeReturnedStd) {
        this.bikeWantedStd.put(hour, bikeReturnedStd);
    }

    //OptimalState
    public double getOptimalState(double hour) {
        return optimalState.get(hour);
    }

    public void setOptimalState(double hour, double optimalState) {
        this.optimalState.put(hour, optimalState);
    }

    //DrivingTime
    public double getDrivingTimeToStation(int stationId) {
        return drivingTime.get(stationId);
    }

    public void addDistanceToStationHashmap(int stationid, double drivingTime) {
        this.drivingTime.put(stationid, drivingTime);
    }

    //Nr of slots
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    //Longitude
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    //Latitude
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double altitude) {
        this.latitude = altitude;
    }

    public String getPlace() {
        return locationName;
    }

    public void setPlace(String locationName) {
        this.locationName = locationName;
    }

    public double getInitialLoad() {
        return initialLoad;
    }

    public void setInitialLoad(double initialLoad) {
        this.initialLoad = initialLoad;
    }

    //Print
    public String toString(){

        return "BikdeId: "+ id;

    };
}
