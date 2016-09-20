package sg.ncl.service.authentication.data.jpa;

import lombok.Getter;
import lombok.Setter;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsStatus;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "credentials", indexes = @Index(columnList = "username"))
@Getter
@Setter
public class CredentialsEntity extends AbstractEntity implements Credentials {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CredentialsStatus status = CredentialsStatus.ACTIVE;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "credentials_roles", joinColumns = @JoinColumn(name = "credentials_id", nullable = false, updatable = false))
    @Column(name = "role", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private final Set<Role> roles = new HashSet<>();

    public void addRole(final Role role) {
        roles.add(role);
    }

    public void removeRole(final Role role) {
        roles.remove(role);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Credentials that = (Credentials) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public String toString() {
        return "CredentialsEntity{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", roles=" + roles +
                "} " + super.toString();
    }

}
