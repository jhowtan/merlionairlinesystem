package MAS.Validator;

import MAS.Bean.FleetBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator
public class TailNumberValidator implements Validator {
    @EJB
    private FleetBean fleetBean;

    private static final String EMAIL_PATTERN = "^9V-[A-Z]{3}$";

    private Pattern pattern;

    public TailNumberValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        if(!pattern.matcher(o.toString()).matches()) {
            FacesMessage m = new FacesMessage("The tail number must be of a proper format 9V-AAA to 9V-ZZZ.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if(!fleetBean.isTailNumberUnique(o.toString())) {
            FacesMessage m = new FacesMessage("This tail number is already assigned to another aircraft.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
    }
}
