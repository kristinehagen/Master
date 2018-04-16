package functions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadStationList {

    public static ArrayList<Integer> readStationIdList(String stationListFile) throws FileNotFoundException {
        ArrayList<Integer> stationIdList = new ArrayList<>();

        File inputFile = new File(stationListFile);
        Scanner in = new Scanner(inputFile);

        while (in.hasNextLine()) {
            String line = in.nextLine();
            Scanner element = new Scanner(line).useDelimiter("\\s*,\\s*");
            if (element.hasNextInt()) {
                int stationId = element.nextInt();
                stationIdList.add(stationId);
            }
            element.close();
        }
        in.close();
        return(stationIdList);
    }
}