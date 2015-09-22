package MAS.ManagedBean;

import MAS.Bean.RoleBean;
import MAS.Entity.Permission;
import MAS.Entity.Role;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
public class UpdateRoleManagedBean {
    @EJB
    private RoleBean roleBean;
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private Role role;
    private String roleName;
    private Map<String,String> params;
    private List<Permission> permissions;
    private Map<Long, Boolean> permissionsMap;

    @PostConstruct
    public void init() {
        params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        long roleId = Long.parseLong(params.get("roleId"));
        getRole(roleId);
        populatePermissions();
    }

    private void getRole(long id) {
        try {
            role = roleBean.getRole(id);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        roleName = role.getName();
    }

    private void populatePermissions() {
        permissions = roleBean.getAllPermissions();
        permissionsMap = new HashMap<>();
        List<Permission> rolePermissions = role.getPermissions();
        for (Permission permission : permissions) {
            if (rolePermissions.contains(permission)){
                permissionsMap.put(permission.getId(), Boolean.TRUE);
            } else {
                permissionsMap.put(permission.getId(), Boolean.FALSE);
            }
        }
    }

    public void save() throws NotFoundException {
        ArrayList<Long> permissionIds = new ArrayList<>();
        for (Object o : permissionsMap.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if ((Boolean) pair.getValue()) {
                permissionIds.add((Long) pair.getKey());
            }
        }
        Long roleId = roleBean.editRole(role.getId(), roleName);
        try {
            roleBean.setPermissions(roleId, permissionIds);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        authManagedBean.createAuditLog("Updated role information: " + roleName, "update_role");

        init();

        FacesMessage m = new FacesMessage("Role details updated successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public void delete(long id) {
        try {
            String roleName = roleBean.getRole(id).getName();
            roleBean.removeRole(id);
            authManagedBean.createAuditLog("Deleted role: " + roleName, "delete_role");
        } catch (NotFoundException e) {
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public Map<Long, Boolean> getPermissionsMap() {
        return permissionsMap;
    }

    public void setPermissionsMap(Map<Long, Boolean> permissionsMap) {
        this.permissionsMap = permissionsMap;
    }
}
