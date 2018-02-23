package classes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import main.GA;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.spriteManager.Sprite;
import org.omg.CORBA.MARSHAL;
import org.graphstream.ui.spriteManager.SpriteManager;

public class GraphViewer {

    Graph graph;

    public GraphViewer() {
        this.graph = new MultiGraph("Graph 1");
    }



    private void drawCustomers(Graph graph, Individual individual, Input input) {
        //System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        ArrayList<Station> tour;
        String color;
        Edge edge;

        for (Station station : input.getStations().values()) {
            Node node = graph.addNode("Station" + station.getId());
            node.addAttribute("x", station.getLatitude());
            node.addAttribute("y", station.getLongitude());
            node.addAttribute("layout.frozen");
            node.addAttribute("ui.style", "fill-color: black;");
            node.addAttribute("ui.label", station.getId());

        }

        /*
        for (Vehicle vehicle : input.getVehicles().values()) {
            Node node = graph.addNode("Vehicle" + vehicle.getId());
            Station initialStation = input.getStations().get(vehicle.getNextStationInitial());
            node.addAttribute("x", initialStation.getLatitude());
            node.addAttribute("y", initialStation.getLongitude());
            node.addAttribute("layout.frozen");
            node.addAttribute("ui.style", "fill-color: red;");
        }
        */

        ArrayList<ArrayList<Station>> solution = individual.getSolution();
        for (int vehicle = 0; vehicle < solution.size(); vehicle++) {
            color = randomColor();
            for (int station = 0; station < solution.get(vehicle).size()-1; station++) {
                //For each pair of nodes within each vehicle sequence

                if (solution.get(vehicle).size() <= 1) {
                    continue;
                }

                //From station
                Station fromStationNode = solution.get(vehicle).get(station);
                Node nodeFromStation = graph.getNode("Station" + fromStationNode.getId());

                //To station
                Station toStationNode = solution.get(vehicle).get(station+1);
                Node nodeToStation = graph.getNode("Station" + toStationNode.getId());

                //Edge ID
                String edgeId = "V" + vehicle + "S" + fromStationNode.getId();

                //Add edge
                edge = graph.addEdge(edgeId, nodeFromStation, nodeToStation, true);

                edge.addAttribute("ui.style", "size: 2px; fill-color: " + color + ";");
            }
        }
    }

    public void displayDrawing(Individual bestIndividual, boolean initial, Input input) {
        this.graph.clear();
        this.drawCustomers(this.graph, bestIndividual, input);
        if (initial) {
            this.graph.display();
        }
    }

    public String randomColor() {
        String color = "#";
        Random randomGenerator = new Random();
        String[] numbers = {"1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
        for (int i = 0; i < 6; i++) {
            color += numbers[randomGenerator.nextInt(numbers.length)];
        }
        return color;
    }
}