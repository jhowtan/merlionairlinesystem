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
                        new MenuEntry("Aircraft", null, Pages.LIST_AIRCRAFT, Pages.CREATE_AIRCRAFT, Pages.UPDATE_AIRCRAFT),
                        new MenuEntry("Aircraft Type", null, Pages.LIST_TYPE, Pages.CREATE_TYPE, Pages.UPDATE_TYPE),
                        new MenuEntry("Seat Configuration", null, Pages.CREATE_SEATCONFIG)
                )
        );

        entries.add(
                new MenuEntry("Route Planning", "road",
                        new MenuEntry("Airports", null, Pages.LIST_AIRPORTS, Pages.CREATE_AIRPORTS, Pages.UPDATE_AIRPORTS,
                                Pages.LIST_CITIES, Pages.CREATE_CITY,
                                Pages.LIST_COUNTRIES, Pages.CREATE_COUNTRY),
                        new MenuEntry("Routes", null, Pages.LIST_ROUTES, Pages.CREATE_ROUTES, Pages.UPDATE_ROUTES),
                        new MenuEntry("Aircraft Assignment", null, Pages.AIRCRAFT_ASSIGNMENT)

                )
        );
    }

    public List<MenuEntry> getEntries() {
        return entries;
    }
}
