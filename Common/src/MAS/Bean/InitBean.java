package MAS.Bean;

import MAS.Common.Constants;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class InitBean {
    @EJB
    RoleBean roleBean;

    @PostConstruct
    public void init() {
        if(roleBean.getAllPermissions().size() == 0) {
            for(String permission : Constants.PERMISSIONS) {
                roleBean.createPermission(permission);
            }
        }
    }

}
