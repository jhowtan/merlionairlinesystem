package MAS.CustomerAnalysis;

import MAS.Entity.Customer;

public class AnalysedCustomer {
    public Customer customer;
    public int segment;
    public double revenuePerMile;
    public int flightCount;
    public double cV;
    public double pV;

    public void analyseSegment() {
        //Values are a bit high because of our test data
        if (flightCount >= 200) {
            if (revenuePerMile >= 400)
                segment = 0;
            else
                segment = 2;
        } else if (flightCount < 200) {
            if (revenuePerMile >= 400)
                segment = 1;
            else
                segment = 3;
        }
    }
}
