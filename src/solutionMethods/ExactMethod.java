package solutionMethods;

import classes.Input;
import com.dashoptimization.XPRMCompileException;
import enums.SolutionMethod;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.IOException;

public class ExactMethod {

    //Constructor
    public ExactMethod(Input input) throws IOException, XPRMCompileException {
        WriteXpressFiles.printFixedInput(input);
        WriteXpressFiles.printTimeDependentInput(input, SolutionMethod.EXACT_METHOD);
        RunXpress.runXpress(input.getXpressFile());
    }

}
