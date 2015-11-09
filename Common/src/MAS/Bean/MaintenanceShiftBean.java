package MAS.Bean;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "MaintenanceShiftEJB")
@LocalBean
public class MaintenanceShiftBean {
    @PersistenceContext
    EntityManager em;

    public MaintenanceShiftBean() {
    }

    public long createMaintenanceShift() {
        return 0;
    }
}
