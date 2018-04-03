package main;

import classes.Input;
//import com.dashoptimization.XPRMCompileException;
//import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.IOException;

public class OptimalInXpress {

    //Constructor
    public  OptimalInXpress(Input input) throws IOException {
        WriteXpressFiles.printFixedInput(input);
        WriteXpressFiles.printTimeDependentInput(input, false, false);
        //RunXpress.runXpress(input.getXpressFileOptimalLevel());
    }

}
