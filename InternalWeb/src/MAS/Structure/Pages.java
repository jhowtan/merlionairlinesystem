package MAS.Structure;

import MAS.Common.Permissions;

public class Pages {
    public static final Page DASHBOARD = new Page("/App/index");

    public static final Page LIST_USERS = new Page("/App/SystemAdmin/users", Permissions.MANAGE_USERS);
    public static final Page CREATE_USER = new Page("/App/SystemAdmin/createUser", Permissions.MANAGE_USERS);

    public static final Page LIST_ROLES = new Page("/App/SystemAdmin/roles", Permissions.MANAGE_ROLES);
    public static final Page CREATE_ROLE = new Page("/App/SystemAdmin/createRole", Permissions.MANAGE_ROLES);

    public static final Page VIEW_AUDIT_LOG = new Page("/App/SystemAdmin/auditLog", Permissions.ACCESS_AUDIT_LOGS);
}
