package classes;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Population {

    ArrayList<Individual> individuals;
    Individual bestPopulationIndividual;

    //Constructor: tar inn mange parametre, og setter lokale parametere lik disse
    public Population (Input input) {
        //Set alle init-verdier
        this.individuals = this.initializePopulation(input);

    }

    //Her lages den initielle populasjonen
    private ArrayList<Individual> initializePopulation(Input input) {
        double bestFitness = 10000;

        ArrayList<Individual> individuals = new ArrayList<>();

        //Lag hvert individ i populasjonen
        for (int i = 0; i < input.getSizeOfPopulation(); i++){

            Solution newSolution = new Solution();

            //Lag hver bilrute for individet
            for (Vehicle vehicle : input.getVehicles().values()){
                int numberOfVisitsForVehicle = ThreadLocalRandom.current().nextInt(1, input.getMaxVisitsForEachVehicle() + 1);
                ArrayList<Station> possibleStationVisits = new ArrayList<>(vehicle.getClusterIdList());
                ArrayList<Station> stationVisitsForVehicle = new ArrayList<>();

                //Lag rute for bil j individ i
                for (int k = 0; k < numberOfVisitsForVehicle; k++){
                    int pickStationIndex = ThreadLocalRandom.current().nextInt(0, possibleStationVisits.size());
                    stationVisitsForVehicle.add(possibleStationVisits.get(pickStationIndex));
                    possibleStationVisits.remove(pickStationIndex);
                }

                newSolution.addVehicleSequence(stationVisitsForVehicle);

            }
            Individual individual = new Individual(newSolution);
            individuals.add(individual);

            if (individual.getFitness()<bestFitness) {
                bestFitness = individual.getFitness();
                bestPopulationIndividual = individual;
            }
        }

        return individuals;
    }


    public void createNewGeneration(Input input, int tournamentParticipants) {

        //Selection - Binary Tournament
        Individual p1, p2;

        //Plukker ut totalt 50% parents og utfører crossover, elitisme, mutasjon på hver av parene
        for (int i = 0; i < input.getSizeOfPopulation() / 4; i++){
            p1 = getParent(tournamentParticipants);
            p2 = getParent(tournamentParticipants);

            //Utfør crossover
            //Utfør elitisme
            //Utfør mutasjon
        }
    }

    public Individual getParent(int tournamentParticipants){
        Individual parent;
        ArrayList<Individual> tournamentIndividuals = new ArrayList<>();
        ArrayList<Integer> usedIndices = new ArrayList<>();
        Random randomGenerator = new Random();
        int index;

        //Velger ut tournamentParticipants
        while (usedIndices.size() < tournamentParticipants) {
            index = randomGenerator.nextInt(individuals.size());
            if (!usedIndices.contains(index)) {
                tournamentIndividuals.add(individuals.get(index));
                usedIndices.add(index);
            }
        }

        //Velger ut den beste av tournamentParticipants med en gitt sannsynlighet (0.8)
        if (randomGenerator.nextFloat() < 0.8) {
                parent = this.getBestIndividual(tournamentIndividuals);
        } else {
                //0.2 sannsynlighet for at man velger random mellom tournamentParticipants
                parent = tournamentIndividuals.get(randomGenerator.nextInt(tournamentParticipants));
        }
        return parent;
    }

    //Velger den beste individual fra en liste med individuals
    public Individual getBestIndividual(ArrayList<Individual> individuals) {
        Individual bestIndividual = null;
        double fitness;
        double bestFitness = 100000;      //Veldig høy verdi slik at første individ blir beste individ
        for (Individual individual : individuals) {
            fitness = individual.getFitness();
            //Minimeringsproblem
            if (fitness < bestFitness) {
                bestFitness = fitness;
                bestIndividual = individual;
            }
        }
        return bestIndividual;
    }


    public Individual getBestIndividual() {
        return bestPopulationIndividual;
    }
}
