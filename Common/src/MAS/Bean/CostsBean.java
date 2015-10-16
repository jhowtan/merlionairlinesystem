package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.SeatConfigObject;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;
import MAS.ScheduleDev.HypoAircraftAssignment;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless(name = "CostsEJB")
@LocalBean
public class CostsBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    AttributesBean attributesBean;
    @EJB
    FleetBean fleetBean;

    public CostsBean() {
    }

    public long createCost(int type, double amount, String comments, long assocId) throws NotFoundException {
        Cost cost = new Cost();
        cost.setType(type);
        cost.setAmount(amount);
        cost.setComments(comments);
        if (type == Constants.COST_PER_AIRCRAFT || type == Constants.COST_PER_MAINTENANCE) {
            Aircraft aircraft = em.find(Aircraft.class, assocId);
            if (aircraft == null) {
                if (assocId == -1)
                    cost.setAssocId(-1);
                else
                    throw new NotFoundException();
            }
            else {
                cost.setAssocId(aircraft.getId());
            }
        }
        else if (type == Constants.COST_PER_FLIGHT) {
            AircraftAssignment aircraftAssignment = em.find(AircraftAssignment.class, assocId);
            if (aircraftAssignment == null)
            {
                if (assocId == -1)
                    cost.setAssocId(-1);
                else
                    throw new NotFoundException();
            }
            else {
                cost.setAssocId(aircraftAssignment.getId());
            }
        }
        cost.setDate(new Date());
        em.persist(cost);
        em.flush();

        return cost.getId();
    }

    public void removeCost(long id) throws NotFoundException {
        Cost cost = em.find(Cost.class, id);
        if (cost == null) throw new NotFoundException();
        em.remove(cost);
    }

    public void changeCostComments(long id, String comments) throws NotFoundException {
        Cost cost = em.find(Cost.class, id);
        if (cost == null) throw new NotFoundException();
        cost.setComments(comments);
        em.persist(cost);
    }

    public void changeCostAmount(long id, double amount) throws NotFoundException {
        Cost cost = em.find(Cost.class, id);
        if (cost == null) throw new NotFoundException();
        cost.setAmount(amount);
        em.persist(cost);
    }

    public Cost getCost(long id) throws NotFoundException {
        Cost cost = em.find(Cost.class, id);
        if (cost == null) throw new NotFoundException();
        return cost;
    }

    public List<Cost> getAllCostOfType(int type) {
        return em.createQuery("SELECT c from Cost c WHERE c.type = :type").setParameter("type", type).getResultList();
    }

    public void calculateCostEstimate(HypoAircraftAssignment hypothetical) {
        AircraftAssignment aircraftAssignment = hypothetical.aircraftAssignment;
        long acId = aircraftAssignment.getAircraft().getId();
        double result = 0;

        //Aircraft costs divided into per flight
        List<Cost> acDLife = em.createQuery("SELECT c from Cost c WHERE c.type = :type AND c.assocId = :acId OR c.type = :type AND c.assocId = -1")
                .setParameter("type", Constants.COST_PER_AIRCRAFT)
                .setParameter("acId", acId).getResultList();
        double acAll = addAllInList(acDLife);
        acAll = acAll/(attributesBean.getIntAttribute(Constants.AIRCRAFT_EXPECTED_LIFE, 25) * attributesBean.getIntAttribute(Constants.FLIGHTS_PER_YEAR, 100));

        //Fuel costs
        double totalFuelCosts = calculateFuelCost(aircraftAssignment.getAircraft(), aircraftAssignment.getRoute().getDistance());

        //Maintenance costs divided by flights
        List<Cost> maintDLife = em.createQuery("SELECT c from Cost c WHERE c.type = :type AND c.assocId = :acId OR c.type = :type AND c.assocId = -1")
                .setParameter("type", Constants.COST_PER_MAINTENANCE)
                .setParameter("acId", acId).getResultList();
        double allMaint = addAllInList(maintDLife) * attributesBean.getIntAttribute(Constants.MAINTENANCE_PER_YEAR, 15);
        allMaint = allMaint / attributesBean.getIntAttribute(Constants.FLIGHTS_PER_YEAR, 100);

        //Salary considerations
        double cockpitSalary = attributesBean.getDoubleAttribute(Constants.COCKPIT_CREW_SALARY, 200);
        double cabinSalary = attributesBean.getDoubleAttribute(Constants.CABIN_CREW_SALARY, 100);
        double totalSalary = cockpitSalary * aircraftAssignment.getAircraft().getSeatConfig().getAircraftType().getCockpitCrewReq();
        totalSalary += cabinSalary * aircraftAssignment.getAircraft().getSeatConfig().getAircraftType().getCabinCrewReq();

        result += acAll;
        result += totalFuelCosts;
        result += allMaint;
        result += totalSalary;
        hypothetical.cost = result;
    }

    public double calculateCostPerFlight(long flightId) throws NotFoundException {
        Flight flight = em.find(Flight.class, flightId);
        if (flight == null) throw  new NotFoundException();
        long acId = flight.getAircraftAssignment().getAircraft().getId();
        long aaId = flight.getAircraftAssignment().getId();
        double result = 0;
        //All consumables per flight
        List<Cost> costPFlight = em.createQuery("SELECT c from Cost c WHERE c.type = :type AND c.assocId = :aaId OR  c.type = :type AND c.assocId = -1", Cost.class)
                .setParameter("type", Constants.COST_PER_FLIGHT)
                .setParameter("aaId", aaId).getResultList();
        double aaAll = addAllInList(costPFlight);
        //Aircraft costs divided into per flight
        List<Cost> acDLife = em.createQuery("SELECT c from Cost c WHERE c.type = :type AND c.assocId = :acId OR c.type = :type AND c.assocId = -1", Cost.class)
                .setParameter("type", Constants.COST_PER_AIRCRAFT)
                .setParameter("acId", acId).getResultList();
        double acAll = addAllInList(acDLife);
        acAll = acAll/(attributesBean.getIntAttribute(Constants.AIRCRAFT_EXPECTED_LIFE, 25) * attributesBean.getIntAttribute(Constants.FLIGHTS_PER_YEAR, 100));
        //Annual costs divided into per flight
        List<Cost> annualDLife = em.createQuery("SELECT c from Cost c WHERE c.type = :type", Cost.class)
                .setParameter("type", Constants.COST_ANNUAL).getResultList();
        double annualAll = addAllInList(annualDLife)/(fleetBean.getAllAircraft().size() * attributesBean.getIntAttribute(Constants.FLIGHTS_PER_YEAR, 100));
        //Fuel costs
        double totalFuelCosts = calculateFuelCost(flight.getAircraftAssignment().getAircraft(), flight.getAircraftAssignment().getRoute().getDistance());

        //Maintenance costs divided by flights
        List<Cost> maintDLife = em.createQuery("SELECT c from Cost c WHERE c.type = :type AND c.assocId = :acId OR c.type = :type AND c.assocId = -1", Cost.class)
                .setParameter("type", Constants.COST_PER_MAINTENANCE)
                .setParameter("acId", acId).getResultList();
        double allMaint = addAllInList(maintDLife) * attributesBean.getIntAttribute(Constants.MAINTENANCE_PER_YEAR, 15);
        allMaint = allMaint / attributesBean.getIntAttribute(Constants.FLIGHTS_PER_YEAR, 100);
        //Salary considerations
        double cockpitSalary = attributesBean.getDoubleAttribute(Constants.COCKPIT_CREW_SALARY, 200);
        double cabinSalary = attributesBean.getDoubleAttribute(Constants.CABIN_CREW_SALARY, 100);
        double totalSalary = cockpitSalary * flight.getAircraftAssignment().getAircraft().getSeatConfig().getAircraftType().getCockpitCrewReq();
        totalSalary += cabinSalary * flight.getAircraftAssignment().getAircraft().getSeatConfig().getAircraftType().getCabinCrewReq();

        result += aaAll;
        result += acAll;
        result += annualAll;
        result += totalFuelCosts;
        result += allMaint;
        result += totalSalary;
        return result;
    }

    private double calculateFuelCost(Aircraft aircraft, double distanceTraveled) {
        //Fuel costs
        List<Cost> fuelCosts = em.createQuery("SELECT c from Cost c WHERE c.type = :type ORDER BY c.id DESC", Cost.class)
                .setParameter("type", Constants.COST_FUEL).setMaxResults(1).getResultList();
        double fuelCost = fuelCosts.get(0).getAmount();
        //Dry weight
        double aircraftWeight = aircraft.getSeatConfig().getWeight() + aircraft.getSeatConfig().getAircraftType().getWeight();
        SeatConfigObject seatConfigObject = new SeatConfigObject();
        seatConfigObject.parse(aircraft.getSeatConfig().getSeatConfig());
        int totalSeats = seatConfigObject.getSeatsInClass(0) + seatConfigObject.getSeatsInClass(1) + seatConfigObject.getSeatsInClass(2) + seatConfigObject.getSeatsInClass(3);
        //Adding number of seats * people and their baggage (Assume always prepare for worst case scenario)
        aircraftWeight += totalSeats * (attributesBean.getDoubleAttribute(Constants.AVERAGE_PERSON_WEIGHT, 62) + attributesBean.getDoubleAttribute(Constants.AVERAGE_BAGGAGE_WEIGHT, 30));
        double fuelEfficiency = aircraft.getSeatConfig().getAircraftType().getFuelEfficiency() / 1000;
        double fuelRequiredPrev = aircraftWeight * distanceTraveled * fuelEfficiency;
        double fuelRequired = 0;
        double aircraftWeightWithFuel = 0;
        do {
            fuelRequiredPrev = fuelRequired;
            aircraftWeightWithFuel = aircraftWeight + fuelRequiredPrev * attributesBean.getDoubleAttribute(Constants.FUEL_WEIGHT, 0.81) / 2;
            fuelRequired = fuelEfficiency * distanceTraveled * aircraftWeightWithFuel;
        } while (fuelRequired - fuelRequiredPrev > (aircraftWeight * 0.05));
        return fuelCost * fuelRequired;
    }

    private double addAllInList(List<Cost> costs) {
        double result = 0;
        for (int i = 0; i < costs.size(); i++) {
            result += costs.get(i).getAmount();
        }
        return result;
    }
}
