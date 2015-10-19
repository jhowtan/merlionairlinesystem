package MAS.ManagedBean.ScheduleDev;

import MAS.Bean.FleetBean;
import MAS.Bean.RouteBean;
import MAS.Bean.ScheduleDevelopmentBean;
import MAS.Entity.Aircraft;
import MAS.Entity.Airport;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
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

    private List<Aircraft> allAircrafts;
    private List<Long> selectAircraftsId;
    private List<Aircraft> selectAircrafts;
    private int step = 0;

    @PostConstruct
    private void init() {
        setAllAirports(routeBean.getAllAirports());
        setAllAircrafts(fleetBean.getAllAircraft());
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
        //Save hub str
        step = 1;
    }

    public boolean displayAc(){
        if (step == 1) return true;
        return false;
    }

    public void selectAircraftsAjaxListener(AjaxBehaviorEvent event) {
        selectAircrafts = new ArrayList<>();
        for (int i = 0; i < selectAircraftsId.size(); i++) {
            try {
                Aircraft ac = fleetBean.getAircraft(selectAircraftsId.get(i));
                selectAircrafts.add(ac);
            } catch (NotFoundException e) {
                //Aircraft not found
            }
        }
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

    public List<Long> getSelectAircraftsId() {
        return selectAircraftsId;
    }

    public void setSelectAircraftsId(List<Long> selectAircraftsId) {
        this.selectAircraftsId = selectAircraftsId;
    }

    public List<Aircraft> getSelectAircrafts() {
        return selectAircrafts;
    }

    public void setSelectAircrafts(List<Aircraft> selectAircrafts) {
        this.selectAircrafts = selectAircrafts;
    }
}
