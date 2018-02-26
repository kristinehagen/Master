package classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Vehicle {

    private int id;
    private int nextStation;
    private int nextStationInitial;
    private double timeToNextStation;
    private double timeToNextStationInitial;
    private int capacity;
    private int load;
    private int initialLoad;
    private ArrayList<Station> clusterStationList = new ArrayList<>();
    private ArrayList<ArrayList<Station>> initializedRoutes;

    public Vehicle(int id) {
        this.id = id;
    }

    //MIDLERTIDIG - Skal returnere stasjonenen som denne bilen har lov til å besøke
    public void createCluster(Input input){
        HashMap<Integer, Station> stations = input.getStations();
        ArrayList<Station> stationsList = new ArrayList<>();
        for (Station station : stations.values()) {
            stationsList.add(station);
        }
        this.clusterStationList = stationsList;
    }


    //Initialiserer ruter for bilen
    public void createRoutes(Input input) {
        createCluster(input);

        //En liste med alle ruter som ikke er ferdig laget enda
        ArrayList<ArrayList<Station>> routesUnderConstruction = new ArrayList<>();

        //Lager en rute som kun går til første stasjon.
        // Legger så denne ruta inn i routesUnderConstruction slik at den kan utvikle seg
        Station firstStation = input.getStations().get(this.nextStationInitial);
        ArrayList<Station> firstRouteUnderConstruction = new ArrayList<>();
        firstRouteUnderConstruction.add(firstStation);
        routesUnderConstruction.add(firstRouteUnderConstruction);

        double hour = input.getCurrentHour();

        //Deler alle stasjoner i clusteret inn i positive og negative stasjoner
        ArrayList<Station> possibleStationsForNextStationVisit = this.clusterStationList;
        ArrayList<Station> positiveStations = filterStations(possibleStationsForNextStationVisit, true, hour);
        ArrayList<Station> negativeStations = filterStations(possibleStationsForNextStationVisit, false, hour);

        //Second station visit
        if (initialLoad <= input.getMinLoad() && firstStation.getNetDemand(hour) <= 0) {
            //Kan kun besøke positive stasjoner
        } else if(initialLoad >= input.getMaxLoad() && firstStation.getNetDemand(hour) >= 0) {
            //Kan kun besøke negative stasjoner
        } else {
            //Besøker både positive og negative stasjoner
        }

    }

    private ArrayList<Station> filterStations(ArrayList<Station> stationList, boolean returnPositive, double hour) {
        ArrayList<Station> stationListFiltered = new ArrayList<>();
        for (Station station:stationList) {
            if (returnPositive & station.getNetDemand(hour)>=0) {
                stationListFiltered.add(station);
            } else if (!returnPositive & station.getNetDemand(hour)<=hour) {
                stationListFiltered.add(station);
            }
        }
        return stationListFiltered;
    }







    //Getter and setters

    public ArrayList<Station> getClusterStationList() {
        return clusterStationList;
    }

    public Station getClusterIdList(int index) {
        return clusterStationList.get(index);
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


    public ArrayList<ArrayList<Station>> getInitializedRoutes() {
        return initializedRoutes;
    }

    public void setInitializedRoutes(ArrayList<ArrayList<Station>> initializedRoutes) {
        this.initializedRoutes = initializedRoutes;
    }
}
