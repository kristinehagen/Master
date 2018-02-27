package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
        ArrayList<ArrayList<StationVisit>> routesUnderConstruction = new ArrayList<>();
        double hour = input.getCurrentHour();


        //FIRST STATION

        //Lager en rute som kun går til første stasjon.
        //Legger så denne ruta inn i routesUnderConstruction slik at den kan utvikle seg
        Station firstStation = input.getStations().get(this.nextStationInitial);

        ArrayList<StationVisit> firstRouteUnderConstruction = new ArrayList<>();
        StationVisit firstStationVisit = new StationVisit();
        firstStationVisit.setStation(firstStation);
        firstRouteUnderConstruction.add(firstStationVisit);
        routesUnderConstruction.add(firstRouteUnderConstruction);


        //Deler alle stasjoner inn i positive og negative stasjoner
        ArrayList<Station> possibleStationsForNextStationVisit = this.clusterStationList;
        ArrayList<Station> positiveStations = filterStations(possibleStationsForNextStationVisit, true, hour);
        ArrayList<Station> negativeStations = filterStations(possibleStationsForNextStationVisit, false, hour);


        //SECOND STATION VISIT

        if (initialLoad <= input.getMinLoad() && firstStation.getNetDemand(hour) <= 0) {

            //Can only visit station with positive demand
            ArrayList<ArrayList<StationVisit>> newRoutes = chooseStations(positiveStations, firstRouteUnderConstruction, input);
            if (newRoutes != null) {
                routesUnderConstruction.addAll(newRoutes);
            }

        } else if(initialLoad >= input.getMaxLoad() && firstStation.getNetDemand(hour) >= 0) {

            //Can only visit station with negative demand
            ArrayList<ArrayList<StationVisit>> newRoutes = chooseStations(negativeStations, firstRouteUnderConstruction, input);
            if (newRoutes != null) {
                routesUnderConstruction.addAll(newRoutes);
            }

        } else {

            //Visits stations with both negative and positive demand
            ArrayList<ArrayList<StationVisit>> newRoutesPositive = chooseStations(positiveStations, firstRouteUnderConstruction, input);
            if (newRoutesPositive != null) {
                routesUnderConstruction.addAll(newRoutesPositive);
            }
            ArrayList<ArrayList<StationVisit>> newRoutesNegative = chooseStations(negativeStations, firstRouteUnderConstruction, input);
            if (newRoutesNegative != null) {
                routesUnderConstruction.addAll(newRoutesNegative);
            }

        }

        //Removes the route containing only the first station visit
        routesUnderConstruction.remove(firstRouteUnderConstruction);


        //STATION VISIT AFTER SECOND STATION
        while (routesUnderConstruction.size()>0) {

            //Expand all routes under construction if necessary
            for (ArrayList<StationVisit> routeUnderConstruction : routesUnderConstruction) {

                //Check if route needs to be expanded
                boolean routeNeedsToBeExpanded = checkIfTimeLimitIsReached(routeUnderConstruction);

                if (routeNeedsToBeExpanded) {

                    //Expand route

                    int numberOfStationsInRoute = routeUnderConstruction.size();

                    boolean lastStationIsPositive = routeUnderConstruction.get(numberOfStationsInRoute-1).getStation().getNetDemand(hour) >= 0;
                    boolean secondLastStationIsPositive = routeUnderConstruction.get(numberOfStationsInRoute-2).getStation().getNetDemand(hour) >= 0;

                    //Last two stations have positive demand
                    if (lastStationIsPositive & secondLastStationIsPositive) {
                        //Can only visit station with negative demand
                        ArrayList<ArrayList<StationVisit>> newRoutes = chooseStations(negativeStations, firstRouteUnderConstruction, input);
                        if (newRoutes != null) {
                            routesUnderConstruction.addAll(newRoutes);
                        }

                    //Last two stations have negative demand
                    } else if (!lastStationIsPositive & !secondLastStationIsPositive) {

                        //Can only visit station with positive demand
                        ArrayList<ArrayList<StationVisit>> newRoutes = chooseStations(positiveStations, firstRouteUnderConstruction, input);
                        if (newRoutes != null) {
                            routesUnderConstruction.addAll(newRoutes);
                        }

                    } else {

                        //Visits stations with both negative and positive demand
                        ArrayList<ArrayList<StationVisit>> newRoutesPositive = chooseStations(positiveStations, firstRouteUnderConstruction, input);
                        if (newRoutesPositive != null) {
                            routesUnderConstruction.addAll(newRoutesPositive);
                        }
                        ArrayList<ArrayList<StationVisit>> newRoutesNegative = chooseStations(negativeStations, firstRouteUnderConstruction, input);
                        if (newRoutesNegative != null) {
                            routesUnderConstruction.addAll(newRoutesNegative);
                        }

                    }

                }


                routesUnderConstruction.remove(routeUnderConstruction);
            }

        }

    }

    //IKKE FERDIG!!
    private boolean checkIfTimeLimitIsReached(ArrayList<StationVisit> routeUnderConstruction) {
        return false;
    }

    private ArrayList<ArrayList<StationVisit>> chooseStations(ArrayList<Station> stationList, ArrayList<StationVisit> routeUnderConstruction, Input input) {
        if (stationList.size() > 0) {

            //This hashmap saves station ID as key, and its respective score as value.
            HashMap<Integer, Double> stationScores = calculateScore(stationList, routeUnderConstruction, input);

            //This ArrayList contains new routes created
            ArrayList<ArrayList<StationVisit>> newRoutes = new ArrayList<>();

            //Branching and expands the input route to nrStationBranching new routes
            for (int branchingCount = 0; branchingCount < input.getNrStationBranching(); branchingCount++) {

                if (stationScores.size() > 0) {

                    Station stationWithHigestScore = stationList.get(findStationWithHighestScore(stationScores));
                    stationScores.remove(stationWithHigestScore.getId());

                    ArrayList<StationVisit> newRoute= new ArrayList<>(routeUnderConstruction);
                    StationVisit newStationVisit = new StationVisit();
                    newStationVisit.setStation(stationWithHigestScore);
                    newRoute.add(newStationVisit);

                    newRoutes.add(newRoute);
                }
            }

            return newRoutes;

        } else {
            return null;
        }
    }

    //FERDIG
    private int findStationWithHighestScore(HashMap<Integer, Double> stationScores) {
        return (Collections.max(stationScores.entrySet(), Map.Entry.comparingByValue()).getKey());
    }

    //FERDIG
    private HashMap<Integer,Double> calculateScore(ArrayList<Station> stationList, ArrayList<StationVisit> firstRouteUnderConstruction, Input input) {
        HashMap<Integer, Double> stationScores = new HashMap<>();
        for (Station station: stationList) {
            double timeLastVisit = 0;

            double timeToViolation = calculateTimeToViolationIfNoVisit(firstRouteUnderConstruction, station, input);
            double diffOptimalState = calculateDiffFromOptimalStateIfNoVisit(firstRouteUnderConstruction, station, input);
            double violationRate = station.getNetDemand(input.getCurrentHour());
            double drivingTime = firstRouteUnderConstruction.get(0).getStation().getDrivingTimeToStation(station.getId());

            //Calculate total score
            double score = input.getWeightTimeToViolation()*timeToViolation
                    + input.getWeightOptimalState()*diffOptimalState
                    + input.getWeightViolationRate() * violationRate
                    + input.getWeightDrivingTime()*drivingTime;

            stationScores.put(station.getId(), score);
        }
        return stationScores;
    }

    private double calculateDiffFromOptimalStateIfNoVisit(ArrayList<StationVisit> stationVisits, Station stationToCheck, Input input) {
        double diffFromOptimalState = 0.0;
        double loadAtTimeHorizon;
        double lastVisitTime;
        double updatedLoad;
        double hour = input.getCurrentHour();
        double timeHorizon = input.getTimeHorizon();

        for (StationVisit stationVisit: stationVisits) {
            if (stationVisit.getStation().getId() == stationToCheck.getId()) {
                lastVisitTime = stationVisit.getVisitTime();
                updatedLoad = stationVisit.getLoadAfterVisit();
                loadAtTimeHorizon = stationToCheck.getNetDemand(hour)*(timeHorizon-lastVisitTime) + updatedLoad;
                if (loadAtTimeHorizon > stationToCheck.getCapacity()){
                    loadAtTimeHorizon = stationToCheck.getCapacity();
                } else if (loadAtTimeHorizon < 0) {
                    loadAtTimeHorizon = 0;
                }
                diffFromOptimalState = java.lang.Math.abs(stationToCheck.getOptimalState(hour)-loadAtTimeHorizon);
            } else {
                loadAtTimeHorizon = stationToCheck.getNetDemand(hour)*timeHorizon + stationToCheck.getInitialLoad();
                diffFromOptimalState = java.lang.Math.abs(stationToCheck.getOptimalState(hour)-loadAtTimeHorizon);
            }
        }
        return diffFromOptimalState;
    }

    private double calculateTimeToViolationIfNoVisit(ArrayList<StationVisit> stationVisits, Station stationToCheck, Input input) {
        double lastVisitTime = 0.0;
        double timeToViolation = 0.0;
        double updatedLoad;
        double hour = input.getCurrentHour();

        for (StationVisit stationVisit: stationVisits) {
            if (stationVisit.getStation().getId() == stationToCheck.getId()) {
                lastVisitTime = stationVisit.getVisitTime();
                updatedLoad = stationVisit.getLoadAfterVisit();
                if (stationToCheck.getNetDemand(hour) > 0) {
                    timeToViolation = (stationToCheck.getCapacity() - updatedLoad) / stationToCheck.getNetDemand(hour);
                } else {
                    timeToViolation = updatedLoad / stationToCheck.getNetDemand(hour);
                }
            } else {
                if (stationToCheck.getNetDemand(hour) > 0) {
                    timeToViolation = (stationToCheck.getCapacity() - stationToCheck.getLoad()) / stationToCheck.getNetDemand(hour);
                } else {
                    timeToViolation = stationToCheck.getLoad() / stationToCheck.getNetDemand(hour);
                }
            }
        }
        return lastVisitTime + timeToViolation;
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
