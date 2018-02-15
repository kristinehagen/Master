package Classes;

import java.util.ArrayList;

public class Solution {

    private ArrayList<ArrayList<Station>> solution = new ArrayList<>();

    public ArrayList<ArrayList<Station>> getSolution() {
        return solution;
    }

    public void setSolution(ArrayList<ArrayList<Station>> solution) {
        this.solution = solution;
    }

    public void addVehicleSequence(ArrayList<Station> vehicleSequence){
        this.solution.add(vehicleSequence);
    }


}
