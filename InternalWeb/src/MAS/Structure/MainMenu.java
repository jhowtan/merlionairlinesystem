package MAS.Structure;

import MAS.Common.Permissions;

import java.util.ArrayList;
import java.util.List;

public class MainMenu {
    private List<MenuEntry> entries;

    public MainMenu() {
        ArrayList<MenuEntry> mainMenu = new ArrayList<>();

        MenuEntry<Page> dashboard = new MenuEntry<>("Dashboard", "dashboard",
                new Page("/App/index")
        );
        mainMenu.add(dashboard);

        MenuEntry<MenuEntry> systemAdmin = new MenuEntry("System Admin", "wrench",
                new MenuEntry<Page>("Users", null,
                        new Page("/App/SystemAdmin/users", Permissions.MANAGE_USERS),
                        new Page("/App/SystemAdmin/createUser", Permissions.MANAGE_USERS)
                ),
                new MenuEntry<Page>("Roles", null,
                        new Page("/App/SystemAdmin/roles", Permissions.MANAGE_ROLES),
                        new Page("/App/SystemAdmin/createRole", Permissions.MANAGE_ROLES)
                ),
                new MenuEntry<Page>("Audit Log", null,
                        new Page("/App/SystemAdmin/auditLog", Permissions.ACCESS_AUDIT_LOGS)
                )
        );
        mainMenu.add(systemAdmin);

        this.setEntries(mainMenu);
    }

    public List<MenuEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<MenuEntry> entries) {
        this.entries = entries;
    }
}
