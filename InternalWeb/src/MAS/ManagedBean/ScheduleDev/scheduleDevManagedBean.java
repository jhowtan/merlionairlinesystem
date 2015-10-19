package MAS.ManagedBean.ScheduleDev;

import MAS.Bean.FleetBean;
import MAS.Bean.RouteBean;
import MAS.Bean.ScheduleDevelopmentBean;
import MAS.Common.Utils;
import MAS.Entity.Aircraft;
import MAS.Entity.Airport;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.lang.model.type.NoType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
@SessionScoped
public class scheduleDevManagedBean {
    @EJB
    ScheduleDevelopmentBean scheduleDevelopmentBean;
    @EJB
    FleetBean fleetBean;
    @EJB
    RouteBean routeBean;

    private List<Airport> allAirports;
    private List<String> selectAirportsId;
    private List<Airport> selectAirports;
    private List<String> hubAirportsId;
    private List<Airport> hubAirports;
    private List<Double> hubStrengths;
    private String[] hubStrInputs;

    private List<Aircraft> allAircrafts;
    private List<String> selectAircraftsId;
    private List<Aircraft> selectAircrafts;
    private String[] acLocInputs;
    private List<Airport> acLocations;

    private Date startDate;
    private String startTime;
    private Date start;
    private int step = 0;

    @PostConstruct
    private void init() {
        setAllAirports(routeBean.getAllAirports());
        setAllAircrafts(fleetBean.getAllAircraft());
        selectAirportsId = new ArrayList<>();
        selectAircraftsId = new ArrayList<>();
    }

    public void selectAirportAjaxListener(AjaxBehaviorEvent event) {
        selectAirports = new ArrayList<>();
        hubAirports = new ArrayList<>();
        for (int i = 0; i < selectAirportsId.size(); i++) {
            try {
                selectAirports.add(routeBean.getAirport(selectAirportsId.get(i)));
            } catch (NotFoundException e) {
                //Airport not found
            }
        }
    }
    public void hubAirportAjaxListener(AjaxBehaviorEvent event) {
        hubAirports = new ArrayList<>();
        for (int i = 0; i < hubAirportsId.size(); i++) {
            try {
                hubAirports.add(routeBean.getAirport(hubAirportsId.get(i)));
            } catch (NotFoundException e) {
                //Airport not found
            }
        }
        hubStrInputs = new String[hubAirports.size()];
        for (int i = 0; i < hubStrInputs.length; i++)
            hubStrInputs[i] = "0.5";
    }

    public boolean showApButtons() {
        if (selectAirports == null)
            return false;
        else if (selectAirports.size() >= 2)
            return true;
        return false;
    }

    public boolean displayAp(){
        if (step == 0) return true;
        return false;
    }

    public void saveAirports() {
        hubStrengths = new ArrayList<>();
        for (int i = 0; i < hubStrInputs.length; i++) {
            hubStrengths.add(Double.parseDouble(hubStrInputs[i]));
        }
        try {
            List<String> apIds = new ArrayList<>();
            for (int i = 0; i < selectAirports.size(); i++) {
                apIds.add(selectAirports.get(i).getId());
            }
            List<String> hubIds = new ArrayList<>();
            for (int i = 0; i < hubAirports.size(); i++) {
                hubIds.add(hubAirports.get(i).getId());
            }
            scheduleDevelopmentBean.addAirports(apIds);
            scheduleDevelopmentBean.addHubs(hubIds, hubStrengths);
            step = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean displayAc(){
        if (step == 1) return true;
        return false;
    }

    public void selectAircraftsAjaxListener(AjaxBehaviorEvent event) {
        selectAircrafts = new ArrayList<>();
        for (int i = 0; i < selectAircraftsId.size(); i++) {
            try {
                selectAircrafts.add(fleetBean.getAircraft(Long.parseLong(selectAircraftsId.get(i))));
            } catch (NotFoundException e) {
                //Aircraft not found
            }
        }
        acLocInputs = new String[selectAircrafts.size()];
    }
    public boolean showAcButtons() {
        if (selectAircrafts == null)
            return false;
        else if (selectAircrafts.size() >= 1)
            return true;
        return false;
    }

    public void saveAircraft() {
        acLocations = new ArrayList<>();
        for (int i = 0; i < acLocInputs.length; i++) {
            try {
                Airport ap = routeBean.getAirport(acLocInputs[i]);
                acLocations.add(ap);
            } catch (NotFoundException e) {
                //Cannot find airport using the code
            }
        }

        try {
            List<Long> acIds = new ArrayList<>();
            List<String> startingApIds = new ArrayList<>();
            for (int i = 0; i < selectAircrafts.size(); i++) {
                acIds.add(selectAircrafts.get(i).getId());
                startingApIds.add(acLocations.get(i).getId());
            }
            scheduleDevelopmentBean.addAircrafts(acIds, startingApIds);
            step = 2;
        } catch (Exception e) {
            //Schedule development failed
            e.printStackTrace();
        }
    }

    public void saveDateTime() {
        start = Utils.addTimeToDate(startDate, startTime);
        try {
            scheduleDevelopmentBean.testProcess(start, 40320);
            step = 3;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean displayDate(){
        if (step == 2) return true;
        return false;
    }

    public List<Airport> getAllAirports() {
        return allAirports;
    }

    public void setAllAirports(List<Airport> allAirports) {
        this.allAirports = allAirports;
    }

    public List<Airport> getSelectAirports() {
        return selectAirports;
    }

    public void setSelectAirports(List<Airport> selectAirports) {
        this.selectAirports = selectAirports;
    }

    public List<Airport> getHubAirports() {
        return hubAirports;
    }

    public void setHubAirports(List<Airport> hubAirports) {
        this.hubAirports = hubAirports;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public List<String> getSelectAirportsId() {
        return selectAirportsId;
    }

    public void setSelectAirportsId(List<String> selectAirportsId) {
        this.selectAirportsId = selectAirportsId;
    }

    public List<String> getHubAirportsId() {
        return hubAirportsId;
    }

    public void setHubAirportsId(List<String> hubAirportsId) {
        this.hubAirportsId = hubAirportsId;
    }

    public List<Aircraft> getAllAircrafts() {
        return allAircrafts;
    }

    public void setAllAircrafts(List<Aircraft> allAircrafts) {
        this.allAircrafts = allAircrafts;
    }

    public List<String> getSelectAircraftsId() {
        return selectAircraftsId;
    }

    public void setSelectAircraftsId(List<String> selectAircraftsId) {
        this.selectAircraftsId = selectAircraftsId;
    }

    public List<Aircraft> getSelectAircrafts() {
        return selectAircrafts;
    }

    public void setSelectAircrafts(List<Aircraft> selectAircrafts) {
        this.selectAircrafts = selectAircrafts;
    }

    public String[] getHubStrInputs() {
        return hubStrInputs;
    }

    public void setHubStrInputs(String[] hubStrInputs) {
        this.hubStrInputs = hubStrInputs;
    }

    public String[] getAcLocInputs() {
        return acLocInputs;
    }

    public void setAcLocInputs(String[] acLocInputs) {
        this.acLocInputs = acLocInputs;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
