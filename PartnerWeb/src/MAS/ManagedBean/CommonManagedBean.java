package MAS.ManagedBean;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@ManagedBean
public class CommonManagedBean {

    public String getRoot() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }

    public static String formatDate(String format, Date date) {
        if (date == null) {
            date = new Date();
        }
        try {
            return new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatDate(String format, XMLGregorianCalendarImpl date) {
        if (date == null) return formatDate(format, new Date());
        return formatDate(format, date.toGregorianCalendar().getTime());
    }

    public static String displayDouble(double value, String format) {
        DecimalFormat formatter = new DecimalFormat(format);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        return formatter.format(value);
    }

}
