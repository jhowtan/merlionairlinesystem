package MAS.ManagedBean.ManagementReporting;

import MAS.Bean.AircraftMaintenanceSlotBean;
import MAS.Bean.FlightRosterBean;
import MAS.Bean.UserBean;
import MAS.Entity.User;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
public class CrewReportManagedBean {
    @EJB
    UserBean userBean;
    @EJB
    FlightRosterBean flightRosterBean;
    @EJB
    AircraftMaintenanceSlotBean aircraftMaintenanceSlotBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private List<String> jobs;
    private List<String> jobInputs;
    private String jobIds;
    private List<User> crewList;

    @PostConstruct
    public void init() {
        crewList = new ArrayList<>();
        jobs = new ArrayList<>();
        jobs.add("Cabin Crew");
        jobs.add("Cockpit Crew");
        jobs.add("Maintenance Crew");
    }

    public void filter() {
        jobIds = "";
        List<Integer> jobsConverted = new ArrayList<>();
        crewList = new ArrayList<>();
        for (int i=0; i < jobInputs.size(); i++) {
            if (jobInputs.get(i).equals("Cabin Crew")) {
                jobsConverted.add(3);
                crewList.addAll(userBean.getUsersWithJobs(3));
            }
            else if (jobInputs.get(i).equals("Cockpit Crew")) {
                jobsConverted.add(4);
                crewList.addAll(userBean.getUsersWithJobs(4));
            }
            else if (jobInputs.get(i).equals("Maintenance Crew")) {
                jobsConverted.add(5);
                crewList.addAll(userBean.getUsersWithJobs(5));
            }
        }
        for (int i = 0; i < jobsConverted.size(); i++) {
            jobIds = jobIds.concat(jobsConverted.get(i).toString()).concat("-");
        }
    }

    public String getJobString(int job) {
        switch(job) {
            case 3:
                return "Cabin Crew";
            case 4:
                return "Cockpit Crew";
            case 5:
                return "Maintenance Crew";
            default:
                return "";
        }
    }

    public List<String> getJobs() {
        return jobs;
    }

    public void setJobs(List<String> jobs) {
        this.jobs = jobs;
    }

    public List<String> getJobInputs() {
        return jobInputs;
    }

    public void setJobInputs(List<String> jobInputs) {
        this.jobInputs = jobInputs;
    }

    public AuthManagedBean getAuthManagedBean() {
        return authManagedBean;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getJobIds() {
        return jobIds;
    }

    public void setJobIds(String jobIds) {
        this.jobIds = jobIds;
    }

    public List<User> getCrewList() {
        return crewList;
    }

    public void setCrewList(List<User> crewList) {
        this.crewList = crewList;
    }
}
