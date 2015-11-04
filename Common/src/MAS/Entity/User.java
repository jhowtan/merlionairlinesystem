package MAS.Entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class User {
    private Long id;

    @GeneratedValue
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String username;

    @Basic
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String passwordHash;

    @Basic
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = password;
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

    private String email;

    @Basic
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private List<Role> roles;

    @ManyToMany
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    private String salt;

    @Basic
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    private boolean locked;

    @Basic
    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean status) {
        this.locked = status;
    }

    private String resetHash;

    @Basic
    public String getResetHash() {
        return resetHash;
    }

    public void setResetHash(String resetHash) {
        this.resetHash = resetHash;
    }

    private Date resetExpiry;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getResetExpiry() {
        return resetExpiry;
    }

    public void setResetExpiry(Date resetExpiry) {
        this.resetExpiry = resetExpiry;
    }

    private String phone;

    @Basic
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private boolean deleted;

    @Basic
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    private List<Message> messages;

    @ManyToMany(mappedBy = "recipients")
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    private List<Workgroup> workgroups;

    @ManyToMany(mappedBy = "users")
    public List<Workgroup> getWorkgroups() {
        return workgroups;
    }

    public void setWorkgroups(List<Workgroup> workgroups) {
        this.workgroups = workgroups;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && this.id.equals(((User) obj).id);
	}

    private Airport baseAirport;

    @ManyToOne
    public Airport getBaseAirport() {
        return baseAirport;
    }

    public void setBaseAirport(Airport baseAirport) {
        this.baseAirport = baseAirport;
    }

    private int job;

    @Basic
    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    private Airport currentLocation;

    @ManyToOne
    public Airport getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Airport currentLocation) {
        this.currentLocation = currentLocation;
    }
}
