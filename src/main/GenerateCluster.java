package main;


import classes.Input;
import com.dashoptimization.XPRMCompileException;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.IOException;

public class GenerateCluster {


    public static void main(String[] args) throws IOException, XPRMCompileException {
        Input input = new Input();
        WriteXpressFiles.writeClusterInformation(input);
        RunXpress.runXpress("createCluster");
    }

}
