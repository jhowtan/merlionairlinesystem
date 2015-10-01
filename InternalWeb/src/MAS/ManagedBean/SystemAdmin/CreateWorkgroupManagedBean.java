package MAS.ManagedBean.SystemAdmin;

import MAS.Bean.UserBean;
import MAS.Bean.WorkgroupBean;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

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
public class CreateWorkgroupManagedBean {

    @EJB
    WorkgroupBean workgroupBean;

    @EJB
    UserBean userBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String workgroupName;
    private String workgroupDesc;

    private List<User> users;
    private Map<Long, Boolean> usersMap;

    @PostConstruct
    public void init() {
        populateUsers();
    }

    private void populateUsers() {
        users = userBean.getAllUsers();
        usersMap = new HashMap<>();
        for (User user : users) {
            usersMap.put(user.getId(), Boolean.FALSE);
            if (user.getId().equals(authManagedBean.getUserId())) {
                usersMap.put(user.getId(), Boolean.TRUE);
            }
        }
    }

    public void createWorkgroup() throws NotFoundException {
        ArrayList<Long> userIds = new ArrayList<>();
        for (Object o : usersMap.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if ((Long) pair.getKey() == authManagedBean.getUserId()) {
                userIds.add((Long) pair.getKey());
                continue;
            }
            if ((Boolean) pair.getValue()) {
                userIds.add((Long) pair.getKey());
            }
        }
        workgroupBean.createWorkgroup(workgroupName, workgroupDesc, authManagedBean.getUserId(), userIds);
        setWorkgroupName(null);
        setWorkgroupDesc(null);
        populateUsers();
        FacesMessage m = new FacesMessage("Workgroup created successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public String getWorkgroupName() {
        return workgroupName;
    }

    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }

    public String getWorkgroupDesc() {
        return workgroupDesc;
    }

    public void setWorkgroupDesc(String workgroupDesc) {
        this.workgroupDesc = workgroupDesc;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Map<Long, Boolean> getUsersMap() {
        return usersMap;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public void setUsersMap(Map<Long, Boolean> usersMap) {
        this.usersMap = usersMap;
    }
}
