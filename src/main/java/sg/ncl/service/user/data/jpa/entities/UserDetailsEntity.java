package sg.ncl.service.user.data.jpa.entities;

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
@Table(name = "user_details", indexes = {@Index(columnList = "email", unique = true)})
public class UserDetailsEntity extends AbstractEntity implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id = null;

    @Column(name = "first_name", nullable = false)
    private String firstName = null;

    @Column(name = "last_name", nullable = false)
    private String lastName = null;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "address_id", nullable = false)
    private AddressEntity address = null;

    @Column(name = "email", nullable = false)
    private String email = null;

    @Column(name = "phone", nullable = false)
    private String phone = null;

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

    void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @Override
    public AddressEntity getAddress() {
        return address;
    }

    void setAddress(final AddressEntity address) {
        this.address = address;
    }

    @Override
    public String getEmail() {
        return email;
    }

    void setEmail(final String email) {
        this.email = email;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    void setPhone(final String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDetails that = (UserDetails) o;

        return getEmail() == null ? that.getEmail() == null : getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return getEmail() == null ? 0 : getEmail().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDetailsEntity{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", address=").append(address);
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}
