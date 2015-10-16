package MAS.ManagedBean.DepartureControl;


import MAS.Bean.FlightScheduleBean;
import MAS.Entity.ETicket;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

@ManagedBean
public class PrintPassManagedBean implements Serializable {
    @EJB
    FlightScheduleBean flightScheduleBean;

    public ETicket getEticket() throws NotFoundException {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        return flightScheduleBean.getETicket(Long.parseLong(params.get("eticket")));
    }
}
