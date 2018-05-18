package main;


import classes.GraphViewer;
import classes.Input;
import classes.Station;
import classes.Vehicle;
import com.dashoptimization.XPRMCompileException;
import enums.SolutionMethod;
import functions.ReadClusterList;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GenerateCluster {


    public static void main(String[] args) throws IOException, XPRMCompileException {
        Input input = new Input();

        WriteXpressFiles.writeClusterInformation(input);
        RunXpress.runXpress("createCluster");


        readCluster(input);
        GraphViewer graph = new GraphViewer();
        graph.drawClusters(input);

        for (Vehicle vehicle : input.getVehicles().values()) {
            System.out.println("Antall stasjoner i cluster: " + vehicle.getClusterStationList().size());
        }

    }

    private static void readCluster(Input input) throws IOException {

        if (input.getSolutionMethod() == SolutionMethod.CURRENT_SOLUTION_IN_OSLO) {
            ReadClusterList.readClusterListExcel(input, "clusterCurrentSolution.xlsx");

        } else if (input.getSolutionMethod() == SolutionMethod.HEURISTIC_VERSION_1 || input.getSolutionMethod() == SolutionMethod.HEURISTIC_VERSION_2 || input.getSolutionMethod() == SolutionMethod.HEURISTIC_VERSION_3) {

            String xpressOutputFile = "clusterOutput.txt";
            ReadClusterList.readClusterListTextFile(input, xpressOutputFile);

        }

    }

}
