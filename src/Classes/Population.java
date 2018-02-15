package Classes;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Population {

    ArrayList<Individual> individuals;

    //Constructor: tar inn mange parametre, og setter lokale parametere lik disse
    public Population (Input input) {
        //Set alle init-verdier
        this.individuals = this.initializePopulation(input);

    }

    //Her lages den initielle populasjonen
    //MARTE JOBBER HER
    private ArrayList<Individual> initializePopulation(Input input) {
        ArrayList<Individual> individuals = new ArrayList<>();
        //Lag hvert individ i populasjonen
        for (int i = 0; i < input.getSizeOfPopulation(); i++){

            Solution newSolution = new Solution();

            //Lag hver bilrute for individet
            for (Vehicle vehicle : input.getVehicles()){
                int numberOfVisitsForVehicle = ThreadLocalRandom.current().nextInt(0, input.getMaxVisitsForEachVehicle() + 1);
                ArrayList<Station> possibleStationVisits = new ArrayList<>(vehicle.getCluster(input.getStations()));
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
        }

        return individuals;
    }


    public void createNewGeneration() {
        //Plukk ut hva som skal parres
        //ørfrø crossover
        //Utfør elitisme
        //Utfør mutasjon

    }


    public Individual getBestIndividual() {
        Solution solution = new Solution();
        return new Individual(solution);
    }
}
