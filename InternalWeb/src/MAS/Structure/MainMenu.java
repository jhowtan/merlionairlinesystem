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
                        new MenuEntry("Roles", null, Pages.LIST_ROLES, Pages.CREATE_ROLE),
                        new MenuEntry("Workgroups", null, Pages.LIST_WORKGROUPS, Pages.CREATE_WORKGROUP),
                        new MenuEntry("Audit Log", null, Pages.VIEW_AUDIT_LOG)
                )
        );

    }

    public List<MenuEntry> getEntries() {
        return entries;
    }
}
