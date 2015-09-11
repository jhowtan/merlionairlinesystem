package MAS.ManagedBean;

import MAS.Bean.RoleBean;
import MAS.Bean.UserBean;
import MAS.Entity.Role;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.*;

@ManagedBean
public class CreateUserManagedBean {
    @EJB
    private UserBean userBean;
    @EJB
    private RoleBean roleBean;

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private List<Role> roles;
    private Map<Long, Boolean> rolesMap;

    @PostConstruct
    public void init() {
        populateRoles();
    }

    private void populateRoles() {
        roles = roleBean.getAllRoles();
        rolesMap = new HashMap<Long, Boolean>();
        for (Role role : roles) {
            rolesMap.put(role.getId(), Boolean.FALSE);
        }
    }

    public void createUser() {
        ArrayList<Long> roleIds = new ArrayList<>();
        Iterator it = rolesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if ((Boolean) pair.getValue()) {
                roleIds.add((Long) pair.getKey());
            }
        }
        Long userId = userBean.createUser(getUsername(), getFirstName(), getLastName(), getEmail(), getPhone());
        try {
            userBean.setRoles(userId, roleIds);
        } catch (NotFoundException e) {
        }
        setUsername(null);
        setFirstName(null);
        setLastName(null);
        setEmail(null);
        setPhone(null);
        populateRoles();
        FacesMessage m = new FacesMessage("User created successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Map<Long, Boolean> getRolesMap() {
        return rolesMap;
    }

    public void setRolesMap(Map<Long, Boolean> rolesMap) {
        this.rolesMap = rolesMap;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
