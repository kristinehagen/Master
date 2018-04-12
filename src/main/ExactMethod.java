package main;

import classes.Input;
import classes.SolutionMethod;
import com.dashoptimization.XPRMCompileException;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.IOException;

public class ExactMethod {

    //Constructor
    public ExactMethod(Input input) throws IOException {
        WriteXpressFiles.printFixedInput(input);
        WriteXpressFiles.printTimeDependentInput(input, SolutionMethod.EXACT_METHOD);
        //RunXpress.runXpress(input.getXpressFile);
    }

}
