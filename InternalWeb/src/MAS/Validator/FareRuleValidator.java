package MAS.Validator;

import MAS.Bean.FareRuleBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator
public class FareRuleValidator implements Validator {
    @EJB
    private FareRuleBean fareRuleBean;

    private Pattern pattern;

    private static final String FARE_RULE_PATTERN = "^[a-zA-Z0-9-]+$";

    public FareRuleValidator() {
        pattern = Pattern.compile(FARE_RULE_PATTERN);
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        if(!pattern.matcher(o.toString()).matches()) {
            FacesMessage m = new FacesMessage("The name has to be made up of alphanumeric characters and dashes.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if(!fareRuleBean.isFareRuleNameUnique(o.toString())) {
            FacesMessage m = new FacesMessage("Another fare rule exists with the same name.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
    }
}
