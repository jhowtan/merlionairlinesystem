package MAS.Bean;

import MAS.Common.Permissions;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.lang.reflect.Field;

@Singleton
@Startup
public class InitBean {
    @EJB
    RoleBean roleBean;

    @PostConstruct
    public void init() {
        if(roleBean.getAllPermissions().size() == 0) {
            for(Field permissionField : Permissions.class.getDeclaredFields()) {
                try {
                    roleBean.createPermission((String) permissionField.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
