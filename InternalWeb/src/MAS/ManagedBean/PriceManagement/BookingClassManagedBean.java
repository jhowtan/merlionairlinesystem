package MAS.ManagedBean.PriceManagement;

import MAS.Bean.BookingClassBean;
import MAS.Common.Cabin;
import MAS.Entity.BookingClass;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class BookingClassManagedBean {
    @EJB
    private BookingClassBean bookingClassBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<BookingClass> getAllBookingClasses() {
        return bookingClassBean.getAllBookingClasses();
    }

    public void delete(long id) {
        try {
            BookingClass bookingClass = bookingClassBean.getBookingClass(id);
            bookingClassBean.removeBookingClass(id);
            authManagedBean.createAuditLog("Deleted booking class: " + bookingClass.getName(), "delete_booking_class");
        } catch (NotFoundException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("The booking class cannot be found, or may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (EJBException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("The booking class you are trying to delete has been open for sale for existing passengers.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setOpen(long id, boolean isOpen) {
        try {
            bookingClassBean.changeOpenStatus(id, isOpen);
            if(isOpen) {
                authManagedBean.createAuditLog("Closed booking class: " + bookingClassBean.getBookingClass(id).getName(), "close_booking_class");
            } else {
                authManagedBean.createAuditLog("Opened booking class: " + bookingClassBean.getBookingClass(id).getName(), "open_booking_class");
            }
        } catch (NotFoundException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("The booking class cannot be found, or may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (EJBException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("The booking class you are trying to modify has existing tickets sold to customers.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getTravelClassName(int travelClass) {
        return Cabin.TRAVEL_CLASSES[travelClass];
    }
}
