package sg.ncl.service.user.data.jpa;

import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

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
}
