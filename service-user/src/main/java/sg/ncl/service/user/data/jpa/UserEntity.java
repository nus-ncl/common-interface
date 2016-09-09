package sg.ncl.service.user.data.jpa;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserStatus;
import sg.ncl.service.user.exceptions.UserAlreadyInTeamException;

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
@Slf4j
public class UserEntity extends AbstractEntity implements User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_details_id", nullable = false, unique = true)
    private UserDetailsEntity userDetails;

    @Column(name = "is_email_verified", nullable = false)
    @Type(type = "yes_no")
    private boolean emailVerified = false;

    @Column(name = "verification_key")
    private String verificationKey;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.CREATED;

    @Column(name = "application_date", nullable = false)
    private ZonedDateTime applicationDate;

    @Column(name = "processed_date")
    private ZonedDateTime processedDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private final List<LoginActivityEntity> loginActivities = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "users_teams", joinColumns = @JoinColumn(name = "user_id", nullable = false, updatable = false), indexes = {@Index(columnList = "user_id"), @Index(columnList = "team_id")}, uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "team_id"}))
    @Column(name = "team_id", nullable = false, updatable = false)
    private final List<String> teams = new ArrayList<>();

    @Override
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public UserDetailsEntity getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(final UserDetailsEntity userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(final boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Override
    public String getVerificationKey() {
        return verificationKey;
    }

    public void setVerificationKey(String verificationKey) {
        this.verificationKey = verificationKey;
    }

    @Override
    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(final UserStatus status) {
        this.status = status;
    }

    @Override
    public ZonedDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final ZonedDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    @Override
    public ZonedDateTime getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(final ZonedDateTime processedDate) {
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

    public void addTeam(final String team) {
        if (teams.contains(team)) {
            throw new UserAlreadyInTeamException();
        }
        log.info("Adding team on the user side: {}", team);
        teams.add(team);
    }

    public void removeTeam(final String team) {
        if (teams.contains(team)) {
            teams.remove(team);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final User that = (User) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", userDetails=" + userDetails +
                ", emailVerified=" + emailVerified +
                ", status=" + status +
                ", verificationKey=" + verificationKey +
                ", applicationDate=" + applicationDate +
                ", processedDate=" + processedDate +
                ", loginActivities=" + loginActivities +
                ", teams=" + teams +
                "} " + super.toString();
    }

}
