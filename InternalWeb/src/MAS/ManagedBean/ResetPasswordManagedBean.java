package MAS.ManagedBean;

import MAS.Bean.UserBean;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;
import org.jboss.weld.context.http.Http;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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

    @PostConstruct
    public void init() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("u") && params.containsKey("h")) {
            try {
                Long id = Long.valueOf(params.get("u")).longValue();
                String hash = (String) params.get("h");
                resetHashValid = userBean.isResetHashValid(id, hash);
            } catch (NumberFormatException e) {
            }
        }
    }

    public boolean isResetHashValid() {
        return resetHashValid;
    }

    public void setResetHashValid(boolean resetHashValid) {
        this.resetHashValid = resetHashValid;
    }
}
