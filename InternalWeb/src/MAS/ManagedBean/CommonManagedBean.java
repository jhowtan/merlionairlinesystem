package MAS.ManagedBean;

import MAS.Common.Utils;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ManagedBean
public class CommonManagedBean {

    public String getRoot() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }

    public String truncate(String str, int chars) {
        return Utils.truncate(str, chars);
    }

    public String formatDate(String format, Date date) {
        if (date == null) {
            date = new Date();
        }
        try {
            return new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public List truncateList(List list, int size) {
        return list.subList(0, Math.min(size, list.size()));
    }

    public String makeParagraph(String string) {
        return string.replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>");
    }

    public void redirect(String path) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(getRoot() + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String displayDouble(double value, String format) {
        DecimalFormat formatter = new DecimalFormat(format);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        return formatter.format(value);
    }

}
