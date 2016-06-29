package sg.ncl.service.user.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.domain.LoginActivity;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserStatus;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Christopher Zhong
 */
public class UserInfo implements User {

    private final String id;
    private final UserDetailsInfo userDetails;
    private final Boolean emailVerified;
    private final UserStatus status;
    private final ZonedDateTime applicationDate;
    private final ZonedDateTime processedDate;
    private final List<? extends LoginActivity> loginActivities;
    private final List<String> teamIds;

    @JsonCreator
    public UserInfo(@JsonProperty("id") final String id, @JsonProperty("userDetails") final UserDetailsInfo userDetails, @JsonProperty("emailVerified") final Boolean emailVerified, @JsonProperty("status") final UserStatus status, @JsonProperty("applicationDate") final ZonedDateTime applicationDate, @JsonProperty("processedDate") final ZonedDateTime processedDate, @JsonProperty("loginActivities") final List<? extends LoginActivity> loginActivities, @JsonProperty("teamIds") final List<String> teamIds) {
        this.id = id;
        this.userDetails = userDetails;
        this.emailVerified = emailVerified;
        this.status = status;
        this.applicationDate = applicationDate;
        this.processedDate = processedDate;
        this.loginActivities = loginActivities;
        this.teamIds = teamIds;
    }

    public UserInfo(final UserEntity userEntity) {
        this(userEntity.getId(),
                new UserDetailsInfo(userEntity.getUserDetails()),
                userEntity.isEmailVerified(),
                userEntity.getStatus(),
                userEntity.getApplicationDate(),
                userEntity.getProcessedDate(),
                userEntity.getLoginActivities(),
                userEntity.getTeamIds()
        );
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public UserDetailsInfo getUserDetails() {
        return userDetails;
    }

    @Override
    public boolean isEmailVerified() {
        return emailVerified;
    }

    @Override
    public UserStatus getStatus() {
        return status;
    }

    @Override
    public ZonedDateTime getApplicationDate() {
        return applicationDate;
    }

    @Override
    public ZonedDateTime getProcessedDate() {
        return processedDate;
    }

    @Override
    public List<? extends LoginActivity> getLoginActivities() {
        return loginActivities;
    }

    @Override
    public List<String> getTeamIds() {
        return teamIds;
    }

}
