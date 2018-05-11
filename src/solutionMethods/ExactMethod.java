package solutionMethods;

import classes.Input;
import classes.StopWatch;
import com.dashoptimization.XPRMCompileException;
import enums.SolutionMethod;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.IOException;

public class ExactMethod {

    private double computationalTime;

    //Constructor
    public ExactMethod(Input input) throws IOException, XPRMCompileException {

        //Start timer
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        WriteXpressFiles.printTimeDependentInput(input, SolutionMethod.EXACT_METHOD);
        RunXpress.runXpress(input.getXpressFile());

        stopWatch.stop();

        this.computationalTime = stopWatch.getElapsedTimeSecs();
    }

    //Getters and setters
    public double getComputationalTime() {
        return computationalTime;
    }

    public void setComputationalTime(double computationalTime) {
        this.computationalTime = computationalTime;
    }

}
