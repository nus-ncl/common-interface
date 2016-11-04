package sg.ncl.service.authentication.data.jpa;

import lombok.Getter;
import lombok.Setter;
import sg.ncl.service.authentication.domain.Credentials;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 11/3/2016.
 */
@Entity
@Table(name = "password_reset_requests")
@Getter
@Setter
public class PasswordResetRequestEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "hash", nullable = false, unique = true, updatable = false)
    private String hash;

    @Column(name = "time", nullable = false)
    private ZonedDateTime time;

    @Column(name = "username", nullable = false)
    private String username;

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
        return "PasswordResetRequestEntity{" +
                "id='" + id + '\'' +
                ", hash='" + hash + '\'' +
                ", username='" + username + '\'' +
                ", time='" + time + '\'' + "} ";
    }

}