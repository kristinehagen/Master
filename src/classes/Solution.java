package classes;

import java.util.ArrayList;

public class Solution {

    private ArrayList<ArrayList<Station>> solution = new ArrayList<>();

    public ArrayList<ArrayList<Station>> getSolution() {
        return solution;
    }

    //Henter ut vehicleSequence for en bestemt bil
    public ArrayList<Station> getVehicleSequence(int index) {
        ArrayList<Station> vehicleSequence;
        vehicleSequence = this.solution.get(index);
        return vehicleSequence;
    }

    public void setSolution(ArrayList<ArrayList<Station>> solution) {
        this.solution = solution;
    }

    public void addVehicleSequence(ArrayList<Station> vehicleSequence){
        this.solution.add(vehicleSequence);
    }



}
