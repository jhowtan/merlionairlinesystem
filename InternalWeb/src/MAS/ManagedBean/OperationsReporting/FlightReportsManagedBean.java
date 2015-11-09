package MAS.ManagedBean.OperationsReporting;

import MAS.Bean.OperationsReportingBean;
import MAS.Common.Constants;
import MAS.Common.Permissions;
import MAS.Entity.FlightReport;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
@ViewScoped
public class FlightReportsManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    private OperationsReportingBean operationsReportingBean;
    private FlightReport selectedFlightReport;

    public String categoryToString(int category) {
        return Constants.FLIGHT_REPORT_CATEGORIES[category];
    }

    public void updateReportStatus(int status) {
        try {
            System.out.println(selectedFlightReport.getId() + " " + status);
            operationsReportingBean.updateFlightReportStatus(selectedFlightReport.getId(), status);
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("Cannot acknowledge/resolve a flight report that does not exist!");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void viewSelectedReport(long id) {
        try {
            selectedFlightReport = operationsReportingBean.getFlightReport(id);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
    public boolean isOpsReportManager() {
        return authManagedBean.hasPermission(Permissions.MANAGE_OPERATIONS_REPORTING);
    }

    public boolean displayDetails() {
        return isOpsReportManager() && selectedFlightReport != null;
    }

    public List<FlightReport> getAllFlightReports() {
        if (isOpsReportManager())
            return operationsReportingBean.getAllFlightReports();
        else
            return operationsReportingBean.getFlightReportsByUser(authManagedBean.getUserId());
    }

    public FlightReport getSelectedFlightReport() {
        return selectedFlightReport;
    }

    public void setSelectedFlightReport(FlightReport selectedFlightReport) {
        this.selectedFlightReport = selectedFlightReport;
    }

    public AuthManagedBean getAuthManagedBean() {
        return authManagedBean;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
