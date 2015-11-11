package MAS.ManagedBean.SystemAdmin;

import MAS.Bean.FFPBean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;

@ManagedBean
public class MaintenanceManagedBean {

    @EJB
    FFPBean ffpBean;

    public void runMaintenanceListener(AjaxBehaviorEvent ajaxBehaviorEvent) {
        ffpBean.periodicUpdateFFP();
    }

}
