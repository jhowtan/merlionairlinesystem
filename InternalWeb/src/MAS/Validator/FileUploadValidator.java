package MAS.Validator;

import MAS.Common.Constants;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

@FacesValidator
public class FileUploadValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        Part file = (Part) o;

        if (file == null || file.getSize() <= 0 || file.getContentType().isEmpty()) {
            FacesMessage m = new FacesMessage("Please select a valid file to upload.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
        if (!file.getContentType().endsWith("pdf")) {
            FacesMessage m = new FacesMessage("Please upload a valid PDF file." );
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(m);
        }
    }
}
