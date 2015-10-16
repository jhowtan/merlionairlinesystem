package MAS.ManagedBean.Remote;

import MAS.Bean.UserBean;
import MAS.Entity.User;
import MAS.Exception.InvalidLoginException;
import MAS.Exception.NotFoundException;
import com.google.gson.Gson;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ManagedBean
public class RemoteManagedBean {
    @EJB
    UserBean userBean;

    public void auth() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String username = params.get("username");
        String password = params.get("password");

        HashMap<String, String> authReturn = new HashMap<>();

        try {
            User user = userBean.getUser(userBean.login(username, password));
            authReturn.put("status", "success");
            authReturn.put("displayName", user.getFirstName() + " " + user.getLastName());
            System.out.println("Trying to login as " + username + ": success!");
        } catch (Exception e) {
            authReturn.put("status", "error");
            System.out.println("Trying to login as " + username + ": error!");
        }

        Gson gson = new Gson();
        String json = gson.toJson(authReturn);

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
