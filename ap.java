import java.util.*;

public class ap {
    private ArrayList<String> allFlights;

    public double getTotalRevenue()
    {
        double total = 0;
        for (Flight flight : allFlights)
        {
            if (flight.getNumPassengers() > flight.getCapacity())
            {
                total += flight.getCapacity() * flight.getPrice();
            }
            else
            {
                total += flight.getNumPassengers() * flight.getPrice();
            }
        }
        return total;
    }
    
    public int updateFlights()
    {
        int counter = 0;
        for (int i = 0; i < allFlights.size(); i++)
        {
            Flight flight = allFlights.get(i);
            if ((flight.getNumPassengers() / flight.getCapacity()) < 0.2)
            {
                counter += flight.getNumPassengers();
                allFlights.remove(i);
                i--;
            }
        }
        return counter;
    }
}