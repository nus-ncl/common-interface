package sg.ncl.testbed_interface.repositories.jpa.entities;

import sg.ncl.testbed_interface.domain.LoginActivity;

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

    protected void setId(final Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(final ZonedDateTime date) {
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
