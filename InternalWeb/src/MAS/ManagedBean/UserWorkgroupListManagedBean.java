package MAS.ManagedBean;

import MAS.Bean.UserBean;
import MAS.Entity.User;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@ManagedBean
public class UserWorkgroupListManagedBean {
    @EJB
    UserBean userBean;

    public void search(String query) {
        List<User> users = userBean.searchForUser(query);

        String json = "{}";

        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
        response.setContentLength(json.length());
        response.setContentType("application/json");

        try {
            response.getOutputStream().write(json.getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ctx.responseComplete();
    }

}
