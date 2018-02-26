package main;

import classes.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GA {

    static Population population;
    static GraphViewer graphViewer;
    static Individual bestGlobalSolution;
    static HashMap<Integer, Station> stations;

    private static void init(Input input) {

        population = new Population(input);
        System.out.println("Initial population created");
        stations = input.getStations();
        graphViewer = new GraphViewer();

        bestGlobalSolution = population.getBestIndividual();
        bestGlobalSolution.printIndividual(input.getVehicles());

    }

    private static void run(Input input) {

        int maxNumberOfGenerations = input.getMaxNumberOfGenerations();
        int count = 0;

        Individual generationBestIndividual = population.getBestIndividual();
        Individual globalBestIndividual = population.getBestIndividual();


        graphViewer.displayDrawing(population.getBestIndividual(),true, input);

        while (count < maxNumberOfGenerations) {

            population.createNewGeneration(input);
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




    public static void main(String[] args) throws IOException {
        Input input = new Input();
        init(input);
        run(input);

        System.out.println("algoritm successfully terminated");
    }

}
