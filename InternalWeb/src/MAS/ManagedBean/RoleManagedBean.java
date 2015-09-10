package MAS.ManagedBean;

import MAS.Bean.RoleBean;
import MAS.Entity.Role;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean
public class RoleManagedBean {
    @EJB
    private RoleBean roleBean;

    public List<Role> getAllRoles() {
        return roleBean.getAllRoles();
    }

}
