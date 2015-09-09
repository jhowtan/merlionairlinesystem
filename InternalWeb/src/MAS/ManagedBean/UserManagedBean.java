package MAS.ManagedBean;

import MAS.Bean.UserBean;
import MAS.Entity.User;

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

}
