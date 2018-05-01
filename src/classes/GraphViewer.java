package classes;

import java.util.ArrayList;
import java.util.Random;

import functions.TimeConverter;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class GraphViewer {

    Graph graph;

    public GraphViewer() {
        this.graph = new MultiGraph("Graph 1");
    }

    public void drawStationDemand(Input input, double mediumDemand, double highDemand) {

        graph.clear();

        Edge edge;
        String color;

        //STATIONS
        for (Station station : input.getStations().values()) {
            Node node = graph.addNode("Station" + station.getId());
            node.addAttribute("y", station.getLatitude());
            node.addAttribute("x", station.getLongitude());
            node.addAttribute("layout.frozen");
            node.addAttribute("ui.label", station.getId());

            double netDemand = station.getNetDemand(TimeConverter.convertMinutesToHourRounded(input.getCurrentMinute()));
            //If demand over highDemand -> red, mediumDemand-HighDemand -> black, 0-mediumDemand grey
            if (netDemand >= highDemand || netDemand <= -highDemand) {
                node.addAttribute("ui.style", "fill-color: red;");
            } else if (netDemand >= mediumDemand || netDemand <= -mediumDemand){
                node.addAttribute("ui.style", "fill-color: black;");
            }else {
                node.addAttribute("ui.style", "fill-color: grey;");
            }



        }

        this.graph.display();

    }



    public void displayInitiatedRoutes(Input input, boolean initial) {
        this.graph.clear();
        this.drawInitiatedRoutes(this.graph, input);
        if (initial) {
            this.graph.display();
        }
    }

    private void drawInitiatedRoutes (Graph graph, Input input) {

        Edge edge;
        String color;

        //STATIONS
        for (Station station : input.getStations().values()) {
            Node node = graph.addNode("Station" + station.getId());
            node.addAttribute("y", station.getLatitude());
            node.addAttribute("x", station.getLongitude());
            node.addAttribute("layout.frozen");
            node.addAttribute("ui.label", station.getId());

            //Color (positive station = black, negative station = grey)
            if (station.getNetDemand(TimeConverter.convertMinutesToHourRounded(input.getCurrentMinute())) >= 0) {
                node.addAttribute("ui.style", "fill-color: black;");
            } else {
                node.addAttribute("ui.style", "fill-color: grey;");
            }
        }


        //EGDE
        for (Vehicle vehicle : input.getVehicles().values()) {
            graph.getNode("Station" + vehicle.getNextStation()).addAttribute("ui.style", "fill-color: red;");


            for (ArrayList<StationVisit> route: vehicle.getInitializedRoutes()) {

                color = getColor(99); //99 = random color

                for (int stationVisit = 0; stationVisit < route.size()-1; stationVisit++) {

                    if (route.size() <= 1) {
                        continue;
                    }

                    //From station
                    Station fromStationNode = route.get(stationVisit).getStation();
                    Node nodeFromStation = graph.getNode("Station" + fromStationNode.getId());

                    //To station
                    Station toStationNode = route.get(stationVisit + 1).getStation();
                    Node nodeToStation = graph.getNode("Station" + toStationNode.getId());

                    //Edge ID
                    String edgeId = "V" + vehicle.getId() + "S" + fromStationNode.getId() + "R" + vehicle.getInitializedRoutes().indexOf(route) + "SV" + stationVisit;

                    //Add edge
                    edge = graph.addEdge(edgeId, nodeFromStation, nodeToStation, true);

                    edge.addAttribute("ui.style", "size: 2px; fill-color: " + color + ";");

                }

            }

        }

    }

    public String getColor(int vehicleId) {
        String color = "#";
        switch(vehicleId) {
            case(0):
                color += "fd0000";
                break;
            case(1):
                color += "00de00";
                break;
            case(2):
                color += "0000fd";
                break;
            case(3):
                color += "fdfd00";
                break;
            case(4):
                color += "fd00fd";
                break;
            case(5):
                color += "00fdfd";
                break;
            case(6):
                color += "fd8f00";
                break;
            default:
                Random randomGenerator = new Random();
                String[] numbers = {"1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
                for (int i = 0; i < 6; i++) {
                    color += numbers[randomGenerator.nextInt(numbers.length)];
                }
        }

        return color;
    }
}