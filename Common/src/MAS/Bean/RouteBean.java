package MAS.Bean;

import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.IntegrityConstraintViolationException;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "RouteEJB")
@LocalBean
public class RouteBean {

    @PersistenceContext
    private EntityManager em;

    public RouteBean() {
    }

    public Airport createAirport(String id, String name, String cityId, double latitude, double longitude, int hangars) throws NotFoundException {
        Airport airport = new Airport();
        airport.setId(id);
        airport.setName(name);
        City city = em.find(City.class, cityId);
        if (city == null) throw new NotFoundException();
        airport.setCity(city);
        airport.setHangars(hangars);
        airport.setLatitude(latitude);
        airport.setLongitude(longitude);
        em.persist(airport);
        em.flush();
        return airport;
    }

    public void updateAirport(Airport airport) throws NotFoundException {
        if (airport.getId() == null || em.find(Airport.class, airport.getId()) == null) throw new NotFoundException();
        em.merge(airport);
    }

    public void removeAirport(String id) throws NotFoundException {
        Airport airport = em.find(Airport.class, id);
        if (airport == null) throw new NotFoundException();
        em.remove(airport);
    }

    public Airport getAirport(String id) throws NotFoundException {
        if (id == null) throw new NotFoundException();
        id = id.toUpperCase();
        Airport airport = em.find(Airport.class, id);
        if (airport == null) throw new NotFoundException();
        return airport;
    }

    public String createCity(String id, String name, String countryId, String timezone) throws NotFoundException {
        City city = new City();
        city.setId(id);
        city.setName(name);
        Country country = em.find(Country.class, countryId);
        if (country == null) throw new NotFoundException();
        city.setCountry(country);
        city.setTimezone(timezone);
        em.persist(city);
        em.flush();

        return city.getId();
    }

    public void removeCity(String id) throws NotFoundException {
        City city = em.find(City.class, id);
        if (city == null) throw new NotFoundException();
        em.remove(city);
    }

    public City getCity(String id) throws NotFoundException {
        City city = em.find(City.class, id);
        if (city == null) throw new NotFoundException();
        return city;
    }

    public List<City> getAllCities() {
        return em.createQuery("SELECT c from City c", City.class).getResultList();
    }

    public Country createCountry(String id, String name) {
        Country country = new Country();
        country.setId(id);
        country.setName(name);
        em.persist(country);
        em.flush();
        return country;
    }

    public void removeCountry(String id) throws NotFoundException {
        Country country = em.find(Country.class, id);
        if (country == null) throw new NotFoundException();
        em.remove(country);
    }

    public Country getCountry(String id) throws NotFoundException {
        Country country = em.find(Country.class, id);
        if (country == null) throw new NotFoundException();
        return country;
    }

    public List<Country> getAllCountries() {
        return em.createQuery("SELECT c from Country c", Country.class).getResultList();
    }

    public List<Airport> getAllAirports() {
        return em.createQuery("SELECT a from Airport a", Airport.class).getResultList();
    }

    public Airport findAirportByName(String name) throws NotFoundException {
        return (Airport) em.createQuery("SELECT a FROM Airport a WHERE a.name = :name").setParameter("name", name.toLowerCase()).getSingleResult();
    }

    public Airport findAirportByCode(String code) throws NotFoundException {
        return (Airport) em.createQuery("SELECT a FROM Airport a WHERE a.id = :code").setParameter("code", code.toLowerCase()).getSingleResult();
    }

    //-----------------ROUTES---------------------------
    public long createRoute(String originId, String destinationId) throws NotFoundException {
        Route route = new Route();
        Airport origin = em.find(Airport.class, originId);
        Airport destination = em.find(Airport.class, destinationId);
        if (origin == null || destination == null) throw new NotFoundException();
        route.setOrigin(origin);
        route.setDestination(destination);
        route.setDistance(Utils.calculateDistance(origin.getLatitude(), origin.getLongitude(),
                destination.getLatitude(), destination.getLongitude()));
        em.persist(route);
        em.flush();
        return route.getId();
    }

    public long updateRoute(long routeId, String originId, String destinationId) throws NotFoundException {
        Route route = em.find(Route.class, routeId);
        if (route == null) throw new NotFoundException();
        Airport origin = em.find(Airport.class, originId);
        Airport destination = em.find(Airport.class, destinationId);
        if (origin == null || destination == null) throw new NotFoundException();
        route.setOrigin(origin);
        route.setDestination(destination);
        route.setDistance(Utils.calculateDistance(origin.getLatitude(), origin.getLongitude(),
                destination.getLatitude(), destination.getLongitude()));
        em.persist(route);
        em.flush();
        return route.getId();
    }

    public void removeRoute(long id) throws NotFoundException {
        Route route = em.find(Route.class, id);
        if (route == null) throw new NotFoundException();
        //Check for assignments on this route
        em.remove(route);
    }

    public Route matchRoute(Route route) throws NotFoundException {
        Airport origin = route.getOrigin();
        Airport destination = route.getDestination();
        Route result = em.createQuery("SELECT r from Route r WHERE r.origin = :origin AND r.destination = :destination", Route.class)
                .setParameter("origin", origin)
                .setParameter("destination", destination).setMaxResults(1).getResultList().get(0);
        if (result == null)
            throw new NotFoundException();
        else
            return result;
    }

    public List<Route> findRouteByOrigin(String airportId) throws NotFoundException {
        Airport airport = em.find(Airport.class, airportId);
        if (airport == null) throw new NotFoundException();
        return em.createQuery("SELECT r from Route r WHERE r.origin = :airport", Route.class).setParameter("airport", airport).getResultList();
    }

    public List<Route> findRouteByDest(String airportId) throws NotFoundException {
        Airport airport = em.find(Airport.class, airportId);
        if (airport == null) throw new NotFoundException();
        return em.createQuery("SELECT r from Route r WHERE r.destination = :airport", Route.class).setParameter("airport", airport).getResultList();
    }

    public Route findRouteByOriginAndDestination(String originId, String destinationId) throws NotFoundException {
        Airport origin = em.find(Airport.class, originId);
        Airport destination = em.find(Airport.class, destinationId);
        if (origin == null || destination == null) throw new NotFoundException();
        try {
            return em.createQuery("SELECT r from Route r WHERE r.origin = :origin AND r.destination = :destination", Route.class)
                    .setParameter("origin", origin)
                    .setParameter("destination", destination).getSingleResult();
        } catch (Exception e) {
            throw new NotFoundException();
        }
    }

    public List<Route> getAllRoutes() {
        return em.createQuery("SELECT r from Route r", Route.class).getResultList();
    }

    public Route getRoute(long id) throws NotFoundException {
        Route route = em.find(Route.class, id);
        if (route == null) throw new NotFoundException();
        return route;
    }

    public List<Airport> getDestinationByOrigin(Airport origin) {
        List<Airport> directFlightDestinations =
                em.createQuery("SELECT r.destination FROM Route r WHERE r.origin = :origin", Airport.class)
                .setParameter("origin", origin)
                .getResultList();

        List<Airport> connectingFlightDestinations =
                em.createQuery("SELECT r2.destination FROM Route r1, Route r2 WHERE r1.origin = :origin AND r1.destination = r2.origin AND r1.origin <> r2.destination", Airport.class)
                .setParameter("origin", origin)
                .getResultList();

        ArrayList<Airport> allFlightDestinationsByOrigin = new ArrayList<>();
        allFlightDestinationsByOrigin.addAll(directFlightDestinations);
        for (Airport airport : connectingFlightDestinations) {
            if (!allFlightDestinationsByOrigin.contains(airport)) {
                allFlightDestinationsByOrigin.add(airport);
            }
        }
        return allFlightDestinationsByOrigin;
    }

    //-----------------AIRCRAFT ASSIGNMENTS---------------------------
    public long createAircraftAssignment (long aircraftId, long routeId) throws NotFoundException {
        AircraftAssignment aircraftAssignment = new AircraftAssignment();
        Aircraft aircraft = em.find(Aircraft.class, aircraftId);
        Route route = em.find(Route.class, routeId);
        if (aircraft == null || route == null) throw new NotFoundException();
        aircraftAssignment.setAircraft(aircraft);
        aircraftAssignment.setRoute(route);
        em.persist(aircraftAssignment);
        em.flush();
        return aircraftAssignment.getId();
    }

    public void removeAircraftAssignment(long id) throws NotFoundException, IntegrityConstraintViolationException {
        AircraftAssignment aircraftAssignment = em.find(AircraftAssignment.class, id);
        if (aircraftAssignment == null) throw new NotFoundException();
        try {
            em.remove(aircraftAssignment);
            em.flush();
        } catch (Exception e) {
            throw new IntegrityConstraintViolationException();
        }
    }

    public AircraftAssignment findAAByAcAndRoute(long aircraftId, long routeId) throws NotFoundException {
        Aircraft aircraft = em.find(Aircraft.class, aircraftId);
        if (aircraft == null) throw new NotFoundException();
        Route route = em.find(Route.class, routeId);
        if (route == null) throw new NotFoundException();
        List<AircraftAssignment> result = em.createQuery("SELECT a from AircraftAssignment a WHERE a.aircraft = :aircraft AND a.route = :route", AircraftAssignment.class)
                .setParameter("aircraft", aircraft)
                .setParameter("route", route).setMaxResults(1).getResultList();
        if (result.size() == 0)
            throw new NotFoundException();
        else
            return result.get(0);
    }

    public List<AircraftAssignment> findAAByAircraft(long aircraftId) throws NotFoundException {
        Aircraft aircraft = em.find(Aircraft.class, aircraftId);
        if (aircraft == null) throw new NotFoundException();
        return em.createQuery("SELECT a from AircraftAssignment a WHERE a.aircraft = :aircraft", AircraftAssignment.class).setParameter("aircraft", aircraftId).getResultList();
    }

    public List<AircraftAssignment> findAAByRoute(long routeId) throws NotFoundException {
        Route route = em.find(Route.class, routeId);
        if (route == null) throw new NotFoundException();
        return em.createQuery("SELECT a from AircraftAssignment a WHERE a.route = :route", AircraftAssignment.class).setParameter("route", route).getResultList();
    }

    public List<AircraftAssignment> getAllAircraftAssignments() {
        return em.createQuery("SELECT a from AircraftAssignment a", AircraftAssignment.class).getResultList();
    }

    public AircraftAssignment getAircraftAssignment(long id) throws NotFoundException {
        AircraftAssignment aircraftAssignment = em.find(AircraftAssignment.class, id);
        if (aircraftAssignment == null) throw new NotFoundException();
        return aircraftAssignment;
    }

    public int getEstimatedDuration (long aaId) throws NotFoundException {
        AircraftAssignment aircraftAssignment = em.find(AircraftAssignment.class, aaId);
        if (aircraftAssignment == null) throw new NotFoundException();
        return (int)((aircraftAssignment.getRoute().getDistance() / aircraftAssignment.getAircraft().getSeatConfig().getAircraftType().getSpeed()) * 60);
    }

}
