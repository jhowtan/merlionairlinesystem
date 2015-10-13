package MAS.Entity;

import javax.persistence.*;

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

    private String seatNumber;

    @Basic
    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
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

    private Customer customer;

    @ManyToOne
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
}
