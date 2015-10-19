package MAS.ManagedBean.Auth;

import MAS.Bean.UserBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.Map;

@ManagedBean
public class ResetPasswordManagedBean {
    @EJB
    private UserBean userBean;

    private boolean resetHashValid = false;
    private boolean processed = false;
    private boolean processedSuccess;
    private long userId;
    private String resetHash;

    @PostConstruct
    public void init() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("u") && params.containsKey("h")) {
            try {
                userId = Long.valueOf(params.get("u"));
                resetHash = (String) params.get("h");
                resetHashValid = userBean.isResetHashValid(userId, resetHash);
            } catch (NumberFormatException e) {
            }
        }
    }

    public void resetPassword(String newPassword) {
        try {
            processed = true;
            userBean.resetPassword(userId, resetHash, newPassword);
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
