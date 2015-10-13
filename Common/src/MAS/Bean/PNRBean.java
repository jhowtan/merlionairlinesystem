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

    public int getPassengerNumber(PNR pnr, String fullName) throws NotFoundException {
        if (!pnr.getPassengers().contains(fullName)) throw new NotFoundException();
        return pnr.getPassengers().indexOf(fullName) + 1;
    }

    public int getItineraryNumber(PNR pnr, String flightCode) throws NotFoundException {
        List<Itinerary> itineraries = pnr.getItineraries();
        for (Itinerary itinerary : itineraries) {
            if (itinerary.getFlightCode().equals(flightCode)) {
                return itineraries.indexOf(itinerary);
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
    }

    public void setSpecialServiceRequest(PNR pnr, int passengerNumber, String actionCode, String value) {
        setSpecialServiceRequest(pnr, passengerNumber, actionCode, value, -1);
    }


//    public PNR createPNR(List<String> passengerNames, List<Flight> flights, String travelAgent) {
//        PNR pnr = new PNR();
//
//        pnr.setTravelAgent(travelAgent);
//        pnr.setCreatedTimestamp(new Date());
//
//        ArrayList<PNRItem> pnrItems = new ArrayList<>();
//
//        for (String passengerName : passengerNames) {
//            PNRItem pnrItem = new PNRItem();
//            pnrItem.setPassengerName(passengerName);
//
//            ArrayList<FlightItem> flightItems = new ArrayList<>();
//
//            for (Flight flight : flights) {
//                FlightItem flightItem = new FlightItem();
//                flightItem.setFlight(flight);
//                flightItems.add(flightItem);
//            }
//            pnrItem.setFlightItems(flightItems);
//            pnrItems.add(pnrItem);
//        }
//
//        pnr.setPNRItems(pnrItems);
//
//        em.persist(pnr);
//        em.flush();
//        return pnr;
//    }
//
//    public PNR getPNR(String bookingReference) throws NotFoundException {
//        PNR pnr = em.find(PNR.class, Utils.convertBookingReference(bookingReference));
//        if (pnr == null) throw new NotFoundException();
//        return pnr;
//    }
//
//    public PNRItem getPNRItem(PNR pnr, String passengerName) throws NotFoundException {
//        for (PNRItem pnrItem : pnr.getPNRItems()) {
//            if (pnrItem.getPassengerName().equals(passengerName)) {
//                return pnrItem;
//            }
//        }
//        throw new NotFoundException();
//    }
//
//    public PNRItem getPNRItem(String bookingReference, String passengerName) throws NotFoundException {
//        return getPNRItem(getPNR(bookingReference), passengerName);
//    }
//
//    public void updateFFP(String bookingReference, String passengerName, String ffpProgram, String ffpNumber) throws NotFoundException {
//        PNRItem pnrItem = getPNRItem(bookingReference, passengerName);
//        pnrItem.setFrequentFlyerProgram(ffpProgram);
//        pnrItem.setFrequentFlyerNumber(ffpNumber);
//        em.merge(pnrItem);
//        em.flush();
//    }


}
