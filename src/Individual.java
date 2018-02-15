import java.util.ArrayList;

public class Individual {

    private double fitness;
    private Solution solution = new Solution();

    public Individual(Solution solution) {
        this.fitness = calculateFitness(solution);
        this.solution = solution;
    }


    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {

        //Returnerer fitnessen til individet
        return 0.0;

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
