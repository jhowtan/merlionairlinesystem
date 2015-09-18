package MAS.Validator;

import MAS.Bean.WorkgroupBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator
public class WorkgroupValidator implements Validator {
    @EJB
    private WorkgroupBean workgroupBean;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        String workgroupName = o.toString();
        if(Pattern.compile("[^a-zA-Z0-9 ]").matcher(workgroupName).find()) {
            FacesMessage m = new FacesMessage("Please choose a workgroup name containing only alphanumeric characters and spaces.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if(!workgroupBean.isNameUnique(workgroupName)) {
            FacesMessage m = new FacesMessage("This workgroup name already exists.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
    }
}

