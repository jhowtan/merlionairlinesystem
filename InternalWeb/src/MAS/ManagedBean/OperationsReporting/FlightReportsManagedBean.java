package MAS.ManagedBean.OperationsReporting;

import MAS.Bean.OperationsReportingBean;
import MAS.Common.Permissions;
import MAS.Entity.FlightReport;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.List;

@ManagedBean
public class FlightReportsManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    private OperationsReportingBean operationsReportingBean;
    private FlightReport selectedFlightReport;

    @PostConstruct
    public void init() {
    }

    public boolean isOpsReportManager() {
        return authManagedBean.hasPermission(Permissions.MANAGE_OPERATIONS_REPORTING);
    }

    public List<FlightReport> getAllFlightReports() {
        return operationsReportingBean.getAllFlightReports();
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
