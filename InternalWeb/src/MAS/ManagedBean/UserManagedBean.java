package MAS.ManagedBean;

import MAS.Bean.UserBean;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean
public class UserManagedBean {
    @EJB
    private UserBean userBean;

    public List<User> getAllUsers() {
        return userBean.getAllUsers();
    }

    public void setLocked(long id, boolean isLocked) {
        try {
            userBean.setLocked(id, isLocked);
        } catch (NotFoundException e) {
        }
    }

    public void delete(long id) {
        try {
            userBean.removeUser(id);
        } catch (NotFoundException e) {
        }
    }

}
