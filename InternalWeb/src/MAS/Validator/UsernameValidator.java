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
public class UsernameValidator implements Validator {
    @EJB
    private UserBean userBean;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        String username = o.toString().toLowerCase();
        if(username.length() < 3) {
            FacesMessage m = new FacesMessage("Please choose a longer username.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if(username.length() > 15) {
            FacesMessage m = new FacesMessage("Please choose a shorter username.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if(Pattern.compile("[^a-z0-9]").matcher(username).find()) {
            FacesMessage m = new FacesMessage("Please choose a username containing only alphanumeric characters.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if(!userBean.isUsernameUnique(username)) {
            FacesMessage m = new FacesMessage("This username is already in use.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
    }
}
