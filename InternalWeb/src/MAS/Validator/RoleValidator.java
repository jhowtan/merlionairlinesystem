package MAS.Validator;

import MAS.Bean.RoleBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator
public class RoleValidator implements Validator {
    @EJB
    private RoleBean roleBean;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        String roleName = o.toString();
        if(Pattern.compile("[^a-zA-Z0-9 ]").matcher(roleName).find()) {
            FacesMessage m = new FacesMessage("Please choose a role name containing only alphanumeric characters and spaces.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if(!roleBean.isNameUnique(roleName)) {
            FacesMessage m = new FacesMessage("This role name already exists.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
    }
}
