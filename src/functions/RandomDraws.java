package functions;

import java.util.Random;


public class RandomDraws {

    public static double drawNormal(double mean, double standardDeviation) {
        Random r = new Random();
        double drawn = (r.nextGaussian() * standardDeviation) + mean;
        if (drawn < 0 ) {
            return 0;
        } else if (drawn > mean*2) {
            return mean*2;
        } else {
            return drawn;
        }
    }

    //Draw a random arrival VisitTime between startTime and startTime+1
    public static double drawArrivalTimes (double startTime){
        return TimeConverter.convertHourtoSeconds(Math.random()+startTime);
    }

}