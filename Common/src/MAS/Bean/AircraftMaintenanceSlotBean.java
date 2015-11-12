package MAS.Bean;

import MAS.Common.Utils;
import MAS.Entity.Aircraft;
import MAS.Entity.AircraftMaintenanceSlot;
import MAS.Entity.Airport;
import MAS.Exception.NotFoundException;
import MAS.Exception.ScheduleClashException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Stateless(name = "AircraftMaintenanceSlotEJB")
@LocalBean
public class AircraftMaintenanceSlotBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    FlightScheduleBean flightScheduleBean;

    public AircraftMaintenanceSlotBean() {
    }

    //-----------------Maintenance slots---------------------------
    public long createSlot(Date startTime, double duration, String airportId, long aircraftId) throws NotFoundException, ScheduleClashException {
        if (!checkAvailability(airportId, startTime, duration)) {
            throw new ScheduleClashException();
        }
        Airport airport = em.find(Airport.class, airportId);
        Aircraft aircraft = em.find(Aircraft.class, aircraftId);
        if (airport == null || aircraft == null) throw new NotFoundException();
        if (airport.getHangars() < 1) throw new NotFoundException();
        AircraftMaintenanceSlot slot = new AircraftMaintenanceSlot();
        slot.setStartTime(startTime);
        slot.setDuration(duration);
        slot.setAirport(airport);
        slot.setAircraft(aircraft);
        if (flightScheduleBean.checkScheduleClash(aircraftId, startTime, Utils.minutesLater(startTime, (int)duration))) {
            throw new ScheduleClashException();
        }
        em.persist(slot);
        em.flush();
        return slot.getId();
    }

    public void changeSlotStartTime(long id, Date startTime) throws NotFoundException {
        AircraftMaintenanceSlot slot = em.find(AircraftMaintenanceSlot.class, id);
        if (slot == null) throw new NotFoundException();
        slot.setStartTime(startTime);
        em.persist(slot);
    }

    public void changeSlotDuration(long id, double duration) throws NotFoundException {
        AircraftMaintenanceSlot slot = em.find(AircraftMaintenanceSlot.class, id);
        if (slot == null) throw new NotFoundException();
        slot.setDuration(duration);
        em.persist(slot);
    }

    public void removeSlot(long id) throws NotFoundException {
        AircraftMaintenanceSlot slot = em.find(AircraftMaintenanceSlot.class, id);
        if (slot == null) throw new NotFoundException();
        em.remove(slot);
    }

    public AircraftMaintenanceSlot getSlot(long id) throws NotFoundException {
        AircraftMaintenanceSlot slot = em.find(AircraftMaintenanceSlot.class, id);
        if (slot == null) throw new NotFoundException();
        return slot;
    }

    public List<AircraftMaintenanceSlot> getAllSlots() {
        return em.createQuery("SELECT s from AircraftMaintenanceSlot s", AircraftMaintenanceSlot.class).getResultList();
    }

    public List<AircraftMaintenanceSlot> findSlotByAircraft(long aircraftId) throws NotFoundException {
        Aircraft aircraft = em.find(Aircraft.class, aircraftId);
        if (aircraft == null) throw new NotFoundException();
        return em.createQuery("SELECT s from AircraftMaintenanceSlot s WHERE s.aircraft = :aircraft", AircraftMaintenanceSlot.class).setParameter("aircraft", aircraft).getResultList();
    }

    public List<AircraftMaintenanceSlot> findSlotByAirport(String airportId) throws NotFoundException {
        Airport airport = em.find(Airport.class, airportId);
        if (airport == null) throw new NotFoundException();
        return em.createQuery("SELECT s from AircraftMaintenanceSlot s WHERE s.airport = :airport", AircraftMaintenanceSlot.class).setParameter("airport", airport).getResultList();
    }

    public void clearAircraftMiles(long id) throws NotFoundException {
        AircraftMaintenanceSlot slot = em.find(AircraftMaintenanceSlot.class, id);
        if (slot == null) throw new NotFoundException();
        slot.getAircraft().setMilesSinceLastMaint(0);
    }

    public boolean checkAvailability(String airportId, Date startTime, double duration) throws NotFoundException {
        Airport airport = em.find(Airport.class, airportId);
        if (airport == null) throw new NotFoundException();
        Date endTime = Utils.minutesLater(startTime, (int) duration);
        List<AircraftMaintenanceSlot> maintenanceSlots = em.createQuery("SELECT m from AircraftMaintenanceSlot m WHERE m.airport = :airport " +
                "AND ((m.startTime > :startTime AND m.startTime < :endTime) OR (m.startTime < :startTime AND FUNCTION('ADDTIME', m.startTime, m.duration) > :endTime) " +
                "OR (FUNCTION('ADDTIME', m.startTime, m.duration) > :startTime AND FUNCTION('ADDTIME', m.startTime, m.duration) < :endTime))", AircraftMaintenanceSlot.class)
                .setParameter("startTime", startTime, TemporalType.TIMESTAMP)
                .setParameter("endTime", endTime, TemporalType.TIMESTAMP)
                .setParameter("airport", airport).getResultList();
//        String debug = "(" + startTime + " - " + endTime + ")\n";
//        for (AircraftMaintenanceSlot slot : maintenanceSlots) {
//            debug = debug.concat(slot.getStartTime() + " / " + slot.getDuration() + "\n");
//        }
//        System.out.println(debug);
        return maintenanceSlots.size() < airport.getHangars();
    }
}
