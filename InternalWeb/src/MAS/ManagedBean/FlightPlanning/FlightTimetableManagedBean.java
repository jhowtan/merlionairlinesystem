package MAS.ManagedBean.FlightPlanning;

import MAS.Bean.AircraftMaintenanceSlotBean;
import MAS.Bean.FleetBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Entity.Aircraft;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;
import com.google.gson.Gson;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
public class FlightTimetableManagedBean {
    @EJB
    FleetBean fleetBean;
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    AircraftMaintenanceSlotBean aircraftMaintenanceSlotBean;
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private List<Aircraft> aircrafts;
    private long aircraftId;
    private Aircraft aircraft;

    @PostConstruct
    private void init() {
        aircrafts = fleetBean.getAllAircraft();
    }

    public long getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(long aircraftId) {
        this.aircraftId = aircraftId;
    }

    public void selectAircraft() {
        try {
            aircraft = fleetBean.getAircraft(aircraftId);
        } catch (NotFoundException e) {
            //No aircraft
        }
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    private class SearchResult {
        public String value;
        public String label;
    }

    public void search() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String query = params.get("q");

//        List<User> users = userBean.searchForUser(query);
//        List<Workgroup> workgroups = workgroupBean.searchForWorkgroup(query);
//
//        ArrayList<SearchResult> searchResults = new ArrayList<>();
//
//        for (User user : users) {
//            SearchResult r = new SearchResult();
//            r.label = user.getFirstName() + " " + user.getLastName() + " (" + user.getUsername() + ")";
//            r.value = "user:" + user.getId() + ":" + r.label;
//            searchResults.add(r);
//        }
//
//        for (Workgroup workgroup : workgroups) {
//            SearchResult r = new SearchResult();
//            r.label = workgroup.getName();
//            r.value = "workgroup:" + workgroup.getId() + ":" + r.label;
//            searchResults.add(r);
//        }

        Gson gson = new Gson();
        String json = gson.toJson(null);//searchResults);

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

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public List<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(List<Aircraft> aircrafts) {
        this.aircrafts = aircrafts;
    }
}
