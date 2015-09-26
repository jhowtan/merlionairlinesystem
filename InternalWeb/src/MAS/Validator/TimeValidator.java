package MAS.Validator;

import MAS.Bean.UserBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator
public class TimeValidator implements Validator {
    @EJB
    private UserBean userBean;

    private static final String TIME_PATTERN = "^([01][0-9]|2[0-3]):[0-5][0-9]$";

    private Pattern pattern;

    public TimeValidator() {
        pattern = Pattern.compile(TIME_PATTERN);
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        if(!pattern.matcher(o.toString()).matches()) {
            FacesMessage m = new FacesMessage("Please enter a valid time.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
    }
}
