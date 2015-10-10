package MAS.Bean;

import MAS.Entity.Aircraft;
import MAS.Entity.AircraftSeatConfig;
import MAS.Entity.AircraftType;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;


@Stateless(name = "FleetEJB")
@LocalBean
public class FleetBean {
    @PersistenceContext
    private EntityManager em;

    public FleetBean() {
    }
    //-----------------AIRCRAFT---------------------------
    public long createAircraft(String tailNumber, Date manufacturedDate) {
        Aircraft aircraft = new Aircraft();
        aircraft.setTailNumber(tailNumber);
        aircraft.setManufacturedDate(manufacturedDate);
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

    public Aircraft getAircraft(long id) throws NotFoundException {
        Aircraft aircraft = em.find(Aircraft.class, id);
        if (aircraft == null) throw new NotFoundException();
        return aircraft;
    }

    public void changeAircraftConfig(long id, long seatConfigId) throws NotFoundException {
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

    //-----------------SEAT CONFIG---------------------------
    public long createAircraftSeatConfig(String seatConfig, String name, int weight, long typeId) throws NotFoundException {
        AircraftSeatConfig aircraftSeatConfig = new AircraftSeatConfig();
        aircraftSeatConfig.setSeatConfig(seatConfig);
        aircraftSeatConfig.setWeight(weight);
        aircraftSeatConfig.setName(name);
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

    public void changeSeatConfigWeight(long id, int weight) throws NotFoundException {
        AircraftSeatConfig aircraftSeatConfig = em.find(AircraftSeatConfig.class, id);
        if (aircraftSeatConfig == null) throw new NotFoundException();
        aircraftSeatConfig.setWeight(weight);
        em.persist(aircraftSeatConfig);
    }

    public void changeName(long id, String newName) throws NotFoundException {
        AircraftSeatConfig aircraftSeatConfig = em.find(AircraftSeatConfig.class, id);
        if (aircraftSeatConfig == null) throw new NotFoundException();
        aircraftSeatConfig.setName(newName);
        em.persist(aircraftSeatConfig);
    }

    public AircraftSeatConfig getAircraftSeatConfig(long id) throws NotFoundException {
        AircraftSeatConfig aircraftSeatConfig = em.find(AircraftSeatConfig.class, id);
        if (aircraftSeatConfig == null) throw new NotFoundException();
        return aircraftSeatConfig;
    }

    public List<AircraftSeatConfig> getAllAircraftSeatConfigs() {
        return em.createQuery("SELECT a from AircraftSeatConfig a", AircraftSeatConfig.class).getResultList();
    }
    //-----------------AIRCRAFT TYPES/EQUIPMENT---------------------------
    public long createAircraftType(String name, int fuelCapacity,
                                   int cabinReq, int cockpitReq, double fuelEfficiency, double speed, int weight) {
        AircraftType aircraftType = new AircraftType();
        aircraftType.setName(name);
        aircraftType.setFuelCapacity(fuelCapacity);
        aircraftType.setCabinCrewReq(cabinReq);
        aircraftType.setCockpitCrewReq(cockpitReq);
        aircraftType.setFuelEfficiency(fuelEfficiency);
        aircraftType.setSpeed(speed);
        aircraftType.setWeight(weight);
        em.persist(aircraftType);
        em.flush();

        return aircraftType.getId();
    }

    public AircraftType getAircraftType(long id) throws NotFoundException {
        AircraftType aircraftType = em.find(AircraftType.class, id);
        if (aircraftType == null) throw new NotFoundException();
        return aircraftType;
    }


    public void removeAircraftType(long id) throws NotFoundException {
        AircraftType aircraftType = em.find(AircraftType.class, id);
        if (aircraftType == null) throw new NotFoundException();
        //Check whether any seat configs have this type -> if have, disable removing of this type
        em.remove(aircraftType);
    }

    public void changeAircraftTypeFuelCap(long id, int fuelCapacity) throws NotFoundException {
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

    public void changeAircraftTypeWeight(long id, int weight) throws NotFoundException {
        AircraftType aircraftType = em.find(AircraftType.class, id);
        if (aircraftType == null) throw new NotFoundException();
        aircraftType.setWeight(weight);
        em.persist(aircraftType);
    }

    public void changeAircraftTypeSpeed(long id, double speed) throws NotFoundException {
        AircraftType aircraftType = em.find(AircraftType.class, id);
        if (aircraftType == null) throw new NotFoundException();
        aircraftType.setSpeed(speed);
        em.persist(aircraftType);
    }

    public void changeAircraftTypeFuelEff(long id, double fuelEfficiency) throws NotFoundException {
        AircraftType aircraftType = em.find(AircraftType.class, id);
        if (aircraftType == null) throw new NotFoundException();
        aircraftType.setFuelEfficiency(fuelEfficiency);
        em.persist(aircraftType);
    }

    public void changeAircraftTypeCabinReq(long id, int cabinReq) throws NotFoundException {
        AircraftType aircraftType = em.find(AircraftType.class, id);
        if (aircraftType == null) throw new NotFoundException();
        aircraftType.setCabinCrewReq(cabinReq);
        em.persist(aircraftType);
    }

    public void changeAircraftTypeCockpitReq(long id, int cockpitReq) throws NotFoundException {
        AircraftType aircraftType = em.find(AircraftType.class, id);
        if (aircraftType == null) throw new NotFoundException();
        aircraftType.setCockpitCrewReq(cockpitReq);
        em.persist(aircraftType);
    }

    public boolean isTailNumberUnique(String tailNum) {
        return (Long) em.createQuery("SELECT COUNT(a) FROM Aircraft a WHERE a.tailNumber = :tailNum").setParameter("tailNum", tailNum).getSingleResult() == 0;
    }

    public boolean isAircraftTypeNameUnique(String name) {
        return (Long) em.createQuery("SELECT COUNT(at) FROM AircraftType at WHERE at.name = :name").setParameter("name", name).getSingleResult() == 0;
    }

    public List<AircraftType> getAllAircraftTypes() {
        return em.createQuery("SELECT a from AircraftType a", AircraftType.class).getResultList();
    }

    public List<AircraftSeatConfig> findSeatConfigByType(long typeId) {
        AircraftType aircraftType = em.find(AircraftType.class, typeId);
        return em.createQuery("SELECT sc from AircraftSeatConfig sc WHERE sc.aircraftType = :aircraftType",
                AircraftSeatConfig.class)
                .setParameter("aircraftType", aircraftType)
                .getResultList();
    }

    public Long getAircraftCountByType(long typeId) {
        AircraftType aircraftType = em.find(AircraftType.class, typeId);
        return (Long) em.createQuery("SELECT COUNT(ac) from AircraftSeatConfig sc, Aircraft ac WHERE sc.aircraftType = :aircraftType AND ac.seatConfig = sc")
                .setParameter("aircraftType", aircraftType)
                .getSingleResult();
    }

}
