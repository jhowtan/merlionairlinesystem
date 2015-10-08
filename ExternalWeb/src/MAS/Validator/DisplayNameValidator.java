package MAS.Validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator
public class DisplayNameValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        String displayName = o.toString().toLowerCase();
        if(displayName.length() < 3) {
            FacesMessage m = new FacesMessage("Please choose a longer display name.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if(displayName.length() > 25) {
            FacesMessage m = new FacesMessage("Please try again. The display name on your card can only fit 25 characters.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
    }
}
