package classes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;

public class Individual {

    private double fitness;
    private Solution solution = new Solution();


    //Constructor
    public Individual(Solution solution) {
        this.fitness = calculateFitness(solution);
        this.solution = solution;
    }

    public Individual(Individual individual){
        this.fitness = individual.getFitness();
        this.solution = individual.getSolution();
    }

    //Print
    public void printIndividual(HashMap<Integer, Vehicle> vehicles) {
        ArrayList<Integer> vehicleId = new ArrayList<>();
        for (Vehicle vehicle: vehicles.values()) {
            vehicleId.add(vehicle.getId());
        }
        for (int i = 0; i < this.solution.getSolution().size(); i++) {
            System.out.print("Vehicle " + vehicleId.get(i) + ": ");
            for (int j = 0; j < this.solution.getSolution().get(i).size(); j++) {
                System.out.print(this.solution.getSolution().get(i).get(j).getId() + " ");
            }
            System.out.println();
        }
    }

    //Getters and setters

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        //Midlertidig fitnessfunksjon som trekker random verdi mellom 0 og 100
        int randomFitnessGenerator = ThreadLocalRandom.current().nextInt(0, 100);
        return randomFitnessGenerator;
    }

    public Solution getSolution() {

        return this.solution;
    }

    public void setSolution(Solution solution) {

        this.solution = solution;
    }

    public boolean getFeasibility() {

        //returnerer true hvis feasible, false ellers

        return true;
    }

    private double calculateFitness(Solution solution) {
        return 0;
    }

}
