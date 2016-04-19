package sg.ncl.testbed_interface.repositories.jpa.entities;

import sg.ncl.testbed_interface.domain.PersonalDetails;

import javax.persistence.*;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "personal_details")
public class PersonalDetailsEntity extends AbstractEntity implements PersonalDetails {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
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
    public String toString() {
        final StringBuilder sb = new StringBuilder("PersonalDetailsEntity{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", createdDate=").append(getCreatedDate());
        sb.append(", lastModifiedDate=").append(getLastModifiedDate());
        sb.append('}');
        return sb.toString();
    }
}
