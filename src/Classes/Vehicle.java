package Classes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Vehicle {

    private ArrayList<Station> cluster = new ArrayList<>();

    public Vehicle(){
    }

    public ArrayList<Station> getCluster(HashMap<Integer, Station> stations) {
        this.cluster = createCluster(stations);
        return cluster;
    }


    public ArrayList<Station> createCluster(HashMap<Integer, Station> stations){
        //Skal returnere stasjonenen som denne bilen har lov til å besøke
        ArrayList<Station> stationsList = new ArrayList<>();
        for (Station station : stations.values()) {
            stationsList.add(station);
        }
        return stationsList;
    }
}
