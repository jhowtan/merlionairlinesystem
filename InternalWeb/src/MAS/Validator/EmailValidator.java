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
public class EmailValidator implements Validator {
    @EJB
    private UserBean userBean;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\." +
            "[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" +
            "(\\.[A-Za-z]{2,})$";

    private Pattern pattern;

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        if(!pattern.matcher(o.toString()).matches()) {
            FacesMessage m = new FacesMessage("Please enter a valid email address.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if(!userBean.isEmailUnique(o.toString())) {
            FacesMessage m = new FacesMessage("This email is already associated with a different account.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
    }
}
