package sg.ncl.testbed_interface.repositories.jpa.entities;

import sg.ncl.testbed_interface.domain.UserActivity;

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
@Table(name = "user_activities")
public class UserActivityEntity implements UserActivity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "activity_date", nullable = false)
    private ZonedDateTime activityDate;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public ZonedDateTime getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(final ZonedDateTime date) {
        this.activityDate = date;
    }

}
