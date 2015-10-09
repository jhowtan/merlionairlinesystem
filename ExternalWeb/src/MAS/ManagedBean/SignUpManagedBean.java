package MAS.ManagedBean;

import MAS.Bean.CustomerBean;
import MAS.Common.Constants;
import MAS.Entity.Customer;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Date;

@ManagedBean
public class SignUpManagedBean {
    @EJB
    CustomerBean customerBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String phone;
    private String address;
    private String nationality;
    private String country;
    private String displayName;

    public void register() {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setDateOfBirth(birthDate);
        customer.setNationality(nationality);
        customer.setCountry(country);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setDisplayName(displayName);
        customer = customerBean.createCustomer(customer, password);
        authManagedBean.login(customer);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/ffp/firstWelcome.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getNationalities() {
        return Constants.NATIONALITIES;
    }
    public String[] getCountries() {
        return Constants.COUNTRIES;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
