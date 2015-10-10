package MAS.ManagedBean.CostManagement;

import MAS.Bean.CostsBean;
import MAS.Bean.FleetBean;
import MAS.Bean.RouteBean;
import MAS.Bean.UserBean;
import MAS.Common.Constants;
import MAS.Entity.Aircraft;
import MAS.Entity.AircraftAssignment;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;
import MAS.ManagedBean.CommonManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class CreateCostManagedBean {
    @EJB
    FleetBean fleetBean;
    @EJB
    RouteBean routeBean;
    @EJB
    UserBean userBean;
    @EJB
    CostsBean costsBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String[] costTypes = Constants.COSTS;
    private List<Aircraft> aircraftList;
    private List<AircraftAssignment> aaList;
    private List<User> userList;

    private double amount;
    private String comments;
    private long assocId;
    private int type;
    private Date date;

    @PostConstruct
    private void init() {
        setAircraftList(fleetBean.getAllAircraft());
        setAaList(routeBean.getAllAircraftAssignments());
        setUserList(userBean.getAllUsers());
    }

    public void createCost() {
        try {
            costsBean.createCost(type, amount, comments, assocId);
            authManagedBean.createAuditLog("Created new cost: " + comments, "create_cost");
            setAssocId(0);
            setAmount(0);
            setType(0);
            setComments(null);
            FacesMessage m = new FacesMessage("Cost created successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            setAssocId(0);
            setAmount(0);
            setType(0);
            setComments(null);
            FacesMessage m = new FacesMessage("Cost could not be created");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }

    }

    public int costIndex(String val) {
        return Arrays.asList(costTypes).indexOf(val);
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String[] getCostTypes() {
        return costTypes;
    }

    public void setCostTypes(String[] costTypes) {
        this.costTypes = costTypes;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getAssocId() {
        return assocId;
    }

    public void setAssocId(long assocId) {
        this.assocId = assocId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Aircraft> getAircraftList() {
        return aircraftList;
    }

    public void setAircraftList(List<Aircraft> aircraftList) {
        this.aircraftList = aircraftList;
    }

    public List<AircraftAssignment> getAaList() {
        return aaList;
    }

    public void setAaList(List<AircraftAssignment> aaList) {
        this.aaList = aaList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
