package MAS.ManagedBean;

import MAS.Common.Utils;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.text.SimpleDateFormat;
import java.util.Date;

@ManagedBean
public class CommonManagedBean {

    public String getRoot() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }

    public String truncate(String str, int chars) {
        return Utils.truncate(str, chars);
    }

    public String formatDate(String format, Date date) {
        return new SimpleDateFormat(format).format(date);
    }

}
