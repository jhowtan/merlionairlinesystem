package MAS.ManagedBean;

import MAS.Bean.RoleBean;
import MAS.Entity.Permission;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.*;

@ManagedBean
public class CreateRoleManagedBean {
    @EJB
    private RoleBean roleBean;

    private String roleName;
    private List<Permission> permissions;
    private Map<Long, Boolean> permissionsMap;

    @PostConstruct
    public void init() {
        populatePermissions();
    }

    private void populatePermissions() {
        permissions = roleBean.getAllPermissions();
        permissionsMap = new HashMap<Long, Boolean>();
        for (Permission permission : permissions) {
            permissionsMap.put(permission.getId(), Boolean.FALSE);
        }
    }

    public void createRole() {
        ArrayList<Long> permissionIds = new ArrayList<>();
        Iterator it = permissionsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if ((Boolean) pair.getValue()) {
                permissionIds.add((Long) pair.getKey());
            }
        }
        roleBean.createRole(roleName, permissionIds);
        setRoleName(null);
        populatePermissions();
        FacesMessage m = new FacesMessage("Role created successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Map<Long, Boolean> getPermissionsMap() {
        return permissionsMap;
    }

    public void setPermissionsMap(Map<Long, Boolean> permissionsMap) {
        this.permissionsMap = permissionsMap;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
