package classes;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class PricingProblem {


    public PricingProblem(){
    }

    public void runPricingProblem(HashMap<Integer, Double> pricingProblemScores) throws FileNotFoundException {

        //Read Xpress input and assign pricingProblemScores
        File inputFile = new File("outputXpressViolationStatistics.txt");
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            Scanner element = new Scanner(line);
            if (element.hasNextInt()) {
                int stationId = element.nextInt();
                int visited = element.nextInt();
                double totalViolations = Double.parseDouble(element.next());
                double starvations = Double.parseDouble(element.next());
                double congestions = Double.parseDouble(element.next());
                if (visited == 0) {
                    pricingProblemScores.put(stationId, totalViolations + starvations + congestions);
                }
            }
        }
        in.close();
    }
}
