package MAS.ManagedBean.OperationsReporting;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.OperationsReportingBean;
import MAS.Entity.Flight;
import MAS.Entity.FlightReport;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
public class CreateFlightReportManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    FlightScheduleBean flightScheduleBean;

    @EJB
    OperationsReportingBean operationsReportingBean;

    private List<Flight> crewFlights;
    private List<String> statusList;
    private List<String> categoryList;

    private Flight flight;
    private FlightReport flightReport;

    @PostConstruct
    public void init() {
        crewFlights = flightScheduleBean.getAllFlights();
        statusList = new ArrayList<>();
        categoryList = new ArrayList<>();
        statusList.add("OK");
        statusList.add("NOT OK");
        statusList.add("UNDIAGNOSED");
        categoryList.add("Minor");
        categoryList.add("Major");
    }

    public void createFlightReport() {
        flightReport = operationsReportingBean.createFlightReport(flightReport);
        authManagedBean.createAuditLog("Created new flight report: " + flightReport.getFlight().getCode() + " - " + flightReport.getId() + " @ ", "create_flight_report");
        FacesMessage m = new FacesMessage("Flight report created for" + flightReport.getFlight().getCode() + " successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public List<Flight> getCrewFlights() {
        return crewFlights;
    }

    public void setCrewFlights(List<Flight> crewFlights) {
        this.crewFlights = crewFlights;
    }

    public AuthManagedBean getAuthManagedBean() {
        return authManagedBean;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public FlightReport getFlightReport() {
        return flightReport;
    }

    public void setFlightReport(FlightReport flightReport) {
        this.flightReport = flightReport;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }
}
