package MAS.Converter;

import MAS.Bean.UserBean;
import MAS.Bean.WorkgroupBean;
import MAS.Entity.User;
import MAS.Entity.Workgroup;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.ArrayList;
import java.util.Arrays;

@FacesConverter("userWorkgroupConverter")
public class UserWorkgroupConverter implements Converter {
    @EJB
    UserBean userBean;

    @EJB
    WorkgroupBean workgroupBean;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Workgroup> workgroups = new ArrayList<>();

        ArrayList<Long> usersId = new ArrayList<>();
        ArrayList<Long> workgroupsId = new ArrayList<>();

        for (String recipient : s.split(", ")) {
            try {
                String[] recipientArray = recipient.split(":");
                if (recipientArray.length != 3) continue;
                if (recipientArray[0].equals("user")) {
                    long userId = Long.parseLong(recipientArray[1]);
                    if (!usersId.contains(userId)) {
                        usersId.add(userId);
                        users.add(userBean.getUser(userId));
                    }

                } else if (recipientArray[0].equals("workgroup")) {
                    long workgroupId = Long.parseLong(recipientArray[1]);
                    if (!workgroupsId.contains(workgroupId)) {
                        workgroupsId.add(workgroupId);
                        workgroups.add(workgroupBean.getWorkgroup(workgroupId));
                    }
                }
            } catch (Exception e) {
            }
        }
        return new ArrayList(Arrays.asList(users, workgroups));
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        ArrayList<User> users = (ArrayList<User>) ((ArrayList) o).get(0);
        ArrayList<Workgroup> workgroups = (ArrayList<Workgroup>) ((ArrayList) o).get(1);
        ArrayList<String> recipients = new ArrayList<>();
        for (User user : users) {
            recipients.add("user:" + user.getId() + ":" + user.getFirstName() + " " + user.getLastName() + " (" + user.getUsername() + ")");
        }
        for (Workgroup workgroup : workgroups) {
            recipients.add("workgroup:" + workgroup.getId() + ":" + workgroup.getName());
        }
        return String.join(", ", recipients);
    }
}
