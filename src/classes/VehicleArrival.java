package classes;

public class VehicleArrival {


    private int stationId;
    private int stationVisitNr;
    private int load;
    private double time;
    private int vehicle;
    private int nextStationId;
    private int nextStationVisit;
    private double timeNextVisit;
    private boolean firstVisit;


    public VehicleArrival(int stationId, int stationVisitNr, int stationLoad, double time, int vehicle, int nextStationId, int nextStationVisit, double timeNextVisit, boolean firstVisit) {
        this.stationId = stationId;
        this.stationVisitNr = stationVisitNr;
        this.load = stationLoad;
        this.time = time;
        this.vehicle = vehicle;
        this.nextStationId = nextStationId;
        this.nextStationVisit = nextStationVisit;
        this.timeNextVisit = timeNextVisit;
        this.firstVisit = firstVisit;
    }

    //GETTERS AND SETTERS
    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getStationVisit() {
        return stationVisitNr;
    }

    public void setStationVisit(int stationVisitNr) {
        this.stationVisitNr = stationVisitNr;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getVehicle() {
        return vehicle;
    }

    public void setVehicle(int vehicle) {
        this.vehicle = vehicle;
    }

    public int getNextStationId() {
        return nextStationId;
    }

    public void setNextStationId(int nextStationId) {
        this.nextStationId = nextStationId;
    }

    public int getNextStationVisit() {
        return nextStationVisit;
    }

    public void setNextStationVisit(int nextStationVisit) {
        this.nextStationVisit = nextStationVisit;
    }

    public double getTimeNextVisit() {
        return timeNextVisit;
    }

    public void setTimeNextVisit(double timeNextVisit) {
        this.timeNextVisit = timeNextVisit;
    }

    public boolean isFirstvisit() {
        return firstVisit;
    }

    public void setFirstvisit(boolean firstvisit) {
        this.firstVisit = firstvisit;
    }

}