package solutionMethods;

import classes.Input;
import classes.StopWatch;
import com.dashoptimization.XPRMCompileException;
import enums.SolutionMethod;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.IOException;

public class ExactMethod {

    private double computationalTimeXpress;
    private double computationalTimeIncludingInitialization;

    //Constructor
    public ExactMethod(Input input) throws IOException, XPRMCompileException {

        //Start timer
        StopWatch stopWatchIncludingInitialization = new StopWatch();
        stopWatchIncludingInitialization.start();

        WriteXpressFiles.printFixedInput(input);
        WriteXpressFiles.printTimeDependentInput(input, SolutionMethod.EXACT_METHOD);

        //Start timer and run Xpress
        StopWatch stopWatchXpress = new StopWatch();
        stopWatchXpress.start();
        RunXpress.runXpress(input.getXpressFile());

        stopWatchXpress.stop();
        stopWatchIncludingInitialization.stop();

        this.computationalTimeXpress = stopWatchXpress.getElapsedTimeSecs();
        this.computationalTimeIncludingInitialization = stopWatchIncludingInitialization.getElapsedTimeSecs();
    }

    //Getters and setters
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
