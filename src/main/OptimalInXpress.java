package main;

import classes.Input;
import com.dashoptimization.XPRMCompileException;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.IOException;

public class OptimalInXpress {

    //Constructor
    public  OptimalInXpress(Input input) throws IOException, XPRMCompileException {
        WriteXpressFiles.printFixedInput(input);
        WriteXpressFiles.printTimeDependentInput(input, false);
        RunXpress.runXpress(input.getXpressFileOptimalLevel());
    }

}