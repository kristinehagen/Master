package main;

import classes.GraphViewer;
import classes.Individual;
import classes.Input;
import classes.Population;

import java.io.FileNotFoundException;

public class GA {

    static Population population;
    static GraphViewer graphViewer;
    static Individual bestGlobalSolution;

    private static void init(String filename, Input input) {

        //Her leses all informasjon inn. (info om distanse, kunder, depot ol)
        //depot, customers, distansematrise og liknende blir satt
        // Se Aksel

        population = new Population(input);
        System.out.println("Initial population created");
        graphViewer = new GraphViewer();

        bestGlobalSolution = population.getBestIndividual();
        bestGlobalSolution.printIndividual(input.getVehicles());

    }

    private static void run(Input input) {

        int maxNumberOfGenerations = input.getMaxNumberOfGenerations();
        int count = 0;

        Individual generationBestIndividual = population.getBestIndividual();
        Individual globalBestIndividual = population.getBestIndividual();

        while (count < maxNumberOfGenerations) {

            population.createNewGeneration();
            System.out.println("One generation created");
            generationBestIndividual = population.getBestIndividual();

            if (generationBestIndividual.getFitness() < globalBestIndividual.getFitness() && generationBestIndividual.getFeasibility()) {
                globalBestIndividual = generationBestIndividual;
                //kan printe ny løsning
            } else {
                //her oppdateres en improvement count
            }
            break;
        }

    }




    public static void main(String[] args) throws FileNotFoundException {
        Input input = new Input();
        String filename = input.getFilename();
        init(filename, input);
        run(input);

        System.out.println("algoritm successfully terminated");
    }

}
