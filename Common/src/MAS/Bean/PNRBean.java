package MAS.Bean;

import MAS.Entity.Flight;
import MAS.Entity.Itinerary;
import MAS.Entity.PNR;
import MAS.Entity.SpecialServiceRequest;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Stateless(name = "PNREJB")
@LocalBean
public class PNRBean {
    @PersistenceContext
    EntityManager em;

    public PNRBean() {
    }

    public void updatePNR(PNR pnr) throws NotFoundException {
        if (em.find(PNR.class, pnr.getId()) == null) throw new NotFoundException();
        em.merge(pnr);
    }

    public int getPassengerNumber(PNR pnr, String fullName) throws NotFoundException {
        if (!pnr.getPassengers().contains(fullName)) throw new NotFoundException();
        return pnr.getPassengers().indexOf(fullName) + 1;
    }

    public int getItineraryNumber(PNR pnr, String flightCode) throws NotFoundException {
        List<Itinerary> itineraries = pnr.getItineraries();
        for (Itinerary itinerary : itineraries) {
            if (itinerary.getFlightCode().equals(flightCode)) {
                return itineraries.indexOf(itinerary) + 1;
            }
        }
        throw new NotFoundException();
    }

    public int getItineraryNumber(PNR pnr, Flight flight) throws NotFoundException {
        return getItineraryNumber(pnr, flight.getCode());
    }

    public List<SpecialServiceRequest> getPassengerSpecialServiceRequests(PNR pnr, int passengerNumber) {
        ArrayList<SpecialServiceRequest> passengerSSRs = new ArrayList<>();
        for (SpecialServiceRequest ssr : pnr.getSpecialServiceRequests()) {
            if (ssr.getPassengerNumber() == passengerNumber) {
                passengerSSRs.add(ssr);
            }
        }
        return passengerSSRs;
    }

    public SpecialServiceRequest getPassengerSpecialServiceRequest(PNR pnr, int passengerNumber, String actionCode) throws NotFoundException {
        for (SpecialServiceRequest ssr : pnr.getSpecialServiceRequests()) {
            if (ssr.getPassengerNumber() == passengerNumber && ssr.getActionCode().equals(actionCode)) {
                return ssr;
            }
        }
        throw new NotFoundException();
    }

    public SpecialServiceRequest getPassengerSpecialServiceRequest(PNR pnr, int passengerNumber, int itineraryNumber, String actionCode) throws NotFoundException {
        for (SpecialServiceRequest ssr : pnr.getSpecialServiceRequests()) {
            if (ssr.getPassengerNumber() == passengerNumber && ssr.getItineraryNumber() == itineraryNumber && ssr.getActionCode().equals(actionCode)) {
                return ssr;
            }
        }
        throw new NotFoundException();
    }

    public void deleteSpecialServiceRequest(PNR pnr, int passengerNumber, String actionCode) {
        Iterator it = pnr.getSpecialServiceRequests().iterator();
        while (it.hasNext()) {
            SpecialServiceRequest ssr = (SpecialServiceRequest) it.next();
            if (ssr.getPassengerNumber() == passengerNumber && ssr.getActionCode().equals(actionCode)) {
                it.remove();
            }
        }
    }

    public void setSpecialServiceRequest(PNR pnr, int passengerNumber, String actionCode, String value, int itineraryNumber) {
        deleteSpecialServiceRequest(pnr, passengerNumber, actionCode);
        SpecialServiceRequest ssr = new SpecialServiceRequest();
        ssr.setActionCode(actionCode);
        ssr.setPassengerNumber(passengerNumber);
        ssr.setValue(value);
        if (itineraryNumber != -1) {
            ssr.setItineraryNumber(itineraryNumber);
        }
        pnr.getSpecialServiceRequests().add(ssr);
    }

    public void setSpecialServiceRequest(PNR pnr, int passengerNumber, String actionCode, String value) {
        setSpecialServiceRequest(pnr, passengerNumber, actionCode, value, -1);
    }

}
