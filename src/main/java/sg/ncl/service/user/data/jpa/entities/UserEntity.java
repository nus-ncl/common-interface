package sg.ncl.service.user.data.jpa.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserStatus;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "users")
public class UserEntity extends AbstractEntity implements User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id = null;

    @OneToOne(optional = false, cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_details_id", nullable = false, unique = true)
    private UserDetailsEntity userDetails = null;

    @Column(name = "is_email_verified", nullable = false)
    @Type(type = "yes_no")
    private boolean emailVerified = false;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING;

    @Column(name = "application_date", nullable = false)
    private ZonedDateTime applicationDate = null;

    @Column(name = "processed_date")
    private ZonedDateTime processedDate = null;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    private final List<LoginActivityEntity> loginActivities = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "users_teams", joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)}, indexes = {@Index(columnList = "user_id"), @Index(columnList = "team_id")}, uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "team_id"})})
    @Column(name = "team_id", nullable = false, updatable = false)
    private final List<String> teams = new ArrayList<>();

    @Override
    public String getId() {
        return id;
    }

    void setId(final String id) {
        this.id = id;
    }

    public UserDetailsEntity getUserDetails() {
        return userDetails;
    }

    void setUserDetails(final UserDetailsEntity userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public boolean isEmailVerified() {
        return emailVerified;
    }

    void setEmailVerified(final boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Override
    public UserStatus getStatus() {
        return status;
    }

    void setStatus(final UserStatus status) {
        this.status = status;
    }

    @Override
    public ZonedDateTime getApplicationDate() {
        return this.applicationDate;
    }

    void setApplicationDate(ZonedDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    @Override
    public ZonedDateTime getProcessedDate() {
        return this.processedDate;
    }

    void setProcessedDate(ZonedDateTime processedDate) {
        this.processedDate = processedDate;
    }

    @Override
    public List<LoginActivityEntity> getLoginActivities() {
        return loginActivities;
    }

    void addLoginActivity(final LoginActivityEntity loginActivity) {
        loginActivities.add(loginActivity);
    }

    @Override
    public List<String> getTeams() {
        return new ArrayList<>(teams);
    }

    void addTeam(final String teamId) {
        if (teams.contains(teamId)) {
            return;
        }
        teams.add(teamId);
    }

    void removeTeam(final String teamId) {
        if (teams.contains(teamId)) {
            teams.remove(teamId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final User that = (User) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserEntity{");
        sb.append("id='").append(id).append('\'');
        sb.append(", userDetails=").append(userDetails);
        sb.append(", emailVerified=").append(emailVerified);
        sb.append(", status=").append(status);
        sb.append(", applicationDate=").append(applicationDate);
        sb.append(", processedDate=").append(processedDate);
        sb.append(", loginActivities=").append(loginActivities);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}
