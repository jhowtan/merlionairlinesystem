package MAS.ManagedBean.CostManagement;

import MAS.Bean.CostsBean;
import MAS.Bean.FleetBean;
import MAS.Bean.RouteBean;
import MAS.Bean.UserBean;
import MAS.Common.Constants;
import MAS.Entity.Aircraft;
import MAS.Entity.AircraftAssignment;
import MAS.Entity.Cost;
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
import java.util.Arrays;
import java.util.List;

@ManagedBean
@ViewScoped
public class CostManagedBean {
    @EJB
    CostsBean costsBean;
    @EJB
    FleetBean fleetBean;
    @EJB
    RouteBean routeBean;
    @EJB
    UserBean userBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String[] costTypes = Constants.COSTS;
    private List<Cost> displayCosts;
    private List<Aircraft> aircraftList;
    private List<AircraftAssignment> aaList;
    private List<User> userList;

    private double amount;
    private String comments;
    private long assocId;
    private int type;


    @PostConstruct
    private void init() {
        displayCosts = costsBean.getAllCostOfType(type);
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
            setComments(null);
            FacesMessage m = new FacesMessage("Cost created successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
            changeCostsDisplay();
        } catch (NotFoundException e) {
            setAssocId(0);
            setAmount(0);
            setComments(null);
            FacesMessage m = new FacesMessage("Cost could not be created because it already exists");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
            changeCostsDisplay();
        }
    }

    public void delete(long id) {
        try {
            String comments = costsBean.getCost(id).getComments();
            costsBean.removeCost(id);
            authManagedBean.createAuditLog("Deleted cost: " + comments, "delete_cost");
            FacesMessage m = new FacesMessage("Successfully deleted cost: " + comments);
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("The cost cannot be found, or may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
        changeCostsDisplay();
    }

    public void costTypeChangeListener(AjaxBehaviorEvent event) {
        changeCostsDisplay();
    }

    public void changeCostsDisplay() {
        setDisplayCosts(costsBean.getAllCostOfType(type));
    }

    public String displayComments(Cost cost) {
        if (cost.getType() == 1 || cost.getType() == 2) {
            return displayAC(cost.getAssocId()) + " - " + cost.getComments();
        }
        else if (cost.getType() == 0) {
            return displayAA(cost.getAssocId()) + " - " + cost.getComments();
        }
        else if (cost.getType() == 5) {
            return displayUser(cost.getAssocId()) + " - " + cost.getComments();
        }
        else {
            return cost.getComments();
        }
    }

    private String displayAC(long acId) {
        try {
            return fleetBean.getAircraft(acId).getTailNumber();
        } catch (NotFoundException e) {
            return "Aircraft Not Found";
        }
    }

    private String displayAA(long aaId) {
        try {
            return CommonManagedBean.formatAA(routeBean.getAircraftAssignment(aaId));
        } catch (NotFoundException e) {
            return "Aircraft Assignment Not Found";
        }
    }

    private String displayUser(long userId) {
        try {
            return CommonManagedBean.formatName(userBean.getUser(userId));
        } catch (NotFoundException e) {
            return "User Not Found";
        }
    }

    public int costIndex(String val) {
        return Arrays.asList(getCostTypes()).indexOf(val);
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String[] getCostTypes() {
        return costTypes;
    }

    public void setCostTypes(String[] costTypes) {
        this.costTypes = costTypes;
    }

    public List<Cost> getDisplayCosts() {
        return displayCosts;
    }

    public void setDisplayCosts(List<Cost> displayCosts) {
        this.displayCosts = displayCosts;
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
}
