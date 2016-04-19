package sg.ncl.testbed_interface.repositories.jpa;

import org.hibernate.annotations.GenericGenerator;
import sg.ncl.testbed_interface.domain.User;

import javax.persistence.*;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "users")
public class UserEntity extends AbstractEntity implements User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id;

    @OneToOne(optional = false, cascade = {CascadeType.ALL})
    @JoinColumn(name = "personal_details_id", nullable = false, unique = true)
    private PersonalDetailsEntity personalDetails;

    @Override
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public PersonalDetailsEntity getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(final PersonalDetailsEntity personalDetails) {
        this.personalDetails = personalDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserEntity{");
        sb.append("id='").append(id).append('\'');
        sb.append(", personalDetails=").append(personalDetails);
        sb.append(", createdDate=").append(getCreatedDate());
        sb.append(", lastModifiedDate=").append(getLastModifiedDate());
        sb.append('}');
        return sb.toString();
    }
}
