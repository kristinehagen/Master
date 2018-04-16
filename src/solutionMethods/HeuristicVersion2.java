package solutionMethods;

import classes.Input;
import classes.Vehicle;
import enums.SolutionMethod;
import com.dashoptimization.XPRMCompileException;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HeuristicVersion2 {

    //Constructor
    public HeuristicVersion2(Input input) throws IOException, XPRMCompileException {
        WriteXpressFiles.printFixedInput(input);
        initiateRoutes(input);
        WriteXpressFiles.printTimeDependentInput(input, SolutionMethod.HEURISTIC_VERSION_2);
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
