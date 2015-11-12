package MAS.ManagedBean.OperationsReporting;

import MAS.Bean.AircraftMaintenanceSlotBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Bean.OperationsReportingBean;
import MAS.Bean.UserBean;
import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.AircraftMaintenanceSlot;
import MAS.Entity.Airport;
import MAS.Entity.MaintenanceReport;
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
public class CreateMaintenanceReportManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    UserBean userBean;

    @EJB
    FlightScheduleBean flightScheduleBean;

    @EJB
    AircraftMaintenanceSlotBean aircraftMaintenanceSlotBean;

    @EJB
    OperationsReportingBean operationsReportingBean;

    private List<AircraftMaintenanceSlot> crewMaintenanceSlots;
    private List<String> statusList;
    private List<String> categoryList;

    private long aircraftMaintenanceSlotId;
    private MaintenanceReport maintenanceReport;

    @PostConstruct
    public void init() {
        try {
            Airport baseAirport = userBean.getUser(authManagedBean.getUserId()).getBaseAirport();
            List<AircraftMaintenanceSlot> airportMaintenanceSlots = aircraftMaintenanceSlotBean.findSlotByAirport(baseAirport.getId());
            crewMaintenanceSlots = new ArrayList<>();
            for (AircraftMaintenanceSlot maintenanceSlot : airportMaintenanceSlots) {
                if (maintenanceSlot.getAirport().equals(baseAirport)) {
                    if ( maintenanceSlot.getStartTime().compareTo(new Date()) < 0) {
                        if ( maintenanceSlot.getStartTime().compareTo(Utils.relativeMonth(new Date(), -1)) >= 0)
                            crewMaintenanceSlots.add(maintenanceSlot);
                    }
                }
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        categoryList = new ArrayList<>(Arrays.asList(Constants.MAINTENANCE_REPORT_CATEGORIES));
        maintenanceReport = new MaintenanceReport();
        maintenanceReport.setStatus(0);
    }

    public void createMaintenanceReport() {
        try {
            maintenanceReport.setUser(userBean.getUser(authManagedBean.getUserId()));
            maintenanceReport.setMaintenanceSlot(aircraftMaintenanceSlotBean.getSlot(aircraftMaintenanceSlotId));
            maintenanceReport = operationsReportingBean.createMaintenanceReport(maintenanceReport);
            authManagedBean.createAuditLog("Created new maintenance report: " + maintenanceReport.getMaintenanceSlot().getAirport() + " - " + maintenanceReport.getId() + " @ ", "create_maintenance_report");
            FacesMessage m = new FacesMessage("Maintenance report created successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("Maintenance report could not be created.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);        }
    }

    public AuthManagedBean getAuthManagedBean() {
        return authManagedBean;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public MaintenanceReport getMaintenanceReport() {
        return maintenanceReport;
    }

    public void setMaintenanceReport(MaintenanceReport maintenanceReport) {
        this.maintenanceReport = maintenanceReport;
    }

    public List<AircraftMaintenanceSlot> getCrewMaintenanceSlots() {
        return crewMaintenanceSlots;
    }

    public void setCrewMaintenanceSlots(List<AircraftMaintenanceSlot> crewMaintenanceSlots) {
        this.crewMaintenanceSlots = crewMaintenanceSlots;
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

    public long getAircraftMaintenanceSlotId() {
        return aircraftMaintenanceSlotId;
    }

    public void setAircraftMaintenanceSlotId(long aircraftMaintenanceSlotId) {
        this.aircraftMaintenanceSlotId = aircraftMaintenanceSlotId;
    }
}
