package MAS.Structure;

import java.util.ArrayList;
import java.util.List;

public class MainMenu {
    private List<MenuEntry> entries;

    public MainMenu() {
        entries = new ArrayList<>();

        entries.add(
                new MenuEntry("Dashboard", "dashboard", Pages.DASHBOARD)
        );

        entries.add(
                new MenuEntry("System Admin", "wrench",
                        new MenuEntry("Users", null, Pages.LIST_USERS, Pages.CREATE_USER, Pages.UPDATE_USER),
                        new MenuEntry("Roles", null, Pages.LIST_ROLES, Pages.CREATE_ROLE, Pages.UPDATE_ROLE),
                        new MenuEntry("Workgroups", null, Pages.LIST_WORKGROUPS, Pages.CREATE_WORKGROUP, Pages.UPDATE_WORKGROUP),
                        new MenuEntry("Audit Log", null, Pages.VIEW_AUDIT_LOG)
                )
        );

        entries.add(
                new MenuEntry("Fleet Planning", "sitemap",
                        new MenuEntry("Aircraft Type", null, Pages.LIST_TYPE, Pages.CREATE_TYPE, Pages.UPDATE_TYPE),
                        new MenuEntry("Seat Configuration", null, Pages.CREATE_SEATCONFIG),
                        new MenuEntry("Aircraft", null, Pages.LIST_AIRCRAFT, Pages.CREATE_AIRCRAFT, Pages.UPDATE_AIRCRAFT)
                )
        );

        entries.add(
                new MenuEntry("Route Planning", "road",
                        new MenuEntry("Cities & Countries", null, Pages.LIST_CITIES, Pages.CREATE_CITY, Pages.LIST_COUNTRIES, Pages.CREATE_COUNTRY),
                        new MenuEntry("Airports", null, Pages.LIST_AIRPORTS, Pages.CREATE_AIRPORTS, Pages.UPDATE_AIRPORTS),
                        new MenuEntry("Routes", null, Pages.LIST_ROUTES, Pages.VISUALIZE_ROUTES, Pages.CREATE_ROUTES, Pages.UPDATE_ROUTES),
                        new MenuEntry("Aircraft Assignment", null, Pages.LIST_AIRCRAFT_ASSIGNMENT, Pages.CREATE_AIRCRAFT_ASSIGNMENT, Pages.UPDATE_AIRCRAFT_ASSIGNMENT)
                )
        );

        entries.add(
                new MenuEntry("Flight Planning", "plane",
                        new MenuEntry("Flights", null, Pages.LIST_FLIGHTS, Pages.CREATE_FLIGHT, Pages.CREATE_FLIGHT_SINGLE, Pages.CREATE_FLIGHT_RECURRING, Pages.UPDATE_FLIGHT),
                        new MenuEntry("Maintenance", null, Pages.LIST_MAINTENANCE_SLOTS, Pages.CREATE_MAINTENANCE_SLOT, Pages.UPDATE_MAINTENANCE_SLOT)
                )
        );

        entries.add(
                new MenuEntry("Schedule Development", "calendar",
                        new MenuEntry("Create Schedule", null, Pages.SCHEDULE_DEVELOPMENT)
                )
        );

        entries.add(
                new MenuEntry("Price Management", "usd",
                        new MenuEntry("Fare Rules", null, Pages.LIST_FARE_RULES, Pages.CREATE_FARE_RULE),
                        new MenuEntry("Booking Classes", null, Pages.LIST_BOOKING_CLASSES, Pages.CREATE_BOOKING_CLASS)
                )
        );

        entries.add(
                new MenuEntry("Costs Management", "book",
                        new MenuEntry("Costs", null, Pages.LIST_COSTS, Pages.CREATE_COST)
                )
        );

        entries.add(
                new MenuEntry("Crew Operations", "coffee",
                        new MenuEntry("Crew Certification", null, Pages.CREW_CERTIFICATION, Pages.CREATE_CREW_CERTIFICATION, Pages.VIEW_CREW_CERTIFICATION),
                        new MenuEntry("Flight Bidding", null, Pages.FLIGHT_BIDDING),
                        new MenuEntry("Flight Roster", null, Pages.FLIGHT_ROSTER),
                        new MenuEntry("Flight Deferment", null, Pages.FLIGHT_DEFERMENT),
                        new MenuEntry("Maintenance Shifts", null, Pages.MAINTENANCE_SHIFTS, Pages.CREATE_MAINTENANCE_SHIFT),
                        new MenuEntry("Flight Reporting", null, Pages.LIST_FLIGHT_REPORTS, Pages.CREATE_FLIGHT_REPORT),
                        new MenuEntry("Maintenance Reporting", null, Pages.LIST_MAINTENANCE_REPORTS, Pages.CREATE_MAINTENANCE_REPORT),
                        new MenuEntry("Flight Sign In/Out", null, Pages.FLIGHT_SIGNINOUT)
                )
        );

        entries.add(
                new MenuEntry("Departure Control", "suitcase",
                        new MenuEntry("Check In", null, Pages.CHECK_IN, Pages.CHECK_IN_2),
                        new MenuEntry("Gate Control", null, Pages.GATE_CHECK, Pages.GATE_CHECK_2),
                        new MenuEntry("Passenger Service", null, Pages.PASSENGER_SERVICE)
                )
        );

        entries.add(
                new MenuEntry("Customer Relations", "user",
                        new MenuEntry("FFP Customers", null, Pages.LIST_CUSTOMERS, Pages.VIEW_CUSTOMER),
                        new MenuEntry("Helpdesk", null, Pages.HELPDESK, Pages.HELPDESK_CUSTOMER, Pages.HELPDESK_UPDATE_CUSTOMER_PROFILE, Pages.HELPDESK_UPDATE_MEMBERSHIP, Pages.HELPDESK_ISSUE_CARD),
                        new MenuEntry("Campaigns", null, Pages.VIEW_CAMPAIGNS, Pages.CREATE_CAMPAIGN, Pages.UPDATE_CAMPAIGN),
                        new MenuEntry("Customer Groups", null, Pages.VIEW_CUSTOMERGROUPS, Pages.CREATE_CUSTOMERGROUP, Pages.UPDATE_CUSTOMERGROUP),
                        new MenuEntry("Customer Segmentation", null, Pages.CUSTOMER_SEGMENTATION)
                )
        );

        entries.add(
                new MenuEntry("Management Reporting", "line-chart",
                        new MenuEntry("Profitability Reporting", null, Pages.PROFITABILITY_REPORT),
                        new MenuEntry("Seasonality Reporting", null, Pages.SEASONALITY_REPORT),
                        new MenuEntry("Aircraft Reporting", null, Pages.AIRCRAFT_REPORT),
                        new MenuEntry("Flight Reporting", null, Pages.FLIGHT_REPORT),
                        new MenuEntry("Crew Reporting", null, Pages.CREW_REPORT),
                        new MenuEntry("Maintenance Reporting", null, Pages.MAINTENANCE_REPORT),
                        new MenuEntry("Campaign Reporting", null, Pages.CAMPAIGN_REPORT)
                )
        );
    }

    public List<MenuEntry> getEntries() {
        return entries;
    }
}
