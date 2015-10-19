package MAS.Entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@TableGenerator(name = "eTicketNumberTable", initialValue=51200000, allocationSize = 1)
public class ETicket {
    private Long id;

    @GeneratedValue(strategy = GenerationType.TABLE, generator = "eTicketNumberTable")
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private PNR pnr;

    @OneToOne
    public PNR getPnr() {
        return pnr;
    }

    public void setPnr(PNR pnr) {
        this.pnr = pnr;
    }

    private int seatNumber;

    @Basic
    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    private BookingClass bookingClass;

    @ManyToOne
    public BookingClass getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(BookingClass bookingClass) {
        this.bookingClass = bookingClass;
    }

    private boolean checkedIn;

    @Basic
    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    private boolean gateChecked;

    @Basic
    public boolean isGateChecked() {
        return gateChecked;
    }

    public void setGateChecked(boolean gateChecked) {
        this.gateChecked = gateChecked;
    }

    private String passengerName;

    @Basic
    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    private Flight flight;

    @ManyToOne
    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    private int travelClass;

    @Basic
    public int getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(int travelClass) {
        this.travelClass = travelClass;
    }

    private String ffpNumber;

    @Basic
    public String getFfpNumber() {
        return ffpNumber;
    }

    public void setFfpNumber(String ffpNumber) {
        this.ffpNumber = ffpNumber;
    }

    private List<Baggage> baggages;

    @OneToMany
    public List<Baggage> getBaggages() {
        return baggages;
    }

    public void setBaggages(List<Baggage> baggages) {
        this.baggages = baggages;
    }

    private ETicket nextConnection;

    @OneToOne
    public ETicket getNextConnection() {
        return nextConnection;
    }

    public void setNextConnection(ETicket nextConnection) {
        this.nextConnection = nextConnection;
    }

    private Date created;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
