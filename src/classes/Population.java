package classes;

import java.lang.reflect.Array;
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


    public void createNewGeneration(Input input, int tournamentParticipants, double crossoverProbability) {
        ArrayList<Individual> offsprings = new ArrayList<>();
        Individual p1, p2, o1, o2;
        Random randomGenerator = new Random();

        //Selection - Binary tournament
        for (int i = 0; i < input.getSizeOfPopulation() / 2; i++){ //Totalt 100% foreldre
            p1 = getParent(tournamentParticipants);
            p2 = getParent(tournamentParticipants);

            //Utfør crossover med en gitt sannsynlighet
            if (randomGenerator.nextFloat() < crossoverProbability)    {
                o1 = performCrossover(input, p1, p2); //Totalt 50% barn
                o2 = performCrossover(input, p1, p2);
            } else {
                o1 = new Individual(p1);
                o2 = new Individual(p2);
            }
            //Utfør elitisme
            //Utfør mutasjon

            offsprings.add(o1);
            offsprings.add(o2);
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


    public Individual performCrossover (Input input, Individual p1, Individual p2) {
        Solution offspring = new Solution();

        //Lager offspring
        for (int i = 0; i < input.getMaxVisitsForEachVehicle(); i++){
            Double randomNumber = ThreadLocalRandom.current().nextDouble(1);
            if (randomNumber < 0.5) {
                //50% sannsynlighet for å velge bil 1 fra parent 1
                offspring.addVehicleSequence(p1.getSolution().getVehicleSequence(i));
            } else {
                //50% sannsynlighet for å velge bil 1 fra parent 2
                offspring.addVehicleSequence(p2.getSolution().getVehicleSequence(i));
            }
        }  
        Individual newOffspring = new Individual(offspring);
        return newOffspring;
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
