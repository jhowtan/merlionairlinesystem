package MAS.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@TableGenerator(name = "membershipNumberTable", initialValue=81111111, allocationSize = 1)
public class Customer {
    private Long id;

    @GeneratedValue(strategy = GenerationType.TABLE, generator = "membershipNumberTable")
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    private boolean locked;

    @Basic
    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    private Date joinDate;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    private int tier;

    @Basic
    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }


    private Date statusExpiry;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStatusExpiry() {
        return statusExpiry;
    }

    public void setStatusExpiry(Date statusExpiry) {
        this.statusExpiry = statusExpiry;
    }

    private Date qualificationEndDate;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getQualificationEndDate() {
        return qualificationEndDate;
    }

    public void setQualificationEndDate(Date qualificationEndDate) {
        this.qualificationEndDate = qualificationEndDate;
    }

    private int eliteMiles;

    @Basic
    public int getEliteMiles() {
        return eliteMiles;
    }

    public void setEliteMiles(int eliteMiles) {
        this.eliteMiles = eliteMiles;
    }

    private int miles;

    @Basic
    public int getMiles() {
        return miles;
    }

    public void setMiles(int miles) {
        this.miles = miles;
    }
}
