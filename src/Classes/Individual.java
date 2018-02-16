package Classes;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Individual {

    private double fitness;
    private Solution solution = new Solution();

    public Individual(Solution solution) {
        this.fitness = calculateFitness(solution);
        this.solution = solution;
    }

    public Individual(Individual individual){
        this.fitness = individual.getFitness();
        this.solution = individual.getSolution();
    }


    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        //Midlertidig fitnessfunksjon som trekker random verdi mellom 0 og 100
        int randomFitnessGenerator = ThreadLocalRandom.current().nextInt(0, 100);
        return randomFitnessGenerator;
    }

    public Solution getSolution() {
        return solution;
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
