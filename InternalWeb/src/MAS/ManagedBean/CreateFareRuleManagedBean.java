package MAS.ManagedBean;

import MAS.Bean.FareRuleBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

@ManagedBean
public class CreateFareRuleManagedBean {
    @EJB
    FareRuleBean fareRuleBean;


    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String name;
    private int minimumStay;
    private int maximumStay;
    private int advancePurchase;
    private int minimumPassengers;
    private int milesAccrual;
    private boolean freeCancellation;

    public CreateFareRuleManagedBean() {
        resetFields();
    }

    public void resetFields() {
        name = "";
        minimumStay = 0;
        maximumStay = 0;
        advancePurchase = 0;
        minimumPassengers = 0;
        milesAccrual = 100;
        freeCancellation = true;
    }

    public void createFareRule() {
        fareRuleBean.createFareRule(name, minimumStay, maximumStay, advancePurchase, minimumPassengers, milesAccrual, freeCancellation);

        authManagedBean.createAuditLog("Created new fare rule: " + name, "create_fare_rule");

        FacesMessage m = new FacesMessage("Fare rule created successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);

        resetFields();
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinimumStay() {
        return minimumStay;
    }

    public void setMinimumStay(int minimumStay) {
        this.minimumStay = minimumStay;
    }

    public int getMaximumStay() {
        return maximumStay;
    }

    public void setMaximumStay(int maximumStay) {
        this.maximumStay = maximumStay;
    }

    public int getAdvancePurchase() {
        return advancePurchase;
    }

    public void setAdvancePurchase(int advancePurchase) {
        this.advancePurchase = advancePurchase;
    }

    public int getMinimumPassengers() {
        return minimumPassengers;
    }

    public void setMinimumPassengers(int minimumPassengers) {
        this.minimumPassengers = minimumPassengers;
    }

    public int getMilesAccrual() {
        return milesAccrual;
    }

    public void setMilesAccrual(int milesAccrual) {
        this.milesAccrual = milesAccrual;
    }

    public boolean isFreeCancellation() {
        return freeCancellation;
    }

    public void setFreeCancellation(boolean freeCancellation) {
        this.freeCancellation = freeCancellation;
    }
}
