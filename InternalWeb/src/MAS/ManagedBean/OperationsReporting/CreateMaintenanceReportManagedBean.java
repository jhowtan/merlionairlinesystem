package MAS.ManagedBean.OperationsReporting;

import MAS.Bean.AircraftMaintenanceSlotBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Bean.OperationsReportingBean;
import MAS.Entity.AircraftMaintenanceSlot;
import MAS.Entity.MaintenanceReport;
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
public class CreateMaintenanceReportManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    FlightScheduleBean flightScheduleBean;

    @EJB
    AircraftMaintenanceSlotBean aircraftMaintenanceSlotBean;

    @EJB
    OperationsReportingBean operationsReportingBean;

    private List<AircraftMaintenanceSlot> aircraftMaintenanceSlots;
    private List<String> statusList;
    private List<String> categoryList;

    private AircraftMaintenanceSlot aircraftMaintenanceSlot;
    private MaintenanceReport maintenanceReport;

    @PostConstruct
    public void init() {
        aircraftMaintenanceSlots = aircraftMaintenanceSlotBean.getAllSlots();
        statusList = new ArrayList<>();
        categoryList = new ArrayList<>();
        statusList.add("OK");
        statusList.add("NOT OK");
        statusList.add("UNDIAGNOSED");
        categoryList.add("Minor");
        categoryList.add("Major");
    }

    public void createMaintenanceReport() {
        maintenanceReport = operationsReportingBean.createMaintenanceReport(maintenanceReport);
        authManagedBean.createAuditLog("Created new maintenance report: " + maintenanceReport.getMaintenanceSlot().getAirport() + " - " + maintenanceReport.getId() + " @ ", "create_maintenance_report");
        FacesMessage m = new FacesMessage("Flight report created for" + maintenanceReport.getMaintenanceSlot().toString() + " successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
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

    public AircraftMaintenanceSlot getAircraftMaintenanceSlot() {
        return aircraftMaintenanceSlot;
    }

    public void setAircraftMaintenanceSlot(AircraftMaintenanceSlot aircraftMaintenanceSlot) {
        this.aircraftMaintenanceSlot = aircraftMaintenanceSlot;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public List<AircraftMaintenanceSlot> getAircraftMaintenanceSlots() {
        return aircraftMaintenanceSlots;
    }

    public void setAircraftMaintenanceSlots(List<AircraftMaintenanceSlot> aircraftMaintenanceSlots) {
        this.aircraftMaintenanceSlots = aircraftMaintenanceSlots;
    }
}
