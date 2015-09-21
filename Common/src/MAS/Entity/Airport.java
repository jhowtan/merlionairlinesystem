package MAS.Entity;

import javax.persistence.*;

@Entity
public class Airport {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    private double latitude;

    @Basic
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private double longtitude;

    @Basic
    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    private String code;

    @Basic
    public String getCode() {
        return code;
    }

    public void setCode(String iata) {
        this.code = iata;
    }

    private City city;

    @ManyToOne
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    private int hangars;

    @Basic
    public int getHangars() {
        return hangars;
    }

    public void setHangars(int hangars) {
        this.hangars = hangars;
    }
}
