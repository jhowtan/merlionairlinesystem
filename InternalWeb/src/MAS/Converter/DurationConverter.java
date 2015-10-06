package MAS.Converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FacesConverter("durationConverter")
public class DurationConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        Pattern p = Pattern.compile("^(\\d+):([0-5][0-9])$");
        Matcher m = p.matcher(s);

        if (!m.matches()) {
            FacesMessage msg = new FacesMessage("Invalid duration entered.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }
        m.reset();
        int mins = 0;
        while (m.find()) {
            mins = Integer.parseInt(m.group(1)) * 60 + Integer.parseInt(m.group(2));
            if (mins == 0) {
                FacesMessage msg = new FacesMessage("Duration must be greater than zero.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ConverterException(msg);
            }
        }
        return mins;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        try {
            int mins = (int) o;
            return (mins / 60) + ":" + new DecimalFormat("00").format(mins % 60);
        } catch (Exception e) {
            return "";
        }
    }
}
