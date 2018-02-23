package classes;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;

public class Individual {

    private double fitness;
    private ArrayList<ArrayList<Station>> solution = new ArrayList<>();

    //Constructor
    public Individual(ArrayList<ArrayList<Station>> solution) {
        this.fitness = calculateFitness(solution);
        this.solution = solution;
    }

    public Individual(Individual individual){
        this.fitness = individual.getFitness();
        this.solution = individual.getSolution();
    }

    //Print
    public void printIndividual(HashMap<Integer, Vehicle> vehicles) {
        ArrayList<Integer> vehicleId = new ArrayList<>();
        for (Vehicle vehicle: vehicles.values()) {
            vehicleId.add(vehicle.getId());
        }
        for (int i = 0; i < this.solution.size(); i++) {
            System.out.print("Vehicle " + vehicleId.get(i) + ": ");
            for (int j = 0; j < this.solution.get(i).size(); j++) {
                System.out.print(this.solution.get(i).get(j).getId() + " ");
            }
            System.out.println();
        }
    }

    public void doIntraMutation(Input input) {
        //Trekker random bil som skal muteres
        int mutationVehicleIndex = ThreadLocalRandom.current().nextInt(0, input.getNumberOfVehicles());
        Vehicle vehicle = input.getVehicle(mutationVehicleIndex);
        ArrayList<Station> vehicleSequence = new ArrayList<>(this.solution.get(mutationVehicleIndex));
        //Trekker stasjon som skal byttes ut og stasjon i cluster som skal settes inn
        int mutationStationIndexOld = ThreadLocalRandom.current().nextInt(0, vehicleSequence.size());
        int mutationStationIndexNew = ThreadLocalRandom.current().nextInt(0, vehicle.getClusterStationList().size());
        //Så lenge ny stasjon allerede er i vehicle sequence
        while (vehicleSequence.contains(vehicle.getClusterIdList(mutationStationIndexNew))) {
            mutationStationIndexNew = ThreadLocalRandom.current().nextInt(0, vehicle.getClusterStationList().size());
        }
        //Muterer vehiclesequence
        vehicleSequence.set(mutationStationIndexOld, vehicle.getClusterIdList(mutationStationIndexNew));
        //Legger til mutert vehiclesequence i individet
        this.solution.add(mutationVehicleIndex, vehicleSequence);
    }

    //Henter ut vehicleSequence for en bestemt bil
    public ArrayList<Station> getVehicleSequence(int index) { return this.solution.get(index); }

    public void addVehicleSequenceAtIndex(int index, ArrayList<Station> vehicleSequence){
        this.solution.set(index, vehicleSequence);
    }

    //Getters and setters

    public ArrayList<ArrayList<Station>> getSolution() { return solution; }

    public void setSolution(ArrayList<ArrayList<Station>> solution) { this.solution = solution; }


    public void setFitness(double fitness) { this.fitness = fitness; }

    public double getFitness() {
        //Midlertidig fitnessfunksjon som trekker random verdi mellom 0 og 100
        int randomFitnessGenerator = ThreadLocalRandom.current().nextInt(0, 100);
        return randomFitnessGenerator;
    }

    public boolean getFeasibility() {
        //returnerer true hvis feasible, false ellers
        return true;
    }

    private double calculateFitness(ArrayList<ArrayList<Station>> solution) {
        return 0;
    }


}
