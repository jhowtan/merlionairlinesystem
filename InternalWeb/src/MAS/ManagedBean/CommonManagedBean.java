package MAS.ManagedBean;

import MAS.Bean.UserBean;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class CommonManagedBean {

    public String getRoot() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();

    }

}
