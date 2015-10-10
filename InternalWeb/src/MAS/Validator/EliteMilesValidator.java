package MAS.Validator;

import MAS.Common.Constants;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectOne;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator
public class EliteMilesValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        int eliteMiles = (int) o;
        HtmlSelectOneMenu tierComponent = (HtmlSelectOneMenu) uiComponent.getAttributes().get("tier");
        int tier = (int) tierComponent.getValue();

        if (eliteMiles < 0) {
            FacesMessage m = new FacesMessage("Please enter a valid number of elite miles");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if (tier == Constants.FFP_TIER_BLUE && eliteMiles >= 25000) {
            FacesMessage m = new FacesMessage("An " + Constants.FFP_TIER_BLUE_LABEL + " customer must have less than 25000 elite miles" );
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if (tier == Constants.FFP_TIER_SILVER && eliteMiles >= 50000) {
            FacesMessage m = new FacesMessage("An " + Constants.FFP_TIER_SILVER_LABEL + " customer must have less than 50000 elite miles" );
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        System.out.println("X" + tier + "X" + o);


    }
}
