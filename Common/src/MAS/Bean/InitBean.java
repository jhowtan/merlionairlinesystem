package MAS.Bean;

import MAS.Common.Permissions;
import MAS.Entity.Permission;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
@Startup
public class InitBean {
    @EJB
    RoleBean roleBean;
    @EJB
    UserBean userBean;

    @PostConstruct
    public void init() {
        List<Long> permissionIds = new ArrayList<>();
        if(roleBean.getAllPermissions().size() == 0) {
            for(Field permissionField : Permissions.class.getDeclaredFields()) {
                try {
                    permissionIds.add(roleBean.createPermission((String) permissionField.get(null)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            Long roleId = roleBean.createRole("Super Admin", permissionIds);
            Long userId = userBean.createUser("admin", "John", "Smith", "nobody@example.com", "1234567");
            try {
                userBean.setPassword(userId, "password");
                userBean.setRoles(userId, Arrays.asList(roleId));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
