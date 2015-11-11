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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getSegment() {
        return segment;
    }

    public void setSegment(int segment) {
        this.segment = segment;
    }

    public double getRevenuePerMile() {
        return revenuePerMile;
    }

    public void setRevenuePerMile(double revenuePerMile) {
        this.revenuePerMile = revenuePerMile;
    }

    public int getFlightCount() {
        return flightCount;
    }

    public void setFlightCount(int flightCount) {
        this.flightCount = flightCount;
    }

    public double getcV() {
        return cV;
    }

    public void setcV(double cV) {
        this.cV = cV;
    }

    public double getpV() {
        return pV;
    }

    public void setpV(double pV) {
        this.pV = pV;
    }
}
