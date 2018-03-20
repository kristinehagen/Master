package classes;

import functions.TimeConverter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    private ArrayList<ArrayList<StationVisit>> initializedRoutes = new ArrayList<>();

    public Vehicle(int id) {
        this.id = id;
    }

    //MIDLERTIDIG - Skal returnere stasjonenen som denne bilen har lov til å besøke
    private void createCluster(Input input){
        HashMap<Integer, Station> stations = input.getStations();
        ArrayList<Station> stationsList = new ArrayList<>();
        for (Station station : stations.values()) {
            stationsList.add(station);
        }
        this.clusterStationList = stationsList;
    }

    //Rammeverket for initialisering av ruter
    public void createRoutes(Input input) {
        createCluster(input);

        //En liste med alle ruter som ikke er ferdig laget enda
        ArrayList<ArrayList<StationVisit>> routesUnderConstruction = new ArrayList<>();


        //FIRST STATION

        //Lager en rute som kun går til første stasjon.
        //Legger så denne ruta inn i routesUnderConstruction slik at den kan utvikle seg
        Station firstStation = input.getStations().get(this.nextStationInitial);

        ArrayList<StationVisit> firstRouteUnderConstruction = new ArrayList<>();
        StationVisit firstStationVisit = new StationVisit();
        firstStationVisit.setStation(firstStation);
        firstStationVisit.setVisitTime(this.timeToNextStationInitial);
        firstRouteUnderConstruction.add(firstStationVisit);
        routesUnderConstruction.add(firstRouteUnderConstruction);


        //Deler alle stasjoner inn i positive og negative stasjoner
        ArrayList<Station> possibleStationsForNextStationVisit = this.clusterStationList;



        //SECOND STATION VISIT

        //Check if route needs to be expanded
        boolean routeNeedsToBeExpanded = checkIfTimeLimitIsReached(firstRouteUnderConstruction, input);

        if (routeNeedsToBeExpanded) {

            ArrayList<ArrayList<StationVisit>> newRoutes = performBranchingSecondStation(firstRouteUnderConstruction, possibleStationsForNextStationVisit, input);
            routesUnderConstruction.addAll(newRoutes);
        } else {

            //Route is completed
            this.initializedRoutes.add(firstRouteUnderConstruction);
        }

        routesUnderConstruction.remove(firstRouteUnderConstruction);

        //Removes the route containing only the first station visit
        routesUnderConstruction.remove(firstRouteUnderConstruction);




        //STATION VISIT AFTER SECOND STATION
        while (routesUnderConstruction.size() > 0) {

            ArrayList<ArrayList<StationVisit>> newRoutesUnderConstruction = new ArrayList<>();

            //Expand all routes under construction if necessary
            for (ArrayList<StationVisit> routeUnderConstruction : routesUnderConstruction) {

                //Check if route needs to be expanded
                routeNeedsToBeExpanded = checkIfTimeLimitIsReached(routeUnderConstruction, input);

                if (routeNeedsToBeExpanded) {

                    ArrayList<ArrayList<StationVisit>> newRoutes = performBranching(routeUnderConstruction, possibleStationsForNextStationVisit, input);
                    newRoutesUnderConstruction.addAll(newRoutes);
                } else {

                    //Route is completed
                    this.initializedRoutes.add(routeUnderConstruction);

                }

            }

            routesUnderConstruction = new ArrayList<>(newRoutesUnderConstruction);

        }

    }

    //Performs the branching on second stations visit. Finds the restrictions enforced on next branching, then asks chooseStation to actually create new routes
    private ArrayList<ArrayList<StationVisit>> performBranchingSecondStation(ArrayList<StationVisit> firstRouteUnderConstruction, ArrayList<Station> possibleStationsForNextStationVisit, Input input) {

        ArrayList<Station> negativeStations = filterStations(possibleStationsForNextStationVisit, false, input.getCurrentMinute());
        ArrayList<Station> positiveStations = filterStations(possibleStationsForNextStationVisit, true, input.getCurrentMinute());
        ArrayList<ArrayList<StationVisit>> newRoutes = new ArrayList<>();

        boolean firstStationHasPositiveDemand = firstRouteUnderConstruction.get(0).getStation().getNetDemand(TimeConverter.convertMinutesToHourRounded(input.getCurrentMinute())) > 0;

        if (load <= input.getMinLoad() && !firstStationHasPositiveDemand) {

            //Assumes vehicle load = 0
            //Can only visit station with positive demand
            newRoutes.addAll(chooseStations(positiveStations, firstRouteUnderConstruction, input));

        } else if (load >= input.getMaxLoad() && firstStationHasPositiveDemand) {

            //Assumes vehicle load = full
            //Can only visit station with negative demand
            newRoutes.addAll(chooseStations(negativeStations, firstRouteUnderConstruction, input));

        } else {

            //Visits stations with both negative and positive demand
            newRoutes.addAll(chooseStations(positiveStations, firstRouteUnderConstruction, input));
            newRoutes.addAll(chooseStations(negativeStations, firstRouteUnderConstruction, input));

        }

        return newRoutes;
    }

    //Performs the branching on all stations visits after second station visit. Finds the restrictions enforced on next branching, then asks chooseStation to actually create new routes
    private ArrayList<ArrayList<StationVisit>> performBranching(ArrayList<StationVisit> routeUnderConstruction, ArrayList<Station> possibleStationsForNextStationVisit, Input input) {

        ArrayList<Station> negativeStations = filterStations(possibleStationsForNextStationVisit, false, input.getCurrentMinute());
        ArrayList<Station> positiveStations = filterStations(possibleStationsForNextStationVisit, true, input.getCurrentMinute());

        int numberOfStationsInRoute = routeUnderConstruction.size();

        double currentHourRounded = TimeConverter.convertMinutesToHourRounded(input.getCurrentMinute());
        boolean lastStationIsPositive = routeUnderConstruction.get(numberOfStationsInRoute-1).getStation().getNetDemand(currentHourRounded) >= 0;
        boolean secondLastStationIsPositive = routeUnderConstruction.get(numberOfStationsInRoute-2).getStation().getNetDemand(currentHourRounded) >= 0;
        ArrayList<ArrayList<StationVisit>> newRoutes = new ArrayList<>();

        //Last two stations have positive demand
        if (lastStationIsPositive & secondLastStationIsPositive) {
            //Can only visit station with negative demand
            newRoutes.addAll(chooseStations(negativeStations, routeUnderConstruction, input));

        //Last two stations have negative demand
        } else if (!lastStationIsPositive & !secondLastStationIsPositive) {

            //Can only visit station with positive demand
            newRoutes.addAll(chooseStations(positiveStations, routeUnderConstruction, input));

        } else {

            //Visits stations with both negative and positive demand
            newRoutes.addAll(chooseStations(positiveStations, routeUnderConstruction, input));
            newRoutes.addAll(chooseStations(negativeStations, routeUnderConstruction, input));

        }
        return newRoutes;

    }

    //Checks if time limit is reaches. Determines time and load to calculate total time
    private boolean checkIfTimeLimitIsReached(ArrayList<StationVisit> routeUnderConstruction, Input input) {

        double timeHorizon = input.getTimeHorizon();
        double currentMinute = input.getCurrentMinute();
        double currentHourRounded = TimeConverter.convertMinutesToHourRounded(currentMinute);
        int vehicleLoad = load;

        int numberOfStationVisitsInRoute = routeUnderConstruction.size();

        for (int i = 0; i < numberOfStationVisitsInRoute; i++) {



            //------Set time for stationVisit---------

                //first station visit
            if (i == 0) {
                routeUnderConstruction.get(0).setVisitTime(timeToNextStation);
            } else {

                //remaining station visits
                double visitTimeLastStation = routeUnderConstruction.get(i-1).getVisitTime();
                double absoluteLoadAtLastStation = Math.abs(routeUnderConstruction.get(i-1).getLoadingQuantity());
                double drivingTimeFromLastStation = routeUnderConstruction.get(i-1).getStation().getDrivingTimeToStation(routeUnderConstruction.get(i).getStation().getId());
                double visitTime = visitTimeLastStation+absoluteLoadAtLastStation*input.getVehicleHandlingTime()+input.getVehicleParkingTime()+ drivingTimeFromLastStation;

                routeUnderConstruction.get(i).setVisitTime(visitTime);

            }




            //-------Find current load at station i, right before visit----------


            double lastVisitTime = 0;
            double stationLoadAfterLastVisit = routeUnderConstruction.get(i).getStation().getLoad();

            //Check if station is already visited in this route
            for (int j = 0; j<i ; j++) {
                if (routeUnderConstruction.get(j).getStation().getId() == routeUnderConstruction.get(i).getStation().getId()) {
                    lastVisitTime = routeUnderConstruction.get(j).getVisitTime();
                    stationLoadAfterLastVisit = routeUnderConstruction.get(j).getLoadAfterVisit();
                }
            }

            double netDemandPerMinute = routeUnderConstruction.get(i).getStation().getNetDemand(currentHourRounded)/60;
            double customersArrivingSinceLastVisit = netDemandPerMinute*(routeUnderConstruction.get(i).getVisitTime()-lastVisitTime);
            double loadRightBeforeVisit = stationLoadAfterLastVisit + customersArrivingSinceLastVisit;

            if (loadRightBeforeVisit > routeUnderConstruction.get(i).getStation().getCapacity()) {
                loadRightBeforeVisit = routeUnderConstruction.get(i).getStation().getCapacity();
            } else if (loadRightBeforeVisit < 0 ) {
                loadRightBeforeVisit = 0;
            }





            //---------Set load------------
            boolean currentStationisPickUpStation = routeUnderConstruction.get(i).getStation().getNetDemand(currentHourRounded) >= 0;
            double maxLoad;
            double actualLoad;

            //If current station is not the last station in the route
            if (i < numberOfStationVisitsInRoute - 1) {

                boolean nextStationisPickUpStation = routeUnderConstruction.get(i + 1).getStation().getNetDemand(currentHourRounded) >= 0;

                //If both pick up stations, load at this station maximum -capacity/2
                if (currentStationisPickUpStation && nextStationisPickUpStation) {
                    maxLoad = Math.round((capacity-vehicleLoad)/2);
                    maxLoad = loadRestrictedByBikesAtStation(maxLoad, loadRightBeforeVisit);
                    maxLoad = loadRestrictedByFreeSpacesInVehicle(maxLoad, vehicleLoad, capacity);

                    actualLoad = -Math.floor(maxLoad);
                    routeUnderConstruction.get(i).setLoadingQuantity(actualLoad);
                    vehicleLoad -= actualLoad;
                }

                //If both delivery stations, load at this station maximum capacity/2
                else if (!currentStationisPickUpStation && !nextStationisPickUpStation) {
                    maxLoad = Math.round(vehicleLoad/2);
                    maxLoad = loadRestrictedByFreeLocksAtStation(routeUnderConstruction.get(i).getStation().getCapacity(), loadRightBeforeVisit, maxLoad);
                    maxLoad = loadRestrictedByBikesInVehicle(vehicleLoad, maxLoad);

                    actualLoad = Math.floor(maxLoad);
                    routeUnderConstruction.get(i).setLoadingQuantity(actualLoad);
                    vehicleLoad -= actualLoad;
                }

                //if next station pick up station and current station delivery station, load maximum capacity
                else if (!currentStationisPickUpStation & nextStationisPickUpStation) {
                    maxLoad = capacity;
                    maxLoad = loadRestrictedByFreeLocksAtStation(routeUnderConstruction.get(i).getStation().getCapacity(), loadRightBeforeVisit, maxLoad);
                    maxLoad = loadRestrictedByBikesInVehicle(vehicleLoad, maxLoad);

                    actualLoad = Math.floor(maxLoad);
                    routeUnderConstruction.get(i).setLoadingQuantity(actualLoad);
                    vehicleLoad -= actualLoad;
                }

                //if next station delivery station and current station pick up station, load maximum -capacity
                else {
                    maxLoad = capacity;
                    maxLoad = loadRestrictedByBikesAtStation(maxLoad, loadRightBeforeVisit);
                    maxLoad = loadRestrictedByFreeSpacesInVehicle(maxLoad, vehicleLoad, capacity);

                    actualLoad = -Math.floor(maxLoad);
                    routeUnderConstruction.get(i).setLoadingQuantity(actualLoad);
                    vehicleLoad -= actualLoad;
                }

            } else {
                //If current station is last station in route

                //Current station is pick up station
                if (currentStationisPickUpStation) {
                    maxLoad = capacity;
                    maxLoad = loadRestrictedByBikesAtStation(maxLoad, loadRightBeforeVisit);
                    maxLoad = loadRestrictedByFreeSpacesInVehicle(maxLoad, vehicleLoad, capacity);

                    actualLoad = -Math.floor(maxLoad);
                    routeUnderConstruction.get(i).setLoadingQuantity(actualLoad);
                    vehicleLoad -= actualLoad;
                }

                //Current station is delivery station
                else {
                    maxLoad = capacity;
                    maxLoad = loadRestrictedByFreeLocksAtStation(routeUnderConstruction.get(i).getStation().getCapacity(), loadRightBeforeVisit, maxLoad);
                    maxLoad = loadRestrictedByBikesInVehicle(vehicleLoad, maxLoad);

                    actualLoad = Math.floor(maxLoad);
                    routeUnderConstruction.get(i).setLoadingQuantity(actualLoad);
                    vehicleLoad -= actualLoad;
                }
            }



            //------Set updated load at station-----------

            routeUnderConstruction.get(i).setLoadAfterVisit(loadRightBeforeVisit+actualLoad);

        }




        //Return true if more stations should be added to the route.

        return (routeUnderConstruction.get(numberOfStationVisitsInRoute-1).getVisitTime() < timeHorizon);

    }

    private double loadRestrictedByBikesInVehicle(int vehicleLoad, double maxLoad) {
        if (vehicleLoad < maxLoad) {
            return vehicleLoad;
        } else {
            return maxLoad;
        }
    }

    private double loadRestrictedByFreeLocksAtStation(int capacity, double loadRightBeforeVisit, double maxLoad) {
        double freeLocksAtStation = capacity-loadRightBeforeVisit;
        if (freeLocksAtStation < maxLoad) {
            return freeLocksAtStation;
        } else {
            return maxLoad;
        }
    }

    private double loadRestrictedByFreeSpacesInVehicle(double maxLoad, int vehicleLoad, int capacity) {
        double freeSpacesInVehicle = capacity-vehicleLoad;
        if (freeSpacesInVehicle < maxLoad) {
            return freeSpacesInVehicle;
        } else {
            return maxLoad;
        }
    }

    private double loadRestrictedByBikesAtStation(double maxLoad, double loadRightBeforeVisit) {
        if (loadRightBeforeVisit < maxLoad) {
            return loadRightBeforeVisit;
        } else {
            return maxLoad;
        }
    }



    //Gets routes under construction and possible stations to visit, and returns new routes
    private ArrayList<ArrayList<StationVisit>> chooseStations(ArrayList<Station> possibleStationsForNextStationVisit, ArrayList<StationVisit> routeUnderConstruction, Input input) {

        if (possibleStationsForNextStationVisit.size() > 0) {

            //Remove last visited station from possibleStationsToVisit
            Station lastStationVisited = routeUnderConstruction.get(routeUnderConstruction.size()-1).getStation();
            boolean lastStationVisitedIsInPossibleStationsForNextStationVisit = possibleStationsForNextStationVisit.contains(lastStationVisited);
            if (lastStationVisitedIsInPossibleStationsForNextStationVisit) {
                possibleStationsForNextStationVisit.remove(lastStationVisited);
            }

            //Do not create routes that are infesiable with respect to M
            for (StationVisit stationVisit1 : routeUnderConstruction) {
                int stationId = stationVisit1.getStation().getId();

                int numberOfTimesInRoute = 0;

                //Check how many times stationId is in route
                for (StationVisit stationVisit2 : routeUnderConstruction) {
                    if (stationVisit2.getStation().getId() == stationId) {
                        numberOfTimesInRoute ++;
                    }
                }

                if (numberOfTimesInRoute >= input.getMaxVisit()) {
                    possibleStationsForNextStationVisit.remove(stationVisit1);
                }

            }

            //This hashmap saves station ID as key, and its respective score as value.
            HashMap<Integer, Double> stationScores = calculateScore(possibleStationsForNextStationVisit, routeUnderConstruction, input);

            ArrayList<ArrayList<StationVisit>> newRoutes = new ArrayList<>();

            //Branching and expands the input route to nrStationBranching new routes
            for (int branchingCount = 0; branchingCount < input.getNrStationBranching(); branchingCount++) {

                if (stationScores.size() > 0) {

                    int idForStationWithHighestScore = findStationWithHighestScore(stationScores);
                    Station stationWithHigestScore = input.getStations().get(idForStationWithHighestScore);

                    ArrayList<StationVisit> newRoute= new ArrayList<>(routeUnderConstruction);

                    //Set all values in previous stations visits to 0
                    for (StationVisit stationVisit : routeUnderConstruction) {
                        stationVisit.setLoadingQuantity(0);
                        stationVisit.setVisitTime(0);
                        stationVisit.setLoadAfterVisit(0);
                    }

                    StationVisit newStationVisit = new StationVisit();
                    newStationVisit.setStation(stationWithHigestScore);
                    newRoute.add(newStationVisit);

                    newRoutes.add(newRoute);
                    stationScores.remove(stationWithHigestScore.getId());
                }
            }

            return newRoutes;

        } else {
            return null;
        }
    }

    //Returns station with highest score
    private int findStationWithHighestScore(HashMap<Integer, Double> stationScores) {
        return (Collections.max(stationScores.entrySet(), Map.Entry.comparingByValue()).getKey());
    }

    //Calculate score of a list of stations. Returns a hashmap with station id and score
    private HashMap<Integer,Double> calculateScore(ArrayList<Station> stationList, ArrayList<StationVisit> routeUnderConstruction, Input input) {
        HashMap<Integer, Double> stationScores = new HashMap<>();
        for (Station station: stationList) {

            //TESTED
            double timeToViolation = calculateTimeToViolationIfNoVisit(routeUnderConstruction, station, input);
            double diffOptimalState = calculateDiffFromOptimalStateIfNoVisit(routeUnderConstruction, station, input);
            double violationRate = station.getNetDemand(TimeConverter.convertMinutesToHourRounded(input.getCurrentMinute()))/60;                            //Each minute
            double drivingTime = routeUnderConstruction.get(routeUnderConstruction.size()-1).getStation().getDrivingTimeToStation(station.getId());         //In minutes

            //Calculate total score
            double score = input.getWeightTimeToViolation()*timeToViolation
                    + input.getWeightOptimalState()*diffOptimalState
                    + input.getWeightViolationRate() * violationRate
                    + input.getWeightDrivingTime()*drivingTime;

            stationScores.put(station.getId(), score);
        }
        return stationScores;
    }

    //Calculates difference between optimal level and inventory at horizon if no more visits. Returns the diff
    private double calculateDiffFromOptimalStateIfNoVisit(ArrayList<StationVisit> routeUnderConstruction, Station stationToCheck, Input input) {
        double loadAtTimeHorizon;
        double lastVisitTime = 0.0;
        double load = stationToCheck.getLoad();
        double currentMinute = input.getCurrentMinute();
        double timeHorizon = input.getTimeHorizon();
        double currentHourRounded = TimeConverter.convertMinutesToHourRounded(currentMinute);

        //Check if station has been visited before
        for (StationVisit stationVisit: routeUnderConstruction) {

            //If visited before, update load
            if (stationVisit.getStation().getId() == stationToCheck.getId()) {

                lastVisitTime = stationVisit.getVisitTime();
                load = stationVisit.getLoadAfterVisit();
            }
        }

        loadAtTimeHorizon = stationToCheck.getNetDemand(currentHourRounded)/60*(timeHorizon-lastVisitTime) + load;

        if (loadAtTimeHorizon > stationToCheck.getCapacity()){
            loadAtTimeHorizon = stationToCheck.getCapacity();
        } else if (loadAtTimeHorizon < 0) {
            loadAtTimeHorizon = 0;
        }

        double diffFromOptimalState = java.lang.Math.abs(stationToCheck.getOptimalState(TimeConverter.convertMinutesToHourRounded(currentMinute))-loadAtTimeHorizon);

        return diffFromOptimalState;
    }

    //Calculates time to violation if no more visits. Returns time. 0 if violation now
    public double calculateTimeToViolationIfNoVisit(ArrayList<StationVisit> routeUnderConstruction, Station stationToCheck, Input input) {
        double lastVisitTime = 0.0;
        double timeToViolation = 0.0;
        double load = stationToCheck.getLoad();
        double currentHourRouded = TimeConverter.convertMinutesToHourRounded(input.getCurrentMinute());

        //Check if station has been visited before
        for (StationVisit stationVisit: routeUnderConstruction) {

            if (stationVisit.getStation().getId() == stationToCheck.getId()) {

                lastVisitTime = stationVisit.getVisitTime();
                load = stationVisit.getLoadAfterVisit();
            }
        }

        if (stationToCheck.getNetDemand(currentHourRouded) > 0) {
            timeToViolation = (stationToCheck.getCapacity() - load) / (stationToCheck.getNetDemand(currentHourRouded)/60);
        } else if (stationToCheck.getNetDemand(currentHourRouded) < 0) {
            timeToViolation = -load / (stationToCheck.getNetDemand(currentHourRouded) / 60);
        }

        return lastVisitTime + timeToViolation;
    }

    //Returns a list with either positive or negative stations.
    private ArrayList<Station> filterStations(ArrayList<Station> stationList, boolean returnPositive, double currentMinute) {
        ArrayList<Station> stationListFiltered = new ArrayList<>();
        double currentHourRounded = TimeConverter.convertMinutesToHourRounded(currentMinute);
        for (Station station:stationList) {
            if (returnPositive & station.getNetDemand(currentHourRounded)>=0) {
                stationListFiltered.add(station);
            } else if (!returnPositive & station.getNetDemand(currentHourRounded)<=currentHourRounded) {
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


    public ArrayList<ArrayList<StationVisit>> getInitializedRoutes() {
        return initializedRoutes;
    }

    public void setInitializedRoutes(ArrayList<ArrayList<StationVisit>> initializedRoutes) {
        this.initializedRoutes = initializedRoutes;
    }
}
