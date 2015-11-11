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
        if (flightCount >= 6) {
            if (revenuePerMile >= 2)
                segment = 0;
            else
                segment = 2;
        } else if (flightCount < 6) {
            if (revenuePerMile >= 2)
                segment = 1;
            else
                segment = 3;
        }
    }
}
