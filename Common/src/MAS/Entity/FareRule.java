package MAS.Entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class FareRule {
    private Long id;

    @GeneratedValue
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String name;

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int minimumStay;

    @Basic
    public int getMinimumStay() {
        return minimumStay;
    }

    public void setMinimumStay(int minimumStay) {
        this.minimumStay = minimumStay;
    }

    private int maximumStay;

    @Basic
    public int getMaximumStay() {
        return maximumStay;
    }

    public void setMaximumStay(int maximumStay) {
        this.maximumStay = maximumStay;
    }

    private int milesAccrual;

    @Basic
    public int getMilesAccrual() {
        return milesAccrual;
    }

    public void setMilesAccrual(int milesAccrual) {
        this.milesAccrual = milesAccrual;
    }

    private int advancePurchase;

    @Basic
    public int getAdvancePurchase() {
        return advancePurchase;
    }

    public void setAdvancePurchase(int advancePurchase) {
        this.advancePurchase = advancePurchase;
    }

    private boolean freeCancellation;

    @Basic
    public boolean isFreeCancellation() {
        return freeCancellation;
    }

    public void setFreeCancellation(boolean freeCancellation) {
        this.freeCancellation = freeCancellation;
    }

    private int minimumPassengers;

    @Basic
    public int getMinimumPassengers() {
        return minimumPassengers;
    }

    public void setMinimumPassengers(int minimumPassengers) {
        this.minimumPassengers = minimumPassengers;
    }

    private double priceMul;

    @Basic
    public double getPriceMul() {
        return priceMul;
    }

    public void setPriceMul(double priceMul) {
        this.priceMul = priceMul;
    }
}
