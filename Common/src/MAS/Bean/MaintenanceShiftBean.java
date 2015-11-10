package MAS.Bean;

import MAS.Entity.Airport;
import MAS.Entity.MaintenanceShift;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "MaintenanceShiftEJB")
@LocalBean
public class MaintenanceShiftBean {
    @PersistenceContext
    EntityManager em;

    public MaintenanceShiftBean() {
    }

    public long createMaintenanceShift(MaintenanceShift maintenanceShift) {
        em.persist(maintenanceShift);
        em.flush();
        return maintenanceShift.getId();
    }

    public List<MaintenanceShift> getAllMaintenanceShifts() {
        return em.createQuery("SELECT ms FROM MaintenanceShift ms", MaintenanceShift.class).getResultList();
    }

    public List<MaintenanceShift> findMaintenanceShiftsByAirport(String airportId) throws NotFoundException{
        Airport airport = em.find(Airport.class, airportId);
        if (airport == null) throw new NotFoundException();
        return em.createQuery("SELECT ms FROM MaintenanceShift ms WHERE ms.airport = :airport", MaintenanceShift.class)
                .setParameter("airport", airport)
                .getResultList();
    }
}
