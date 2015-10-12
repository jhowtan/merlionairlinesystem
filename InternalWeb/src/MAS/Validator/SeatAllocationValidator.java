package MAS.Validator;

import MAS.Bean.BookingClassBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Common.SeatConfigObject;
import MAS.Entity.BookingClass;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.List;

@FacesValidator
public class SeatAllocationValidator implements Validator {
    @EJB
    BookingClassBean bookingClassBean;
    @EJB
    FlightScheduleBean flightScheduleBean;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        int seats;
        try {
            seats = Integer.parseInt(o.toString());
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Invalid number of seats.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if (seats <= 0) {
            FacesMessage m = new FacesMessage("Invalid number of seats.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }

        long flightId = (Long) ((UIInput) uiComponent.getAttributes().get("flight")).getValue();
        int travelClass = (Integer) ((UIInput) uiComponent.getAttributes().get("travelClass")).getValue();
        SeatConfigObject seatConfigObject = new SeatConfigObject();
        int totalSeats = seatConfigObject.getSeatsInClass(travelClass);
        //List<BookingClass> sameFlightAndClass = null;

        try {
            seatConfigObject.parse(flightScheduleBean.findSeatConfigOfFlight(flightId));
            totalSeats = seatConfigObject.getSeatsInClass(travelClass);
            //sameFlightAndClass = bookingClassBean.findBookingClassByFlightAndClass(flightId, travelClass);
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage m = new FacesMessage("No seats available on this flight.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }

        if (seats > totalSeats) {
            FacesMessage m = new FacesMessage("There are only " + totalSeats + " seats for this travel class.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }

        /*
        int currSeats = 0;
        try {
            long bookingClassId = (long) uiComponent.getAttributes().get("bookingClass");
            currSeats = bookingClassBean.getBookingClass(bookingClassId).getAllocation();
        } catch (Exception e) {
        }
        int allocatedSeats = 0;
        for (int i = 0; i < sameFlightAndClass.size(); i++) {
            allocatedSeats += sameFlightAndClass.get(i).getAllocation();
        }
        int seatsLeft = totalSeats - allocatedSeats + currSeats;
        if (seatsLeft < 0) {
            FacesMessage m = new FacesMessage("There are no seats left on this flight.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if ((allocatedSeats - currSeats + seats) > totalSeats) {
            FacesMessage m = new FacesMessage("There are only " + seatsLeft + " seats left.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        */
    }
}
