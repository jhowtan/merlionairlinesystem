package MAS.Bean;

import MAS.Entity.Flight;
import MAS.Entity.FlightReport;
import MAS.Entity.MaintenanceReport;
import MAS.Exception.NotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "OperationsReportingEJB")
public class OperationsReportingBean {
    public OperationsReportingBean() {
    }

    @PersistenceContext
    EntityManager em;

    /**
     * Flight Report Methods
     */
    public FlightReport createFlightReport(FlightReport flightReport) {
        em.persist(flightReport);
        em.flush();
        return flightReport;
    }

    public List<FlightReport> findFlightReportsByFlight(long flightId) throws NotFoundException {
        Flight flight = em.find(Flight.class, flightId);
        if (flight == null) throw new NotFoundException();
        return em.createQuery("SELECT fr FROM FlightReport fr WHERE fr.flight = :flight", FlightReport.class)
                .setParameter("flight", flight).getResultList();
    }

    public List<FlightReport> getAllFlightReports() {
        return em.createQuery("SELECT fr FROM FlightReport fr", FlightReport.class).getResultList();
    }

    public FlightReport getFlightReport(long id) throws NotFoundException {
        FlightReport flightReport = em.find(FlightReport.class, id);
        if (flightReport == null) throw new NotFoundException();
        return flightReport;
    }

    /**
     * Maintenance Report Methods
     */
    public MaintenanceReport createMaintenanceReport(MaintenanceReport maintenanceReport) {
        em.persist(maintenanceReport);
        em.flush();
        return maintenanceReport;
    }

    public List<MaintenanceReport> getAllMaintenanceReports() {
        return em.createQuery("SELECT mr FROM MaintenanceReport mr", MaintenanceReport.class).getResultList();
    }

    public MaintenanceReport getMaintenanceReport(long id) throws NotFoundException {
        MaintenanceReport maintenanceReport = em.find(MaintenanceReport.class, id);
        if (maintenanceReport == null) throw new NotFoundException();
        return maintenanceReport;
    }

}
