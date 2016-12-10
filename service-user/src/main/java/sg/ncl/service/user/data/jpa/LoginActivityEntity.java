package sg.ncl.service.user.data.jpa;

import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
public class LoginActivityEntity extends AbstractEntity implements LoginActivity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

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
