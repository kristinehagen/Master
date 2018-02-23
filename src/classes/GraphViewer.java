package classes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import main.GA;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
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

            /*
            if (station.getId() == 35) {
                SpriteManager sman = new SpriteManager(graph);
                Sprite s = sman.addSprite("S1");
                s.attachToNode("Station35");
                s.setPosition(StyleConstants.Units.PX, 12, 12, 0);
            }
            */
        }
        

        for (Vehicle vehicle : input.getVehicles().values()) {
            graph.getNode("Station" + vehicle.getNextStationInitial()).addAttribute("ui.style", "fill-color: red;");
        }


        ArrayList<ArrayList<Station>> solution = individual.getSolution();
        for (int vehicle = 0; vehicle < solution.size(); vehicle++) {
            color = getColor(vehicle);
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