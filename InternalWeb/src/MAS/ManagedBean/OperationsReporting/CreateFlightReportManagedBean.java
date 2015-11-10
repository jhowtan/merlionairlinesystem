package MAS.ManagedBean.OperationsReporting;

import MAS.Bean.FlightRosterBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Bean.OperationsReportingBean;
import MAS.Bean.UserBean;
import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.Flight;
import MAS.Entity.FlightReport;
import MAS.Entity.FlightRoster;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ManagedBean
public class CreateFlightReportManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    UserBean userBean;

    @EJB
    FlightScheduleBean flightScheduleBean;

    @EJB
    FlightRosterBean flightRosterBean;

    @EJB
    OperationsReportingBean operationsReportingBean;

    private List<Flight> crewFlights;
    private List<String> categoryList;

    private long flightId;
    private FlightReport flightReport;

    @PostConstruct
    public void init() {
        try {
            List<FlightRoster> flightRosters = flightRosterBean.getFlightRostersOfUser(authManagedBean.getUserId());
            crewFlights = new ArrayList<>();
            for (FlightRoster flightRoster : flightRosters) {
                if (flightRoster.getFlight().getActualDepartureTime() != null) {
                    if ( flightRoster.getFlight().getActualDepartureTime().compareTo(new Date()) < 0) {
                        if ( flightRoster.getFlight().getActualDepartureTime().compareTo(Utils.relativeMonth(new Date(), -1)) >= 0)
                            crewFlights.add(flightRoster.getFlight());
                    }
                }
                else {
                    continue;
                }
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        categoryList = new ArrayList<>(Arrays.asList(Constants.FLIGHT_REPORT_CATEGORIES));
        flightReport = new FlightReport();
        flightReport.setStatus(0);
    }

    public void createFlightReport() {
        try {
            flightReport.setUser(userBean.getUser(authManagedBean.getUserId()));
            flightReport.setFlight(flightScheduleBean.getFlight(flightId));
            flightReport = operationsReportingBean.createFlightReport(flightReport);
            authManagedBean.createAuditLog("Created new flight report: " + flightReport.getFlight().getCode() + " - " + flightReport.getId() + " @ ", "create_flight_report");
            FacesMessage m = new FacesMessage("Flight report created for " + flightReport.getFlight().getCode() + " successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
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

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(long flightId) {
        this.flightId = flightId;
    }

    public FlightReport getFlightReport() {
        return flightReport;
    }

    public void setFlightReport(FlightReport flightReport) {
        this.flightReport = flightReport;
    }


    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }
}
