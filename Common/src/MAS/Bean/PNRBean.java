package MAS.Bean;

import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(name = "PNREJB")
@LocalBean
public class PNRBean {
    @PersistenceContext
    EntityManager em;

    public PNRBean() {
    }

    public PNR createPNR(List<String> passengerNames, List<Flight> flights, String travelAgent) {
        PNR pnr = new PNR();

        pnr.setTravelAgent(travelAgent);
        pnr.setCreatedTimestamp(new Date());

        ArrayList<PNRItem> pnrItems = new ArrayList<>();

        for (String passengerName : passengerNames) {
            PNRItem pnrItem = new PNRItem();
            pnrItem.setPassengerName(passengerName);

            ArrayList<FlightItem> flightItems = new ArrayList<>();

            for (Flight flight : flights) {
                FlightItem flightItem = new FlightItem();
                flightItem.setFlight(flight);
                flightItems.add(flightItem);
            }
            pnrItem.setFlightItems(flightItems);
            pnrItems.add(pnrItem);
        }

        pnr.setPNRItems(pnrItems);

        em.persist(pnr);
        em.flush();
        return pnr;
    }

    public void updateFFP(String bookingReference, String passengerName, String ffpProgram, String ffpNumber) throws NotFoundException {
        PNR pnr = em.find(PNR.class, Utils.convertBookingReference(bookingReference));
        if (pnr == null) throw new NotFoundException();
        // @TODO

        List<PNRItem> pnrItems = pnr.getPNRItems();
        for (PNRItem pnrItem : pnrItems) {
            if (pnrItem.getPassengerName().equals(passengerName)) {
                pnrItem.setFrequentFlyerProgram(ffpProgram);
                pnrItem.setFrequentFlyerNumber(ffpNumber);
            }
        }
        pnr.setPNRItems(pnrItems);
        em.merge(pnr);
        em.flush();
    }


}
