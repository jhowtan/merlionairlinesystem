package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.CampaignBean;
import MAS.Bean.MailBean;
import MAS.Entity.CampaignGroup;
import MAS.Entity.Customer;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.Map;

@ManagedBean
@ViewScoped
public class SendMailManagedBean {
    @EJB
    MailBean mailBean;
    @EJB
    CampaignBean campaignBean;

    private Map<String,String> params;
    private long campaignGroupId;
    private CampaignGroup campaignGroup;

    private String email;
    private String subject;
    private String body;

    @PostConstruct
    private void init() {
        try {
            params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            campaignGroupId = Long.parseLong(params.get("cmGrpId"));
            campaignGroup = campaignBean.getCampaignGroup(campaignGroupId);
        } catch (Exception e) {
        }
    }

    public void sendMail() {
        try {
            for (Customer customer : campaignGroup.getCustomers()) {
                mailBean.send(customer.getEmail(), customer.getDisplayName(), subject, body.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " "), body);
            }
            FacesMessage m = new FacesMessage("Mails sent successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Mail could not be sent.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public long getCampaignGroupId() {
        return campaignGroupId;
    }

    public void setCampaignGroupId(long campaignGroupId) {
        this.campaignGroupId = campaignGroupId;
    }

    public CampaignGroup getCampaignGroup() {
        return campaignGroup;
    }

    public void setCampaignGroup(CampaignGroup campaignGroup) {
        this.campaignGroup = campaignGroup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
