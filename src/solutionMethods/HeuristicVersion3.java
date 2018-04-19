package solutionMethods;

import classes.Input;
import classes.StopWatch;
import classes.Vehicle;
import enums.SolutionMethod;
import com.dashoptimization.XPRMCompileException;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HeuristicVersion3 {

    private double computationalTimeXpress;
    private double computationalTimeIncludingInitialization;

    //Constructor
    public HeuristicVersion3(Input input) throws IOException, XPRMCompileException {

        //Start timer and run Xpress
        StopWatch stopWatchIncludingInitialization = new StopWatch();
        stopWatchIncludingInitialization.start();

        initiateRoutes(input);
        WriteXpressFiles.printTimeDependentInput(input, SolutionMethod.HEURISTIC_VERSION_3);

        //Start timer and run Xpress
        StopWatch stopWatchXpress = new StopWatch();
        stopWatchXpress.start();

        RunXpress.runXpress(input.getXpressFile());

        stopWatchIncludingInitialization.stop();
        stopWatchXpress.stop();
        this.computationalTimeXpress = stopWatchXpress.getElapsedTimeSecs();
        this.computationalTimeIncludingInitialization = stopWatchIncludingInitialization.getElapsedTimeSecs();
    }

    private static void initiateRoutes(Input input) throws FileNotFoundException, UnsupportedEncodingException {

        //Initialize routes for each vehicle
        for (Vehicle vehicle: input.getVehicles().values()) {
            vehicle.createRoutes(input);
        }


    }

    public double getComputationalTimeXpress() {
        return computationalTimeXpress;
    }

    public void setComputationalTimeXpress(double computationalTimeXpress) {
        this.computationalTimeXpress = computationalTimeXpress;
    }

    public double getComputationalTimeIncludingInitialization() {
        return computationalTimeIncludingInitialization;
    }

    public void setComputationalTimeIncludingInitialization(double computationalTimeIncludingInitialization) {
        this.computationalTimeIncludingInitialization = computationalTimeIncludingInitialization;
    }
}
