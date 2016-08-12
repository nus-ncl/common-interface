package sg.ncl.service.user.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.user.domain.LoginActivity;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserStatus;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Christopher Zhong
 */
@Getter
public class UserInfo implements User {

    private final String id;
    private final UserDetailsInfo userDetails;
    private final boolean emailVerified;
    private final String verificationKey;
    private final UserStatus status;
    private final ZonedDateTime applicationDate;
    private final ZonedDateTime processedDate;
    private final List<? extends LoginActivity> loginActivities;
    private final List<String> teams;

    @JsonCreator
    public UserInfo(
            @JsonProperty("id") final String id,
            @JsonProperty("userDetails") final UserDetailsInfo userDetails,
            @JsonProperty("emailVerified") final boolean emailVerified,
            @JsonProperty("verificationKey") final String verificationKey,
            @JsonProperty("status") final UserStatus status,
            @JsonProperty("applicationDate") final ZonedDateTime applicationDate,
            @JsonProperty("processedDate") final ZonedDateTime processedDate,
            @JsonProperty("loginActivities") final List<? extends LoginActivity> loginActivities,
            @JsonProperty("teams") final List<String> teams
    ) {
        this.id = id;
        this.userDetails = userDetails;
        this.emailVerified = emailVerified;
        this.verificationKey = verificationKey;
        this.status = status;
        this.applicationDate = applicationDate;
        this.processedDate = processedDate;
        this.loginActivities = loginActivities;
        this.teams = teams;
    }

    public UserInfo(final User user) {
        this(
                user.getId(),
                new UserDetailsInfo(user.getUserDetails()),
                user.isEmailVerified(),
                user.getVerificationKey(),
                user.getStatus(),
                user.getApplicationDate(),
                user.getProcessedDate(),
                user.getLoginActivities(),
                user.getTeams()
        );
    }

}
