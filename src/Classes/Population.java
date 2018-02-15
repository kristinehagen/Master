package Classes;

import java.util.ArrayList;

public class Population {

    ArrayList<Individual> individuals;

    //Constructor: tar inn mange parametre, og setter lokale parametere lik disse
    public Population () {
        //Set alle init-verdier
        this.individuals = this.initializePopulation();

    }

    //Herlages den initielle populasjonen
    //MARTE JOBBER HER
    private ArrayList<Individual> initializePopulation() {
        return new ArrayList<>();
    }


    public void createNewGeneration() {

        //Plukk ut hva som skal parres
        //ørfrø crossover
        //Utfør elitisme
        //Utfør mutasjon

    }

    public Individual getBestIndividual() {
        return new Individual();
    }

}
