package sg.ncl.service.authentication.data.jpa.entities;

import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "credentials", indexes = @Index(columnList = "username", unique = true))
public class CredentialsEntity extends AbstractEntity implements Credentials {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CredentialsStatus status = CredentialsStatus.ACTIVE;

    public Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    void setUsername(final String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    void setUserId(final String userId) {
        this.userId = userId;
    }

    @Override
    public CredentialsStatus getStatus() {
        return status;
    }

    void setStatus(final CredentialsStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Credentials that = (Credentials) o;

        return getUsername() == null ? that.getUsername() == null : getUsername().equals(that.getUsername());
    }

    @Override
    public int hashCode() {
        return getUsername() == null ? 0 : getUsername().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CredentialsEntity{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", userId=").append(userId);
        sb.append(", status=").append(status);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}
