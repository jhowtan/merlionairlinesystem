package MAS.ManagedBean.SystemAdmin;

import MAS.Bean.UserBean;
import MAS.Bean.WorkgroupBean;
import MAS.Entity.User;
import MAS.Entity.Workgroup;
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
public class UpdateWorkgroupManagedBean {
    @EJB
    private WorkgroupBean workgroupBean;
    @EJB
    private UserBean userBean;
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private Workgroup workgroup;
    private String workgroupName;
    private String workgroupDesc;
    private Map<String,String> params;
    private List<User> users;
    private Map<Long, Boolean> usersMap;

    @PostConstruct
    public void init() {
        params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        long roleId = Long.parseLong(params.get("workgroupId"));
        getWorkgroup(roleId);
        populateUsers();
    }

    private void getWorkgroup(long id) {
        try {
            workgroup = workgroupBean.getWorkgroup(id);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        workgroupName = workgroup.getName();
        workgroupDesc = workgroup.getDescription();
    }

    private void populateUsers() {
        users = userBean.getAllUsers();
        usersMap = new HashMap<>();
        List<User> workgroupUsers = workgroup.getUsers();
        for (User user : users) {
            if (workgroupUsers.contains(user)){
                usersMap.put(user.getId(), Boolean.TRUE);
            } else {
                usersMap.put(user.getId(), Boolean.FALSE);
            }
        }
    }

    public void save() throws NotFoundException {
        ArrayList<Long> userIds = new ArrayList<>();
        for (Object o : usersMap.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if ((Boolean) pair.getValue()) {
                userIds.add((Long) pair.getKey());
            }
        }
        Long workgroupId = workgroupBean.editWorkgroup(workgroup.getId(), workgroupName, workgroupDesc);
        try {
            workgroupBean.setWorkgroupUsers(workgroupId, userIds);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        authManagedBean.createAuditLog("Updated workgroup information: " + workgroupName, "update_role");

        init();

        FacesMessage m = new FacesMessage("Workgroup details updated successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public Map<Long, Boolean> getUsersMap() {
        return usersMap;
    }

    public void setUsersMap(Map<Long, Boolean> usersMap) {
        this.usersMap = usersMap;
    }

    public Workgroup getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(Workgroup workgroup) {
        this.workgroup = workgroup;
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

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
