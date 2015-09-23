package MAS.ManagedBean;

import MAS.Bean.UserBean;
import MAS.Bean.WorkgroupBean;
import MAS.Entity.User;
import MAS.Entity.Workgroup;
import com.google.gson.Gson;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
public class UserWorkgroupListManagedBean {
    @EJB
    UserBean userBean;
    @EJB
    WorkgroupBean workgroupBean;
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    private class SearchResult {
        public String value;
        public String label;
    }

    public void search() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String query = params.get("q");

        List<User> users = userBean.searchForUser(query);
        List<Workgroup> workgroups = workgroupBean.searchForWorkgroup(query);

        ArrayList<SearchResult> searchResults = new ArrayList<>();

        for (User user : users) {
            SearchResult r = new SearchResult();
            r.label = user.getFirstName() + " " + user.getLastName() + " (" + user.getUsername() + ")";
            r.value = "user:" + user.getId() + ":" + r.label;
            searchResults.add(r);
        }

        for (Workgroup workgroup : workgroups) {
            SearchResult r = new SearchResult();
            r.label = workgroup.getName();
            r.value = "workgroup:" + workgroup.getId() + ":" + r.label;
            searchResults.add(r);
        }

        Gson gson = new Gson();
        String json = gson.toJson(searchResults);

        if(!authManagedBean.isAuthenticated()) {
            json = "[]";
        }

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
