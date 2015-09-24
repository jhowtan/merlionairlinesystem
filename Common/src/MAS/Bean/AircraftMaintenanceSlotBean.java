package MAS.Bean;

import MAS.Entity.Aircraft;
import MAS.Entity.AircraftMaintenanceSlot;
import MAS.Entity.Airport;
import MAS.Exception.NotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless(name = "AircraftMaintenanceSlotEJB")
public class AircraftMaintenanceSlotBean {
    @PersistenceContext
    EntityManager em;

    public AircraftMaintenanceSlotBean() {
    }

    //-----------------Maintenance slots---------------------------
    public long createSlot(Date startTime, double duration, long airportId, long aircraftId) throws NotFoundException {
        Airport airport = em.find(Airport.class, airportId);
        Aircraft aircraft = em.find(Aircraft.class, aircraftId);
        if (airport == null || aircraft == null) throw new NotFoundException();
        AircraftMaintenanceSlot slot = new AircraftMaintenanceSlot();
        slot.setStartTime(startTime);
        slot.setDuration(duration);
        slot.setAirport(airport);
        slot.setAircraft(aircraft);
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

    public List<AircraftMaintenanceSlot> findSlotByAirport(long airportId) throws NotFoundException {
        Airport airport = em.find(Airport.class, airportId);
        if (airport == null) throw new NotFoundException();
        return em.createQuery("SELECT s from AircraftMaintenanceSlot s WHERE s.airport = :airport", AircraftMaintenanceSlot.class).setParameter("airport", airport).getResultList();
    }
}
