package sg.ncl.service.user.data.jpa;

import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.user.domain.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "user_details", indexes = @Index(columnList = "email", unique = true))
public class UserDetailsEntity extends AbstractEntity implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    private AddressEntity address;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "institution", nullable = false)
    private String institution;

    @Column(name = "institution_abbreviation", nullable = false)
    private String institutionAbbreviation;

    @Column(name = "institution_web", nullable = false)
    private String institutionWeb;



    public Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(final AddressEntity address) {
        this.address = address;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    @Override
    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    @Override
    public String getInstitutionAbbreviation() {
        return institutionAbbreviation;
    }

    public void setInstitutionAbbreviation(String institutionAbbreviation) {
        this.institutionAbbreviation = institutionAbbreviation;
    }

    @Override
    public String getInstitutionWeb() {
        return institutionWeb;
    }

    public void setInstitutionWeb(String institutionWeb) {
        this.institutionWeb = institutionWeb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        UserDetails that = (UserDetails) o;

        return getEmail() == null ? that.getEmail() == null : getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return getEmail() == null ? 0 : getEmail().hashCode();
    }

    @Override
    public String toString() {
        return "UserDetailsEntity{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", address=" + address +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", institution='" + institution + '\'' +
                ", institutionAbbreviation='" + institutionAbbreviation + '\'' +
                ", institutionWeb='" + institutionWeb + '\'' +
                "} " + super.toString();
    }

    public static UserDetailsEntity get(final UserDetails userDetails) {
        final UserDetailsEntity entity = new UserDetailsEntity();
        entity.setFirstName(userDetails.getFirstName());
        entity.setLastName(userDetails.getLastName());
        entity.setJobTitle(userDetails.getJobTitle());
        entity.setAddress(AddressEntity.get(userDetails.getAddress()));
        entity.setEmail(userDetails.getEmail());
        entity.setPhone(userDetails.getPhone());
        entity.setInstitution(userDetails.getInstitution());
        entity.setInstitutionAbbreviation(userDetails.getInstitutionAbbreviation());
        entity.setInstitutionWeb(userDetails.getInstitutionWeb());
        return entity;
    }
}
