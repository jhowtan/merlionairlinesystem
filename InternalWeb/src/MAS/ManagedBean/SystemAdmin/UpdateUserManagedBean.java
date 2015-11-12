package MAS.ManagedBean.SystemAdmin;

import MAS.Bean.RoleBean;
import MAS.Bean.RouteBean;
import MAS.Bean.UserBean;
import MAS.Common.Constants;
import MAS.Entity.Airport;
import MAS.Entity.Role;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.*;

@ManagedBean
public class UpdateUserManagedBean {
    @EJB
    private UserBean userBean;
    @EJB
    private RoleBean roleBean;
    @EJB
    private RouteBean routeBean;
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private User user;
    private Map<String,String> params;

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String baseAirport;
    private List<Role> roles;
    private Map<Long, Boolean> rolesMap;
    private String job;
    private List<String> jobList = Arrays.asList(Constants.JOB_NAMES);

    @PostConstruct
    public void init() {
        params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        long userId = Long.parseLong(params.get("userId"));
        getUser(userId);
        populateRoles();
    }

    public List<Airport> retrieveAirports() {
        return routeBean.getAllAirports();
    }

    private void getUser(long id) {
        try {
            user = userBean.getUser(id);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        username = user.getUsername();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        phone = user.getPhone();
        job = jobList.get(user.getJob());
        baseAirport = user.getBaseAirport().getId();
    }

    private void populateRoles() {
        roles = roleBean.getAllRoles();
        rolesMap = new HashMap<>();
        List<Role> userRoles = user.getRoles();
        for (Role role : roles) {
            if (userRoles.contains(role)) {
                rolesMap.put(role.getId(), Boolean.TRUE);
            } else {
                rolesMap.put(role.getId(), Boolean.FALSE);
            }
        }
    }

    public void save() throws NotFoundException {
        username = username.toLowerCase();
        email = email.toLowerCase();

        ArrayList<Long> roleIds = new ArrayList<>();
        for (Object o : rolesMap.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if ((Boolean) pair.getValue()) {
                roleIds.add((Long) pair.getKey());
            }
        }
        try {
            Long userId = userBean.adminUpdateUserInfo(user.getId(), firstName, lastName, email, phone, routeBean.getAirport(baseAirport));
            userBean.setRoles(userId, roleIds);
            userBean.changeJob(userId, jobList.indexOf(job));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        authManagedBean.createAuditLog("Updated user information: " + username, "update_user");

        init();

        FacesMessage m = new FacesMessage("User details updated successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public void setLocked(long id, boolean isLocked) {
        try {
            userBean.setLocked(id, isLocked);
            if(isLocked) {
                authManagedBean.createAuditLog("Locked user: " + userBean.getUser(id).getUsername(), "lock_user");
            } else {
                authManagedBean.createAuditLog("Unlocked user: " + userBean.getUser(id).getUsername(), "unlock_user");
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void delete(long id) {
        try {
            userBean.removeUser(id);
            authManagedBean.createAuditLog("Deleted user: " + userBean.getUser(id).getUsername(), "delete_user");
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBaseAirport() {
        return baseAirport;
    }

    public void setBaseAirport(String baseAirport) {
        this.baseAirport = baseAirport;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public Map<Long, Boolean> getRolesMap() {
        return rolesMap;
    }

    public void setRolesMap(Map<Long, Boolean> rolesMap) {
        this.rolesMap = rolesMap;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
