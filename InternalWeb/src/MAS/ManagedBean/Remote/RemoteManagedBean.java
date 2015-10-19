package MAS.ManagedBean.Remote;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.UserBean;
import MAS.Entity.ETicket;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;
import MAS.ManagedBean.DepartureControl.CheckInManagedBean;
import MAS.WebSocket.WebSocketMessage;
import com.google.gson.Gson;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ManagedBean
public class RemoteManagedBean {
    @EJB
    UserBean userBean;

    @EJB
    FlightScheduleBean flightScheduleBean;

    @ManagedProperty(value="#{checkInManagedBean}")
    CheckInManagedBean checkInManagedBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;


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

    public void gateCheck() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String eticketId = params.get("eticket");

        HashMap<String, String> passengerReturn = new HashMap<>();

        try {
            ETicket eticket = flightScheduleBean.getETicket(Long.parseLong(eticketId));
            if (eticket.isCheckedIn()) {
                passengerReturn.put("status", "success");
                passengerReturn.put("passengerName", eticket.getPassengerName());
                passengerReturn.put("seatNumber", checkInManagedBean.getNiceSeatName(eticket));
                eticket.setGateChecked(true);
                flightScheduleBean.updateETicket(eticket);
                authManagedBean.createAuditLog("Processed passenger for boarding: " + eticket.getPassengerName(), "gate_check");


                try {
                    WebSocketMessage.broadcastToChannel("gateCheckUpdate", "{\"boarded\": true, \"passenger\": \"" + eticket.getPassengerName() + "\"}");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                passengerReturn.put("status", "error");
                passengerReturn.put("error", "ETicket has not been checked in!");
                System.out.println("ETicket has not been checked in!");
            }

        } catch (NotFoundException e) {
            passengerReturn.put("status", "error");
            passengerReturn.put("error", "Cannot find eTicket with the supplied ID!");
            System.out.println("Cannot find eTicket with the supplied ID!");
        }

        Gson gson = new Gson();
        String json = gson.toJson(passengerReturn);

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

    public CheckInManagedBean getCheckInManagedBean() {
        return checkInManagedBean;
    }

    public void setCheckInManagedBean(CheckInManagedBean checkInManagedBean) {
        this.checkInManagedBean = checkInManagedBean;
    }

    public AuthManagedBean getAuthManagedBean() {
        return authManagedBean;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
