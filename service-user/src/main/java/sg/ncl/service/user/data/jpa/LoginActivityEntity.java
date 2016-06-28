package sg.ncl.service.user.data.jpa;

import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.user.domain.LoginActivity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "login_activities")
public class LoginActivityEntity extends AbstractEntity implements LoginActivity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    public Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public ZonedDateTime getDate() {
        return date;
    }

    void setDate(final ZonedDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LoginActivityEntity{");
        sb.append("id=").append(id);
        sb.append(", ipAddress='").append(ipAddress).append('\'');
        sb.append(", date=").append(date);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}
