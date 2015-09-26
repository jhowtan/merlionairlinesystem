package MAS.ManagedBean;

import MAS.Bean.BookingClassBean;
import MAS.Entity.BookingClass;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
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
        }
    }


    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
