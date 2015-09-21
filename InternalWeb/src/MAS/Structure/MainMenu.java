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
                new MenuEntry("Fleet Planning", "wrench",
                        new MenuEntry("Aircraft", null, Pages.LIST_AIRCRAFT, Pages.CREATE_AIRCRAFT, Pages.UPDATE_AIRCRAFT, Pages.UPDATE_SEATCONFIG),
                        new MenuEntry("Aircraft Type", null, Pages.LIST_TYPE, Pages.CREATE_TYPE, Pages.UPDATE_TYPE),
                        new MenuEntry("Seat Configuration", null, Pages.CREATE_SEATCONFIG, Pages.UPDATE_SEATCONFIG)
                )
        );

        entries.add(
                new MenuEntry("Route Planning", "wrench",
                        new MenuEntry("Routes", null, Pages.LIST_ROUTES, Pages.CREATE_ROUTE, Pages.UPDATE_ROUTE)
                )
        );
    }

    public List<MenuEntry> getEntries() {
        return entries;
    }
}
