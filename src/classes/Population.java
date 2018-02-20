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

        //Create each individual in the population
        for (int individualCount = 0; individualCount < input.getSizeOfPopulation(); individualCount++){

            ArrayList<ArrayList<Station>> newSolution = new ArrayList<>();

            //Create route for each vehicle
            for (Vehicle vehicle : input.getVehicles().values()){
                int numberOfVisitsForVehicle = ThreadLocalRandom.current().nextInt(input.getMinVisitsForEachVehicle(), input.getMaxVisitsForEachVehicle() + 1);
                ArrayList<Station> possibleStationVisits = new ArrayList<>(vehicle.getClusterStationList());
                ArrayList<Station> stationVisitsForVehicle = new ArrayList<>();

                //First station visit (pre-defined)
                Station firstStationVisit = input.getStations().get(vehicle.getNextStationInitial());
                stationVisitsForVehicle.add(firstStationVisit);
                possibleStationVisits.remove(firstStationVisit);

                //Second ++ station visit
                for (int stationVisit = 1; stationVisit < numberOfVisitsForVehicle; stationVisit++){
                    int pickStationIndex = ThreadLocalRandom.current().nextInt(0, possibleStationVisits.size());
                    stationVisitsForVehicle.add(possibleStationVisits.get(pickStationIndex));
                    possibleStationVisits.remove(pickStationIndex);
                }
                newSolution.add(stationVisitsForVehicle);

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


    public void createNewGeneration(Input input) {
        ArrayList<Individual> offsprings = new ArrayList<>();
        Individual p1, p2, o1, o2;
        Random randomGenerator = new Random();

        //Selection - Binary tournament
        for (int i = 0; i < input.getSizeOfPopulation() / 2; i++){ //Totalt 100% foreldre
            p1 = getParent(input.getTournamentParticipants());
            p2 = getParent(input.getTournamentParticipants());

            //Utfør crossover med en gitt sannsynlighet
            if (randomGenerator.nextFloat() < input.getCrossoverProbability())    {
                o1 = performCrossover(input, p1, p2); //Totalt 50% barn
                o2 = performCrossover(input, p1, p2);
            } else {
                o1 = new Individual(p1);
                o2 = new Individual(p2);
            }

            //Utfør intra-mutasjon med en gitt sannsynlighet
            if (randomGenerator.nextFloat() < input.getIntraMutationProbability()) {
                o1.doIntraMutation(input);
            }
            if (randomGenerator.nextFloat() < input.getIntraMutationProbability()) {
                o2.doIntraMutation(input);
            }

            //Utfør ellitisme

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
        ArrayList<ArrayList<Station>> offspring = new ArrayList<>();

        //Lager offspring
        for (int i = 0; i < input.getNumberOfVehicles(); i++){
            Double randomNumber = ThreadLocalRandom.current().nextDouble(1);
            if (randomNumber < 0.5) {
                //50% sannsynlighet for å velge bil i fra parent 1
                offspring.add(p1.getSolution().get(i));
            } else {
                //50% sannsynlighet for å velge bil i fra parent 2
                offspring.add(p2.getSolution().get(i));
            }
        }  
        Individual newOffspring = new Individual(offspring);
        return newOffspring;
    }

    //Velger den beste individual fra en liste med individuals
    public Individual getBestIndividual(ArrayList<Individual> individuals) {
        Individual bestIndividual = null;
        double bestFitness = 10000000;      //Veldig høy verdi slik at første individ blir beste individ
        for (Individual individual : individuals) {
            //Minimeringsproblem
            if (individual.getFitness() < bestFitness) {
                bestFitness = individual.getFitness();
                bestIndividual = individual;
            }
        }
        return bestIndividual;
    }


    public Individual getBestIndividual() {
        return bestPopulationIndividual;
    }
}
