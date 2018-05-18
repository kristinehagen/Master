
package main;


import classes.GraphViewer;
import classes.Input;
import classes.Station;
import functions.TimeConverter;

import java.io.IOException;

public class DrawStations {

    public static void main(String[] args) throws IOException {

        Input input = new Input();

        //Low : 0-mediumDemand, Medium: mediumDemand-highDemand, High: highDemand++
        double highDemand = input.getHighDemand();
        double mediumDemand = input.getMediumDemand();

        GraphViewer graph = new GraphViewer();
        graph.drawStationDemand(input, mediumDemand, highDemand);


        int nrOfHighCriticalStations = 0;
        int nrOfMediumCriticalStations = 0;
        int nrOfLowCriticalStations = 0;

        for (Station station : input.getStations().values()) {
            double netDemand = station.getNetDemand(TimeConverter.convertMinutesToHourRounded(input.getCurrentMinute()));
            if (station.getNumberOfClusters() == 2) {
                nrOfHighCriticalStations++;
            } else if (station.getNumberOfClusters() == 1) {
                nrOfMediumCriticalStations++;
            } else {
                nrOfLowCriticalStations++;
            }
        }

        System.out.println("Number of stations with high demand: " + nrOfHighCriticalStations);
        System.out.println("Number of stations with medium demand: " + nrOfMediumCriticalStations);
        System.out.println("Number of stations with low demand: " + nrOfLowCriticalStations);
    }
}

