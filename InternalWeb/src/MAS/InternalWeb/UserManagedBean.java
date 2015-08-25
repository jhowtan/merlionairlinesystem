package MAS.InternalWeb;

import MAS.CommonInterface.UserBeanRemote;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="user")
public class UserManagedBean {
    @EJB
    private UserBeanRemote userBean;

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    public UserManagedBean() {
    }

    public void addUser() {
        System.out.println("Creating user: " + username);
        userBean.createUser(username, password, firstName, lastName, email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    private String email;

}
