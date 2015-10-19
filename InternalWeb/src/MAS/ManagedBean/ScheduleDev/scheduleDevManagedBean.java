package MAS.ManagedBean.ScheduleDev;

import MAS.Bean.ScheduleDevelopmentBean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class scheduleDevManagedBean {
    @EJB
    ScheduleDevelopmentBean scheduleDevelopmentBean;


}
