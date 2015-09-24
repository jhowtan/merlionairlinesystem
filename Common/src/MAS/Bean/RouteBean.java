package MAS.Bean;

import MAS.Entity.*;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static java.lang.Math.*;

@Stateless(name = "RouteEJB")
@LocalBean
public class RouteBean {

    private static final double EarthRadius = 6373;

    @PersistenceContext
    private EntityManager em;

    public RouteBean() {
    }

    //-----------------AIRPORTS, CITIES, COUNTRIES---------------------------
    public long createAirport(String name, double latitude,
                              double longitude, String code, int hangars, long cityId) throws NotFoundException{
        Airport airport = new Airport();
        airport.setName(name);
        City city = em.find(City.class, cityId);
        if (city == null) throw new NotFoundException();
        airport.setCity(city);
        airport.setHangars(hangars);
        airport.setCode(code);
        airport.setLatitude(latitude);
        airport.setLongitude(longitude);
        em.persist(airport);
        em.flush();
        return airport.getId();
    }

    public void removeAirport(long id) throws NotFoundException {
        Airport airport = em.find(Airport.class, id);
        if (airport == null) throw new NotFoundException();
        em.remove(airport);
    }

    public void changeAirportCode(long id, String code) throws NotFoundException {
        Airport airport = em.find(Airport.class, id);
        if (airport == null) throw new NotFoundException();
        airport.setCode(code);
        em.persist(airport);
    }

    public void changeAirportName(long id, String newName) throws NotFoundException {
        Airport airport = em.find(Airport.class, id);
        if (airport == null) throw new NotFoundException();
        airport.setName(newName);
        em.persist(airport);
    }

    public Airport getAirport(long id) throws NotFoundException {
        Airport airport = em.find(Airport.class, id);
        if (airport == null) throw new NotFoundException();
        return airport;
    }

    public long createCity(String name, long countryId) throws NotFoundException {
        City city = new City();
        city.setName(name);
        Country country = em.find(Country.class, countryId);
        if (country == null) throw new NotFoundException();
        city.setCountry(country);
        em.persist(city);
        em.flush();

        return city.getId();
    }

    public void removeCity(long id) throws NotFoundException {
        City city = em.find(City.class, id);
        if (city == null) throw new NotFoundException();
        //Check for airports in this city
        em.remove(city);
    }

    public City getCity(long id) throws NotFoundException {
        City city = em.find(City.class, id);
        if (city == null) throw new NotFoundException();
        return city;
    }

    public List<City> getAllCities() {
        return em.createQuery("SELECT c from City c", City.class).getResultList();
    }

    public long createCountry(String name, String code) {
        Country country = new Country();
        country.setName(name);
        country.setCode(code);
        em.persist(country);
        em.flush();
        return country.getId();
    }

    public void removeCountry(long id) throws NotFoundException {
        Country country = em.find(Country.class, id);
        if (country == null) throw new NotFoundException();
        //Check for cities in this country
        em.remove(country);
    }

    public Country getCountry(long id) throws NotFoundException {
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
        return (Airport) em.createQuery("SELECT a FROM Airport a WHERE a.code = :code").setParameter("code", code.toLowerCase()).getSingleResult();
    }

    //-----------------ROUTES---------------------------
    public long createRoute(long originId, long destinationId) throws NotFoundException {
        Route route = new Route();
        Airport origin = em.find(Airport.class, originId);
        Airport destination = em.find(Airport.class, destinationId);
        if (origin == null || destination == null) throw new NotFoundException();
        route.setOrigin(origin);
        route.setDestination(destination);
        route.setDistance(calcDist(origin.getLatitude(), origin.getLongitude(),
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

    public List<Route> findRouteByOrigin(long airportId) throws NotFoundException {
        return em.createQuery("SELECT r from Route r WHERE r.origin = :airportId", Route.class).setParameter("airportId", airportId).getResultList();
    }

    public List<Route> findRouteByDest(long airportId) throws NotFoundException {
        return em.createQuery("SELECT r from Route r WHERE r.destination = :airportId", Route.class).setParameter("airportId", airportId).getResultList();
    }

    public List<Route> getAllRoutes() {
        return em.createQuery("SELECT r from Route r", Route.class).getResultList();
    }

    public Route getRoute(long id) throws NotFoundException {
        Route route = em.find(Route.class, id);
        if (route == null) throw new NotFoundException();
        return route;
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

    public void removeAircraftAssignment(long id) throws NotFoundException {
        AircraftAssignment aircraftAssignment = em.find(AircraftAssignment.class, id);
        if (aircraftAssignment == null) throw new NotFoundException();
        em.remove(aircraftAssignment);
    }

    public List<AircraftAssignment> findAAByAircraft(long aircraftId) throws NotFoundException {
        return em.createQuery("SELECT a from AircraftAssignment a WHERE a.aircraft = :aircraftId", AircraftAssignment.class).setParameter("aircraftId", aircraftId).getResultList();
    }

    public List<AircraftAssignment> findAAByRoute(long routeId) throws NotFoundException {
        return em.createQuery("SELECT a from AircraftAssignment a WHERE a.route = :routeId", AircraftAssignment.class).setParameter("routeId", routeId).getResultList();
    }

    public List<AircraftAssignment> getAllAircraftAssignments() {
        return em.createQuery("SELECT a from AircraftAssignment a", AircraftAssignment.class).getResultList();
    }

    public AircraftAssignment getAircraftAssignment(long id) throws NotFoundException {
        AircraftAssignment aircraftAssignment = em.find(AircraftAssignment.class, id);
        if (aircraftAssignment == null) throw new NotFoundException();
        return aircraftAssignment;
    }
    //-----------------OTHERS---------------------------
    public double calcDist(double oriLat, double oriLong, double destLat, double destLong) {
        double dLong = destLong - oriLong;
        double dLat = destLat - oriLat;
        double a = pow(sin(dLat / 2), 2) + cos(oriLat) * cos(destLat) * pow(sin(dLong / 2), 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return EarthRadius * c;
    }
}
