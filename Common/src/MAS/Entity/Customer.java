package MAS.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@SequenceGenerator(name = "membershipNumberSeq", initialValue = 81111111, allocationSize = 10000000)
public class Customer {
    private Long id;

    @GeneratedValue
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long membershipNumber;

    @Basic
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "membershipNumberSeq")
    public Long getMembershipNumber() {
        return membershipNumber;
    }

    public void setMembershipNumber(Long membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    private String firstName;

    @Basic
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String lastName;

    @Basic
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private Date dateOfBirth;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    private String nationality;

    @Basic
    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    private String email;

    @Basic
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String phone;

    @Basic
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String displayName;

    @Basic
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    private String passwordHash;

    @Basic
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    private String salt;

    @Basic
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    private String address;

    @Basic
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String country;

    @Basic
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
