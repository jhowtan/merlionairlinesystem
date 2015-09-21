package MAS.ManagedBean;

import MAS.Bean.UserBean;
import MAS.Bean.WorkgroupBean;
import MAS.Entity.User;
import MAS.Entity.Workgroup;
import com.google.gson.Gson;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
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

    private class UserResult {
        public long id;
        public String username;
        public String fullName;
    }

    private class WorkgroupResult {
        public long id;
        public String name;
    }

    private class SearchResults {
        public List<UserResult> users;
        public List<WorkgroupResult> workgroups;
        public SearchResults() {
            users = new ArrayList<>();
            workgroups = new ArrayList<>();
        }
    }

    public void search() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String query = params.get("q");

        List<User> users = userBean.searchForUser(query);
        List<Workgroup> workgroups = workgroupBean.searchForWorkgroup(query);

        SearchResults searchResults = new SearchResults();

        for (User user : users) {
            UserResult userResult = new UserResult();
            userResult.id = user.getId();
            userResult.username = user.getUsername();
            userResult.fullName = user.getFirstName() + " " + user.getLastName();
            searchResults.users.add(userResult);
        }

        for (Workgroup workgroup : workgroups) {
            WorkgroupResult workgroupResult = new WorkgroupResult();
            workgroupResult.id = workgroup.getId();
            workgroupResult.name = workgroup.getName();
            searchResults.workgroups.add(workgroupResult);
        }

        Gson gson = new Gson();
        String json = gson.toJson(searchResults);

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
