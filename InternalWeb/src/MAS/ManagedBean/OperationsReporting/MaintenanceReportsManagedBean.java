package MAS.ManagedBean.OperationsReporting;

import MAS.Bean.OperationsReportingBean;
import MAS.Common.Constants;
import MAS.Common.Permissions;
import MAS.Entity.MaintenanceReport;
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
public class MaintenanceReportsManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    private OperationsReportingBean operationsReportingBean;
    private MaintenanceReport selectedMaintenanceReport;

    public String categoryToString(int category) {
        return Constants.MAINTENANCE_REPORT_CATEGORIES[category];
    }

    public void updateReportStatus(int status) {
        try {
            operationsReportingBean.updateMaintenanceReportStatus(selectedMaintenanceReport.getId(), status);
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("Cannot acknowledge/resolve a maintenance report that does not exist!");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void viewSelectedReport(long id) {
        try {
            selectedMaintenanceReport = operationsReportingBean.getMaintenanceReport(id);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
    public boolean isOpsReportManager() {
        return authManagedBean.hasPermission(Permissions.MANAGE_OPERATIONS_REPORTING);
    }

    public boolean displayDetails() {
        return isOpsReportManager() && selectedMaintenanceReport != null;
    }

    public List<MaintenanceReport> getAllMaintenanceReports() {
        if (isOpsReportManager())
            return operationsReportingBean.getAllMaintenanceReports();
        else
            return operationsReportingBean.getMaintenanceReportsByUser(authManagedBean.getUserId());
    }

    public MaintenanceReport getSelectedMaintenanceReport() {
        return selectedMaintenanceReport;
    }

    public void setSelectedMaintenanceReport(MaintenanceReport selectedMaintenanceReport) {
        this.selectedMaintenanceReport = selectedMaintenanceReport;
    }

    public AuthManagedBean getAuthManagedBean() {
        return authManagedBean;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
