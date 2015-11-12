package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.CancelException;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.rmi.CORBA.Util;
import java.util.*;

@Stateless(name = "PNREJB")
@LocalBean
public class PNRBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    CustomerBean customerBean;
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    BookingClassBean bookingClassBean;

    public PNRBean() {
    }

    public PNR getPNR(long id) throws NotFoundException {
        PNR pnr = em.find(PNR.class, id);
        if (pnr == null) throw new NotFoundException();
        return pnr;
    }

    public PNR getPNR(long id, String passengerLastName) throws NotFoundException {
        PNR pnr = getPNR(id);
        for(String passenger : pnr.getPassengers()) {
            String[] parts = passenger.split("/");
            if (parts[0].toUpperCase().equals(passengerLastName.trim().toUpperCase())) {
                return pnr;
            }
        }
        throw new NotFoundException();
    }

    public List<PNR> getCustomerPNR(long custId) throws NotFoundException {
        Customer customer = customerBean.getCustomer(custId);
        List<PNR> pnrList = em.createQuery("SELECT p FROM PNR p", PNR.class).getResultList();
        List<PNR> customerPNR = new ArrayList<>();
        for (PNR pnr : pnrList) {
            for (String passenger : pnr.getPassengers()) {
                String[] parts = passenger.split("/");
                if (parts[0].toUpperCase().equals(customer.getLastName().trim().toUpperCase())) {
                    customerPNR.add(pnr);
                }
            }
        }
        Collections.sort(customerPNR, new Comparator<PNR>() {
            @Override
            public int compare(PNR o1, PNR o2) {
                return o1.getCreated().compareTo(o2.getCreated());
            }
        });
        return customerPNR;
    }

    public List<ETicket> getETicketsByPNR(PNR pnr) {
        return em.createQuery("SELECT e FROM ETicket e WHERE e.pnr = :pnr", ETicket.class).setParameter("pnr", pnr).getResultList();
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

    public void cancel(long pnrId) throws CancelException {
        try {
            PNR pnr = getPNR(pnrId);
            ArrayList<ETicket> eTickets = new ArrayList<>();
            for (SpecialServiceRequest ssr : pnr.getSpecialServiceRequests()) {
                if (ssr.getActionCode().equals(Constants.SSR_ACTION_CODE_TICKET_NUMBER)) {
                    ETicket eTicket = flightScheduleBean.getETicket(Long.parseLong(ssr.getValue()));
                    if (eTicket.getFlight().getDepartureTime().before(Utils.hoursFromNow(24 * 2))) {
                        throw new CancelException();
                    }
                    eTickets.add(eTicket);
                }
            }
            for (ETicket eTicket : eTickets) {
                BookingClass bookingClass = bookingClassBean.getBookingClass(eTicket.getBookingClass().getId());
                bookingClass.setOccupied(bookingClass.getOccupied() - 1);
                em.persist(bookingClass);
                em.remove(eTicket);
            }
            em.remove(pnr);
            em.flush();
        } catch (Exception e) {
            throw new CancelException();
        }
    }

    public String getRequest(ETicket eTicket) {
        try {
            PNR pnr = eTicket.getPnr();
            int passengerNum = getPassengerNumber(pnr, eTicket.getPassengerName());
            int itineraryNum = getItineraryNumber(pnr, eTicket.getFlight());
            return getPassengerSpecialServiceRequest(pnr, passengerNum, itineraryNum, Constants.SSR_ACTION_CODE_REQUEST).getValue();
        } catch (NotFoundException e) {
            return "";
        }
    }

}
