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
public class AircraftTypeValidator implements Validator {
    @EJB
    private FleetBean fleetBean;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        String typeName = o.toString();
        if(Pattern.compile("[^a-zA-Z0-9 -]").matcher(typeName).find()) {
            FacesMessage m = new FacesMessage("Please choose an aircraft type name containing only alphanumeric characters, dashes and spaces.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if(!fleetBean.isAircraftTypeNameUnique(typeName)) {
            FacesMessage m = new FacesMessage("This aircraft type name already exists.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
    }
}
