import java.util.ArrayList;

public class Individual {

    private double fitness;
    private ArrayList<ArrayList<Integer>> solution;




    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {

        //Returnerer fitnessen til individet
        return 0.0;

    }

    public ArrayList<ArrayList<Integer>> getSolution() {
        return solution;
    }

    public void setSolution(ArrayList<ArrayList<Integer>> solution) {
        this.solution = solution;
    }


    public boolean getFeasibility() {

        //returnerer true hvis feasible, false ellers

        return true;
    }

}
