package solutionMethods;

import classes.Input;
import classes.Vehicle;
import com.dashoptimization.XPRMCompileException;
import enums.SolutionMethod;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HeuristicVersion1 {

    //Constructor
    public HeuristicVersion1(Input input) throws IOException, XPRMCompileException {
        WriteXpressFiles.printFixedInput(input);
        initiateRoutes(input);
        WriteXpressFiles.printTimeDependentInput(input, SolutionMethod.HEURISTIC_VERSION_1);
        RunXpress.runXpress(input.getXpressFile());
    }






    private static void initiateRoutes(Input input) throws FileNotFoundException, UnsupportedEncodingException {

        //Initialize routes for each vehicle
        for (Vehicle vehicle: input.getVehicles().values()) {
            vehicle.createRoutes(input);
        }

        /*
        GraphViewer graphViewer = new GraphViewer();
        graphViewer.displayInitiatedRoutes(input, true);
        */

    }






}
