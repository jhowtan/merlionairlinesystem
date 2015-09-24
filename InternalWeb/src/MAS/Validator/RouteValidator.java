package MAS.Validator;

import MAS.Bean.RouteBean;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator
public class RouteValidator implements Validator {

    @EJB
    RouteBean routeBean;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        UIInput destinationComponent = (UIInput) uiComponent.getAttributes().get("destination");
        long destinationValue = Long.parseLong(destinationComponent.getSubmittedValue().toString());
        long originValue = Long.parseLong(o.toString());
        if (originValue == destinationValue) {
            FacesMessage m = new FacesMessage("The origin and destination cannot be the same.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        try {
            routeBean.findRouteByOriginAndDestination(originValue, destinationValue);
            FacesMessage m = new FacesMessage("This route already exists.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        } catch (NotFoundException e) {
        }
    }
}
