package classes;

import java.util.Scanner;

public class CustomerArrival {

    private int stationId;
    private double load;
    private double time;




    //Getters and setters
    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public double getLoad() {
        return load;
    }

    public void setLoad(double load) {
        this.load = load;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setNoMoreCustomers(double simulationStopTime) {
        this.setLoad(0);
        this.setTime(simulationStopTime+1);
        this.setStationId(0);
    }


    public void updateNextCustomerArrival(Scanner in, double currentTime, CustomerArrival nextCustomerArrival, double simulationStopTime) {
        boolean searchForDemand = true;

        while(searchForDemand) {
            if (in.hasNextLine()) {
                String line = in.nextLine();
                Scanner element = new Scanner(line).useDelimiter("\\s*,\\s*");
                if (element.hasNext()) {
                    double nextDemandTime = Double.parseDouble(element.next())/60;                              //Time for next customer in minutes, actual time
                    int stationId = element.nextInt();
                    double load = Double.parseDouble(element.next());
                    if (nextDemandTime >= currentTime) {
                        nextCustomerArrival.setStationId(stationId);
                        nextCustomerArrival.setLoad(load);
                        nextCustomerArrival.setTime(nextDemandTime);
                        searchForDemand = false;
                    }
                } else {
                    //No more customer arrivals
                    searchForDemand = false;
                    nextCustomerArrival.setNoMoreCustomers(simulationStopTime);
                }
                element.close();
            }else{
                //No more customer arrivals
                searchForDemand = false;
                nextCustomerArrival.setNoMoreCustomers(simulationStopTime);
            }
        }
    }

}
