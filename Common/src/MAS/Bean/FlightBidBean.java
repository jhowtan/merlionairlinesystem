package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.Flight;
import MAS.Entity.FlightBid;
import MAS.Entity.IntAttribute;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(name = "FlightBidEJB")
@LocalBean
public class FlightBidBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    UserBean userBean;
    @EJB
    AttributesBean attributesBean;

    public FlightBidBean() {
    }

    public long createFlightBid(long userId, List<Long> flightIds) throws NotFoundException {
        User bidder = em.find(User.class, userId);
        if (bidder == null) throw new NotFoundException();
        FlightBid flightBid = new FlightBid();
        flightBid.setBidder(bidder);
        flightBid.setBidDate(new Date());
        flightBid.setStatus(0);
        List<Flight> flightList = new ArrayList<>();
        for (int i = 0; i < flightIds.size(); i++) {
            Flight flight = em.find(Flight.class, flightIds.get(i));
            if (flight == null) throw new NotFoundException();
            flightList.add(flight);
        }
        flightBid.setFlights(flightList);
        em.persist(flightBid);
        em.flush();
        return flightBid.getId();
    }

    public FlightBid getFlightBid(long id) throws NotFoundException {
        FlightBid flightBid = em.find(FlightBid.class, id);
        if (flightBid == null) throw new NotFoundException();
        return flightBid;
    }

    public List<FlightBid> getFlightBidsWithStatus(int status) {
        return em.createQuery("SELECT f from FlightBid f WHERE f.status = :status", FlightBid.class).setParameter("status", status).getResultList();
    }

    public void removeFlightBid(long id) throws NotFoundException {
        FlightBid flightBid = em.find(FlightBid.class, id);
        if (flightBid == null) throw new NotFoundException();
        em.remove(flightBid);
    }

    public void spamFlightBids() {
        Date startOfMonth = Utils.monthStart(1);
        Date endOfMonth = Utils.monthEnd(1);
        List<Flight> flights = flightScheduleBean.getFlightWithinDate(startOfMonth, endOfMonth);
        List<User> users = userBean.getUsersWithJobs(Constants.cabinCrewJobId);
        users.addAll(userBean.getUsersWithJobs(Constants.cockpitCrewJobId));
        for (int j = 0; j < users.size(); j++) {
            List<Long> biddedFlights = new ArrayList<>();
            for (int i = 0; i < attributesBean.getIntAttribute(Constants.FLIGHTJOBS_PER_MONTH, 5); i++) {
                //long flightId = -1;
                //while (biddedFlights.indexOf(flightId) != -1 || flightId == -1)
                //{
                    long flightId = flights.get((int)(Math.random() * flights.size())).getId();
                //}
                if (biddedFlights.indexOf(flightId) == -1)
                    biddedFlights.add(flightId);
            }
            try {
                long id = createFlightBid(users.get(j).getId(), biddedFlights);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
