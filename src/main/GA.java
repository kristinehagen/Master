package main;

import Classes.GraphViewer;
import Classes.Individual;
import Classes.Input;
import Classes.Population;

import java.io.FileNotFoundException;

public class GA {

    static Population population;
    static GraphViewer graphViewer;

    private static void init(String filename, Input input) {

        //Her leses all informasjon inn. (info om distanse, kunder, depot ol)
        //depot, customers, distansematrise og liknende blir satt
        // Se Aksel

        population = new Population(input);
        System.out.println("Initial population created");
        graphViewer = new GraphViewer();

    }

    private static void run(Input input) {

        int maxNumberOfGenerations = input.getMaxNumberOfGenerations();
        int count = 0;
        int tournamentParticipants = 2;

        Individual generationBestIndividual = population.getBestIndividual();
        Individual globalBestIndividual = population.getBestIndividual();

        while (count < maxNumberOfGenerations) {

            population.createNewGeneration(input, tournamentParticipants);
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
