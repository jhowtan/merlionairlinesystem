package MAS.ManagedBean;

import MAS.Bean.UserBean;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.Map;

@ManagedBean
public class ForgotPasswordManagedBean {
    @EJB
    private UserBean userBean;

    private boolean processed = false;

    public void forgotPassword(String usernameEmail) {
        processed = true;
        try {
            userBean.forgotPassword(usernameEmail);
        } catch (NotFoundException e) {
        }
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

}
