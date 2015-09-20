package MAS.Bean;

import MAS.Entity.Aircraft;
import MAS.Entity.AircraftSeatConfig;
import MAS.Entity.AircraftType;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Stateless(name = "FleetEJB")
@LocalBean
public class FleetBean {
    @PersistenceContext
    private EntityManager em;

    public FleetBean() {
    }
    //-----------------AIRCRAFT---------------------------
    public long createAircraft(String tailNumber) {
        Aircraft aircraft = new Aircraft();
        aircraft.setTailNumber(tailNumber);
        em.persist(aircraft);
        em.flush();

        return aircraft.getId();
    }

    public void removeAircraft(long id) throws NotFoundException {
        Aircraft aircraft = em.find(Aircraft.class, id);
        if (aircraft == null) throw new NotFoundException();
        em.remove(aircraft);
    }

    public List<Aircraft> getAllAircraft() {
        return em.createQuery("SELECT a from Aircraft a", Aircraft.class).getResultList();
    }
    //-----------------SEAT CONFIG---------------------------
    public void setAircraftConfig(long id, long seatConfigId) throws NotFoundException {
        Aircraft aircraft = em.find(Aircraft.class, id);
        if (aircraft == null) throw new NotFoundException();
        AircraftSeatConfig seatConfig;
        seatConfig = em.find(AircraftSeatConfig.class, seatConfigId);
        if (seatConfig != null) {
            aircraft.setSeatConfig(seatConfig);
        } else
            throw new NotFoundException();
        em.persist(aircraft);
    }

    public long createAircraftSeatConfig(String seatConfig, int weight, long typeId) throws NotFoundException {
        AircraftSeatConfig aircraftSeatConfig = new AircraftSeatConfig();
        aircraftSeatConfig.setSeatConfig(seatConfig);
        aircraftSeatConfig.setWeight(weight);
        AircraftType aircraftType = em.find(AircraftType.class, typeId);
        if (aircraftType == null) throw new NotFoundException();
        aircraftSeatConfig.setAircraftType(aircraftType);

        em.persist(aircraftSeatConfig);
        em.flush();
        return aircraftSeatConfig.getId();
    }

    public void removeAircraftSeatConfig(long id) throws NotFoundException {
        AircraftSeatConfig aircraftSeatConfig = em.find(AircraftSeatConfig.class, id);
        if (aircraftSeatConfig == null) throw new NotFoundException();
        //Check whether any aircraft have this config -> if have, disable removing of this seat config
        em.remove(aircraftSeatConfig);
    }

    public void changeWeight(long id, int weight) throws NotFoundException {
        AircraftSeatConfig aircraftSeatConfig = em.find(AircraftSeatConfig.class, id);
        if (aircraftSeatConfig == null) throw new NotFoundException();
        aircraftSeatConfig.setWeight(weight);
        em.persist(aircraftSeatConfig);
    }

    public List<AircraftSeatConfig> getAllAircraftSeatConfigs() {
        return em.createQuery("SELECT a from AircraftSeatConfig a", AircraftSeatConfig.class).getResultList();
    }
    //-----------------AIRCRAFT TYPES/EQUIPMENT---------------------------
    public long createAircraftType(String name, int fuelCapacity) {
        AircraftType aircraftType = new AircraftType();
        aircraftType.setName(name);
        aircraftType.setFuelCapacity(fuelCapacity);
        em.persist(aircraftType);
        em.flush();

        return aircraftType.getId();
    }

    public void removeAircraftType(long id) throws NotFoundException {
        AircraftType aircraftType = em.find(AircraftType.class, id);
        if (aircraftType == null) throw new NotFoundException();
        //Check whether any seat configs have this type -> if have, disable removing of this type
        em.remove(aircraftType);
    }

    public void changeFuelCapacity(long id, int fuelCapacity) throws NotFoundException {
        AircraftType aircraftType = em.find(AircraftType.class, id);
        if (aircraftType == null) throw new NotFoundException();
        aircraftType.setFuelCapacity(fuelCapacity);
        em.persist(aircraftType);
    }

    public void changeAircraftTypeName(long id, String newName) throws NotFoundException {
        AircraftType aircraftType = em.find(AircraftType.class, id);
        if (aircraftType == null) throw new NotFoundException();
        aircraftType.setName(newName);
        em.persist(aircraftType);
    }

    public List<AircraftType> getAllAircraftTypes() {
        return em.createQuery("SELECT a from AircraftType a", AircraftType.class).getResultList();
    }
}
