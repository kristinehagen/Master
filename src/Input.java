import java.util.ArrayList;

public class Input {

    private int maxNumberOfGenerations = 400;
    private String filename = "filename";
    private int maxVisitsForEachVehicle = 5;
    private int sizeOfPopulation = 100;
    private int numberOfVehicles = 5;
    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    public Input() {
        //Midlertidig
        for (int i = 0; i < numberOfVehicles; i++) {
            Vehicle vehicle = new Vehicle();
            this.vehicles.add(vehicle);
        }
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }


    public int getMaxVisitsForEachVehicle() {
        return maxVisitsForEachVehicle;
    }

    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }


    public int getSizeOfPopulation() {
        return sizeOfPopulation;
    }







    public int getMaxNumberOfGenerations() {
        return maxNumberOfGenerations;
    }

    public void setMaxNumberOfGenerations(int maxNumberOfGenerations) {
        this.maxNumberOfGenerations = maxNumberOfGenerations;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


}
