package classes;


/*
import enums.RouteLength;
import org.junit.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


class VehicleTest {

    @org.junit.jupiter.api.Test
    void findStationWithHighestScore() {
        HashMap<Integer, Double> stationScores = new HashMap<>();
        stationScores.put(3, 21.0);
        stationScores.put(5, 30.0);
        stationScores.put(11, -2.4);
        stationScores.put(6, 0.0);
        stationScores.put(1, 25.0);

        int expected = 5;
        int actual = (Collections.max(stationScores.entrySet(), Map.Entry.comparingByValue()).getKey());

        Assert.assertEquals(expected, actual);

    }

    @org.junit.jupiter.api.Test
    void getId() {
        int id = 3;
        Vehicle vehicle = new Vehicle(id);
        int actual = vehicle.getId();
        Assert.assertEquals(id, actual);
    }

    //Sjekker regretfunksjonen. Må gjøres public for testing

    /*
    @org.junit.jupiter.api.Test
    void checkIfTimeLimitIsReachedTest() throws IOException {

        Input input = new Input();
        input.setTimeHorizon(20);
        input.setCurrentMinute(8*60);

        StationVisit stationVisit1 = new StationVisit();
        stationVisit1.setStation(new Station(1, 0));
        stationVisit1.getStation().setBikeReturnedMedian(8, 0);
        stationVisit1.getStation().setBikeWantedMedian(8, 10);
        stationVisit1.getStation().setCapacity(10);
        stationVisit1.getStation().setLoad(0);
        stationVisit1.getStation().addDistanceToStationHashmap(2, 4.0);

        StationVisit stationVisit2 = new StationVisit();
        stationVisit2.setStation(new Station(2, 0));
        stationVisit2.getStation().setBikeReturnedMedian(8, 0);
        stationVisit2.getStation().setBikeWantedMedian(8, 10);
        stationVisit2.getStation().setCapacity(3);
        stationVisit2.getStation().setLoad(0);


        ArrayList<StationVisit> routeUnderConstruction = new ArrayList<>();
        routeUnderConstruction.add(stationVisit1);
        routeUnderConstruction.add(stationVisit2);

        Vehicle vehicle = new Vehicle(1);
        vehicle.setLoad(10);
        vehicle.setCapacity(10);

        ArrayList<Double> expected = new ArrayList<>();
        expected.add(7.0);
        expected.add(3.0);

        ArrayList<Double> actual = new ArrayList<>();

        RouteLength routeLength = vehicle.checkIfTimeLimitIsReached(routeUnderConstruction, input);

        actual.add(routeUnderConstruction.get(0).getLoadingQuantity());
        actual.add(routeUnderConstruction.get(1).getLoadingQuantity());




        //TEST 2 - not enough capacity

        stationVisit1.getStation().setCapacity(5);

        routeUnderConstruction = new ArrayList<>();
        routeUnderConstruction.add(stationVisit1);
        routeUnderConstruction.add(stationVisit2);

        expected.add(5.0);
        expected.add(3.0);

        routeLength = vehicle.checkIfTimeLimitIsReached(routeUnderConstruction, input);

        actual.add(routeUnderConstruction.get(0).getLoadingQuantity());
        actual.add(routeUnderConstruction.get(1).getLoadingQuantity());


        Assert.assertEquals(expected, actual);


    }



}
    */