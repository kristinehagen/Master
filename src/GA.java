public class GA {

    static Population population;
    static GraphViewer graphViewer;

    private static void init(String filename) {

        //Her leses all informasjon inn. (info om distanse, kunder, depot ol)
        //depot, customers, distansematrise og liknende blir satt
        // Se Aksel

        population = new Population();
        graphViewer = new GraphViewer();

    }

    private static void run(Input input) {

        int maxNumberOfGenerations = input.getMaxNumberOfGenerations();
        int count = 0;

        Individual generationBestIndividual = population.getBestIndividual();
        Individual globalBestIndividual = population.getBestIndividual();

        while (count < maxNumberOfGenerations) {

            population.createNewGeneration();
            generationBestIndividual = population.getBestIndividual();

            if (generationBestIndividual.getFitness() < globalBestIndividual.getFitness() && generationBestIndividual.getFeasibility()) {
                globalBestIndividual = generationBestIndividual;
                //kan printe ny løsning
            } else {
                //her oppdateres en improvement count
            }

        }

    }




    public static void main(String[] args) {
        Input input = new Input();
        String filename = input.getFilename();
        init(filename);
        run(input);

        System.out.println("algoritm successfully terminated");
    }

}
