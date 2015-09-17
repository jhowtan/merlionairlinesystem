package MAS.ManagedBean;

import MAS.Bean.UserBean;
import MAS.Bean.WorkgroupBean;
import MAS.Entity.User;
import MAS.Entity.Workgroup;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class UserProfileManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    private UserBean userBean;

    @EJB
    private WorkgroupBean workgroupBean;

    private User user = new User();
    private String phone = "";
    private List<Workgroup> workgroups;

    @PostConstruct
    public void init() {
        if (authManagedBean.isAuthenticated()) {
            try {
                user = userBean.getUser(authManagedBean.getUserId());
                phone = user.getPhone();
                workgroups = workgroupBean.getUserWorkgroups(authManagedBean.getUserId());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateUserInfo() {
        try {
            userBean.updateUserInfo(user.getId(), phone);
            FacesMessage m = new FacesMessage("User information successfully updated.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("userInfo", m);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(String newPassword) {
        try {
            userBean.changePassword(user.getId(), newPassword);
            FacesMessage m = new FacesMessage("Password successfully changed.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("changePassword", m);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Workgroup> getWorkgroups() {
        return workgroups;
    }

    public void setWorkgroups(List<Workgroup> workgroups) {
        this.workgroups = workgroups;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
