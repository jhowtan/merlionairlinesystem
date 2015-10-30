package MAS.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Certification {
    private Long id;

    @GeneratedValue
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Date submittedDate;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

    private int type;

    @Basic
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private String language;

    @Basic
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    private String others;

    @Basic
    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    private AircraftType aircraftType;

    @ManyToOne
    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }

    private User owner;

    @ManyToOne
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    private User approver;

    @ManyToOne
    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }

    private int approvalStatus;

    @Basic
    public int getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(int approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    private Date approvalDate;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    private String document;

    @Basic
    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
