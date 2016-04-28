package sg.ncl.testbed_interface.repositories.jpa.entities;

import sg.ncl.testbed_interface.domain.UserCredentials;
import sg.ncl.testbed_interface.domain.UserCredentialsStatus;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "user_credentials", indexes = {@Index(columnList = "username")})
public class UserCredentialsEntity extends AbstractEntity implements UserCredentials {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id = null;

    @Column(name = "username", nullable = false, unique = true)
    private String username = null;

    @Column(name = "password", nullable = false)
    private String password = null;

    @OneToOne(optional = false, cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user = null;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserCredentialsStatus status = UserCredentialsStatus.ACTIVE;

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public UserEntity getUser() {
        return user;
    }

    public void setUser(final UserEntity user) {
        this.user = user;
    }

    @Override
    public UserCredentialsStatus getStatus() {
        return status;
    }

    public void setStatus(final UserCredentialsStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserCredentials that = (UserCredentials) o;

        return getUsername() == null ? that.getUsername() == null : getUsername().equals(that.getUsername());
    }

    @Override
    public int hashCode() {
        return getUsername() == null ? 0 : getUsername().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserCredentialsEntity{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", user=").append(user);
        sb.append(", status=").append(status);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}
