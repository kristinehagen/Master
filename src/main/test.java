package main;

import classes.Input;
import classes.Station;
import com.dashoptimization.XPRMCompileException;

import java.io.IOException;

public class test {

    public static void main(String[] args) throws IOException {

        double numberOfCusomersDemandingBikesInOneDay = 0;

        Input input = new Input();

        for (Station station : input.getStations().values()) {
            for (double hour = 17; hour <= 17; hour ++) {
                numberOfCusomersDemandingBikesInOneDay += (int)(station.getBikeWantedMedian(hour));
                numberOfCusomersDemandingBikesInOneDay += (int)(station.getBikeReturnedMedian(hour));
            }
        }

        System.out.println("Total number of customers: " + numberOfCusomersDemandingBikesInOneDay);

    }

}
