package sg.ncl.service.user.dtos;

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

    public UserInfo(final String id, final UserDetailsInfo userDetails, final Boolean emailVerified, final UserStatus status, final ZonedDateTime applicationDate, final ZonedDateTime processedDate, final List<? extends LoginActivity> loginActivities, final List<String> teamIds) {
        this.id = id;
        this.userDetails = userDetails;
        this.emailVerified = emailVerified;
        this.status = status;
        this.applicationDate = applicationDate;
        this.processedDate = processedDate;
        this.loginActivities = loginActivities;
        this.teamIds = teamIds;
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
