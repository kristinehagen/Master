import java.lang.reflect.Array;
import java.util.ArrayList;

public class Vehicle {

    private ArrayList<Station> cluster = new ArrayList<>();

    public Vehicle(){
    }

    public ArrayList<Station> getCluster() {
        this.cluster = createCluster();
        return cluster;
    }

    public ArrayList<Station> createCluster(){
        ArrayList<Station> possibleStations = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            Station station = new Station();
            possibleStations.add(station);
        }
        return possibleStations;
    }
}
