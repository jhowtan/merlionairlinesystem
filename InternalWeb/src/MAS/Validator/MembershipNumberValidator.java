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
public class MembershipNumberValidator implements Validator {

    private static final String MEMBERSHIP_NUMBER_PATTERN = "^\\d{8}$";

    private Pattern pattern;

    public MembershipNumberValidator() {
        pattern = Pattern.compile(MEMBERSHIP_NUMBER_PATTERN);
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        if(!pattern.matcher(o.toString()).matches()) {
            FacesMessage m = new FacesMessage("Please enter a valid membership number.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
    }
}
