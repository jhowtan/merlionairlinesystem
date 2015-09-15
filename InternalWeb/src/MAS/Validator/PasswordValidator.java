package MAS.Validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator
public class PasswordValidator implements Validator {

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-zA-Z]).{6,})";

    private Pattern pattern;

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        if(!pattern.matcher(o.toString()).matches()) {
            FacesMessage m = new FacesMessage("Your password should have at least 6 characters and should contain one letter and number.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        UIInput confirmPasswordComponent = (UIInput) uiComponent.getAttributes().get("confirmPassword");
        String confirmPasswordValue = (String) confirmPasswordComponent.getSubmittedValue();
        if(!confirmPasswordValue.equals(o.toString())) {
            FacesMessage m = new FacesMessage("The passwords do not match.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
    }
}
