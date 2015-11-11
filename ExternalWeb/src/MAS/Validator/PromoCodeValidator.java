package MAS.Validator;

import MAS.Bean.CampaignBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator
public class PromoCodeValidator implements Validator {
    @EJB
    private CampaignBean campaignBean;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        if (o.toString().isEmpty()) return;
       if (! campaignBean.validateCode(o.toString(), (Long) uiComponent.getAttributes().get("customerId"))) {
           FacesMessage m = new FacesMessage("The promotional code is not valid.");
           m.setSeverity(FacesMessage.SEVERITY_ERROR);
           throw new ValidatorException(m);
       }
    }
}
