package main;

import classes.Input;
import classes.Station;
import classes.Vehicle;
import com.dashoptimization.XPRMCompileException;

import java.io.IOException;

public class test {

    public static void main(String[] args) throws IOException {


        Input input = new Input();

        double thisHour;

        for (int time = 7; time <= 20; time += 1) {

            thisHour = 0;

            for (Station station : input.getStations().values()) {
                thisHour += Math.abs(station.getBikeWantedMedian(time));
            }

            System.out.println("Total no. of customers at " + time + "-" + (time+1) +": " + thisHour);

        }

    }

}
