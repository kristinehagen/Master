package classes;

import org.junit.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class VehicleTest {

    @org.junit.jupiter.api.Test
    void createCluster() {
    }


    @org.junit.jupiter.api.Test
    void createRoutes() throws IOException {

        Vehicle vehicle = new Vehicle(1);

        //CalculateTimeToViolationIfNoVisit

        Input input = new Input();
        input.setCurrentMinute(8);
        ArrayList<StationVisit> routeUnderConstruction = new ArrayList<>();
        StationVisit stationVisit = new StationVisit();

        //Station already added
        Station stationAlreadyInRoute = new Station(1);
        stationVisit.setStation(stationAlreadyInRoute);

        //Station to check
        Station stationToCheck = new Station(2);
        stationToCheck.setBikeReturnedMedian(8, 40);
        stationToCheck.setBikeWantedMedian(8, 20);
        stationToCheck.setCapacity(20);
        stationToCheck.setInitialLoad(15);

        routeUnderConstruction.add(stationVisit);

        double actual = vehicle.calculateTimeToViolationIfNoVisit(routeUnderConstruction, stationToCheck, input);
        System.out.println(actual);
        double expected = 15;

        Assert.assertEquals(expected, actual, 0.00);
    }

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

}
