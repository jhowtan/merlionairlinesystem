package MAS.Validator;

import MAS.Entity.User;
import MAS.Entity.Workgroup;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;

@FacesValidator
public class UserWorkgroupValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        ArrayList<User> users = (ArrayList<User>) ((ArrayList) o).get(0);
        ArrayList<Workgroup> workgroups = (ArrayList<Workgroup>) ((ArrayList) o).get(1);
        if(users.size() + workgroups.size() == 0) {
            ((UIInput) uiComponent).resetValue();
            FacesMessage m = new FacesMessage("At least one recipient is required.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
    }
}
