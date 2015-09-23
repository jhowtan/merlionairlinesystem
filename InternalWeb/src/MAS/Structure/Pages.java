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

    // Airline Planning System: Fleet Planning Pages
    public static final Page LIST_TYPE = new Page("/App/FleetPlanning/aircraftTypes.xhtml", Permissions.MANAGE_FLEET);
    public static final Page CREATE_TYPE = new Page("/App/FleetPlanning/createAircraftType", Permissions.MANAGE_FLEET);
    public static final Page UPDATE_TYPE = new Page("/App/FleetPlanning/updateAircraftType", Permissions.MANAGE_FLEET);

    public static final Page LIST_AIRCRAFT = new Page("/App/FleetPlanning/aircrafts", Permissions.MANAGE_FLEET);
    public static final Page CREATE_AIRCRAFT = new Page("/App/FleetPlanning/createAircraft", Permissions.MANAGE_FLEET);
    public static final Page UPDATE_AIRCRAFT = new Page("/App/FleetPlanning/updateAircraft", Permissions.MANAGE_FLEET);

    public static final Page CREATE_SEATCONFIG = new Page("/App/FleetPlanning/createSeatConfig", Permissions.MANAGE_FLEET);
    public static final Page UPDATE_SEATCONFIG = new Page("/App/FleetPlanning/updateSeatConfig", Permissions.MANAGE_FLEET);

    // Airline Planning System: Route Planning Pages
    public static final Page LIST_AIRPORTS = new Page("/App/RoutePlanning/airports", Permissions.MANAGE_ROUTES);
    public static final Page CREATE_AIRPORTS = new Page("/App/RoutePlanning/createAirports", Permissions.MANAGE_ROUTES);
    public static final Page UPDATE_AIRPORTS = new Page("/App/RoutePlanning/updateAirports", Permissions.MANAGE_ROUTES);

    // Airline Planning System: Schedule Planning Pages


    public static final Page VIEW_AUDIT_LOG = new Page("/App/SystemAdmin/auditLog", Permissions.ACCESS_AUDIT_LOGS);
}
