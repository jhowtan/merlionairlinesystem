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
                new MenuEntry("Fleet Planning", "plane",
                        new MenuEntry("Aircraft Type", null, Pages.LIST_TYPE, Pages.CREATE_TYPE, Pages.UPDATE_TYPE),
                        new MenuEntry("Seat Configuration", null, Pages.CREATE_SEATCONFIG),
                        new MenuEntry("Aircraft", null, Pages.LIST_AIRCRAFT, Pages.CREATE_AIRCRAFT, Pages.UPDATE_AIRCRAFT)
                )
        );

        entries.add(
                new MenuEntry("Route Planning", "road",
                        new MenuEntry("Countries", null, Pages.LIST_COUNTRIES, Pages.CREATE_COUNTRY),
                        new MenuEntry("Cities", null, Pages.LIST_CITIES, Pages.CREATE_CITY),
                        new MenuEntry("Airports", null, Pages.LIST_AIRPORTS, Pages.CREATE_AIRPORTS, Pages.UPDATE_AIRPORTS),
                        new MenuEntry("Routes", null, Pages.LIST_ROUTES, Pages.CREATE_ROUTES, Pages.UPDATE_ROUTES),
                        new MenuEntry("Aircraft Assignment", null, Pages.AIRCRAFT_ASSIGNMENT)

                )
        );

        entries.add(
                new MenuEntry("Price Management", "usd",
                        new MenuEntry("Fare Rules", null, Pages.LIST_FARE_RULES, Pages.CREATE_FARE_RULE)
                )
        );
    }

    public List<MenuEntry> getEntries() {
        return entries;
    }
}
