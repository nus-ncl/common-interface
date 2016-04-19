package sg.ncl.testbed_interface.repositories.jpa;

import sg.ncl.testbed_interface.domain.Credentials;

import javax.persistence.*;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "credentials", indexes = {@Index(columnList = "username")})
public class CredentialsEntity extends AbstractEntity implements Credentials {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "hashing_algorithm", nullable = false)
    private String hashingAlgorithm;

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

    public String getHashingAlgorithm() {
        return hashingAlgorithm;
    }

    public void setHashingAlgorithm(final String hashingAlgorithm) {
        this.hashingAlgorithm = hashingAlgorithm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Credentials that = (Credentials) o;

        return getUsername().equals(that.getUsername());
    }

    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CredentialsEntity{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", hashingAlgorithm='").append(hashingAlgorithm).append('\'');
        sb.append(", createdDate=").append(getCreatedDate());
        sb.append(", lastModifiedDate=").append(getLastModifiedDate());
        sb.append('}');
        return sb.toString();
    }

}
