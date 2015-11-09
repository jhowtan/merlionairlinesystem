package MAS.ManagedBean.DepartureControl;

import MAS.Bean.FlightScheduleBean;
import MAS.Entity.Baggage;
import MAS.Entity.ETicket;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
public class PrintBaggageTagManagedBean implements Serializable {
    @EJB
    FlightScheduleBean flightScheduleBean;

    private ETicket eticket;
    private Baggage baggage;
    
    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            eticket = flightScheduleBean.getETicket(Long.parseLong(params.get("eticket")));
            baggage = flightScheduleBean.getBaggageItem(Long.parseLong(params.get("baggage")));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public Baggage getBaggage() {
        return baggage;
    }

    public void setBaggage(Baggage baggage) {
        this.baggage = baggage;
    }

    public ETicket getEticket() {
        return eticket;
    }

    public void setEticket(ETicket eticket) {
        this.eticket = eticket;
    }

    public ETicket getLastSegment() {
        ETicket lastSegment = eticket;
        while (lastSegment.getNextConnection() != null) {
            lastSegment = lastSegment.getNextConnection();
        }
        return lastSegment;
    }

    public List<ETicket> getAllSegments() {
        ArrayList<ETicket> segments = new ArrayList<>();
        segments.add(eticket);
        ETicket segment = eticket;
        while (segment.getNextConnection() != null) {
            segment = segment.getNextConnection();
            segments.add(0, segment);
        }
        return segments;
    }
}