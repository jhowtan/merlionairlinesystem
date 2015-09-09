package MAS.ManagedBean;

import MAS.Bean.UserBean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class CreateUserManagedBean {
    @EJB
    private UserBean userBean;

    private String username;
    private String firstName;
    private String lastName;
    private String email;

    private String phone;

    public void createUser() {
        userBean.createUser(getUsername(), getFirstName(), getLastName(), getEmail(), getPhone());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
