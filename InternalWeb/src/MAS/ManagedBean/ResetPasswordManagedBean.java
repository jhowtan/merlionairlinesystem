package MAS.ManagedBean;

import MAS.Bean.UserBean;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;
import org.jboss.weld.context.http.Http;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@ManagedBean
public class ResetPasswordManagedBean {
    @EJB
    private UserBean userBean;

    private boolean resetHashValid = false;
    private boolean processed = false;
    private boolean processedSuccess;
    private long userId;
    private String hash;

    @PostConstruct
    public void init() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("u") && params.containsKey("h")) {
            try {
                userId = Long.valueOf(params.get("u")).longValue();
                hash = (String) params.get("h");
                resetHashValid = userBean.isResetHashValid(userId, hash);
            } catch (NumberFormatException e) {
            }
        }
    }

    public void resetPassword(String newPassword) {
        try {
            processed = true;
            userBean.resetPassword(userId, hash, newPassword);
            processedSuccess = true;
        } catch (Exception e) {
            processedSuccess = false;
        }
    }

    public boolean isResetHashValid() {
        return resetHashValid;
    }

    public void setResetHashValid(boolean resetHashValid) {
        this.resetHashValid = resetHashValid;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean isProcessedSuccess() {
        return processedSuccess;
    }

    public void setProcessedSuccess(boolean processedSuccess) {
        this.processedSuccess = processedSuccess;
    }
}
