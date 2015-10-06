package MAS.Structure;

import MAS.Common.Permissions;

public class Pages {
    public static final Page DASHBOARD = new Page("/App/index");
    
	// Common Infrastructure: System Admin
    public static final Page LIST_USERS = new Page("/App/SystemAdmin/users", Permissions.MANAGE_USERS);
    public static final Page CREATE_USER = new Page("/App/SystemAdmin/createUser", Permissions.MANAGE_USERS);
    public static final Page UPDATE_USER = new Page("/App/SystemAdmin/updateUser", Permissions.MANAGE_USERS);

    public static final Page LIST_ROLES = new Page("/App/SystemAdmin/roles", Permissions.MANAGE_ROLES);
    public static final Page CREATE_ROLE = new Page("/App/SystemAdmin/createRole", Permissions.MANAGE_ROLES);
    public static final Page UPDATE_ROLE = new Page("/App/SystemAdmin/updateRole", Permissions.MANAGE_ROLES);

    public static final Page LIST_WORKGROUPS = new Page("/App/SystemAdmin/workgroups", Permissions.MANAGE_WORKGROUPS);
    public static final Page CREATE_WORKGROUP = new Page("/App/SystemAdmin/createWorkgroup", Permissions.MANAGE_WORKGROUPS);
    public static final Page UPDATE_WORKGROUP = new Page("/App/SystemAdmin/updateWorkgroup", Permissions.MANAGE_WORKGROUPS);
	
    public static final Page VIEW_AUDIT_LOG = new Page("/App/SystemAdmin/auditLog", Permissions.ACCESS_AUDIT_LOGS);

    // Airline Planning System: Fleet Planning Pages
    public static final Page LIST_TYPE = new Page("/App/FleetPlanning/aircraftTypes.xhtml", Permissions.MANAGE_FLEET);
    public static final Page CREATE_TYPE = new Page("/App/FleetPlanning/createAircraftType", Permissions.MANAGE_FLEET);
    public static final Page UPDATE_TYPE = new Page("/App/FleetPlanning/updateAircraftType", Permissions.MANAGE_FLEET);

    public static final Page LIST_AIRCRAFT = new Page("/App/FleetPlanning/aircrafts", Permissions.MANAGE_FLEET);
    public static final Page CREATE_AIRCRAFT = new Page("/App/FleetPlanning/createAircraft", Permissions.MANAGE_FLEET);
    public static final Page UPDATE_AIRCRAFT = new Page("/App/FleetPlanning/updateAircraft", Permissions.MANAGE_FLEET);

    public static final Page CREATE_SEATCONFIG = new Page("/App/FleetPlanning/createSeatConfig", Permissions.MANAGE_FLEET);

    // Airline Planning System: Route Planning Pages
    public static final Page LIST_AIRPORTS = new Page("/App/RoutePlanning/airports", Permissions.MANAGE_ROUTES);
    public static final Page CREATE_AIRPORTS = new Page("/App/RoutePlanning/createAirport", Permissions.MANAGE_ROUTES);
    public static final Page UPDATE_AIRPORTS = new Page("/App/RoutePlanning/updateAirport", Permissions.MANAGE_ROUTES);

    public static final Page LIST_CITIES = new Page("/App/RoutePlanning/cities", Permissions.MANAGE_ROUTES);
    public static final Page CREATE_CITY = new Page("/App/RoutePlanning/createCity", Permissions.MANAGE_ROUTES);
    public static final Page LIST_COUNTRIES = new Page("/App/RoutePlanning/countries", Permissions.MANAGE_ROUTES);
    public static final Page CREATE_COUNTRY = new Page("/App/RoutePlanning/createCountry", Permissions.MANAGE_ROUTES);

    public static final Page LIST_ROUTES = new Page("/App/RoutePlanning/routes", Permissions.MANAGE_ROUTES);
    public static final Page VISUALIZE_ROUTES = new Page("/App/RoutePlanning/routesVisualization", Permissions.MANAGE_ROUTES);
    public static final Page CREATE_ROUTES = new Page("/App/RoutePlanning/createRoute", Permissions.MANAGE_ROUTES);
    public static final Page UPDATE_ROUTES = new Page("/App/RoutePlanning/updateRoute", Permissions.MANAGE_ROUTES);

    public static final Page LIST_AIRCRAFT_ASSIGNMENT = new Page("/App/RoutePlanning/aircraftAssignment", Permissions.MANAGE_ROUTES);
    public static final Page CREATE_AIRCRAFT_ASSIGNMENT = new Page("/App/RoutePlanning/createAircraftAssignment", Permissions.MANAGE_ROUTES);
    public static final Page UPDATE_AIRCRAFT_ASSIGNMENT = new Page("/App/RoutePlanning/updateAircraftAssignment", Permissions.MANAGE_ROUTES);

    // Airline Planning System: Schedule Planning Pages
    public static final Page LIST_FLIGHTS = new Page("/App/FlightPlanning/flights", Permissions.MANAGE_FLIGHT);
    public static final Page CREATE_FLIGHT = new Page("/App/FlightPlanning/createFlight", Permissions.MANAGE_FLIGHT);
    public static final Page CREATE_FLIGHT_SINGLE = new Page("/App/FlightPlanning/createFlightSingle", Permissions.MANAGE_FLIGHT);
    public static final Page CREATE_FLIGHT_RECURRING = new Page("/App/FlightPlanning/createFlightRecurring", Permissions.MANAGE_FLIGHT);
    public static final Page UPDATE_FLIGHT = new Page("/App/FlightPlanning/updateFlight", Permissions.MANAGE_FLIGHT);

    public static final Page LIST_MAINTENANCE_SLOTS = new Page("/App/FlightPlanning/maintenanceSlots", Permissions.MANAGE_FLIGHT);
    public static final Page CREATE_MAINTENANCE_SLOT = new Page("/App/FlightPlanning/createMaintenanceSlot", Permissions.MANAGE_FLIGHT);
    public static final Page UPDATE_MAINTENANCE_SLOT = new Page("/App/FlightPlanning/updateMaintenanceSlot", Permissions.MANAGE_FLIGHT);

    // Airline Inventory Subsystem: Price Management Module
    public static final Page LIST_FARE_RULES = new Page("/App/PriceManagement/fareRules", Permissions.MANAGE_FARE_RULES);
    public static final Page CREATE_FARE_RULE = new Page("/App/PriceManagement/createFareRule", Permissions.MANAGE_FARE_RULES);

    public static final Page LIST_BOOKING_CLASSES = new Page("/App/PriceManagement/bookingClasses", Permissions.MANAGE_BOOKING_CLASSES);
    public static final Page CREATE_BOOKING_CLASS = new Page("/App/PriceManagement/createBookingClass", Permissions.MANAGE_BOOKING_CLASSES);

}
