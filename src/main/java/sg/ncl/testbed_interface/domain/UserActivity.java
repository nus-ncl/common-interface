package sg.ncl.testbed_interface.domain;

import java.time.ZonedDateTime;

/**
 * The {@link UserActivity} interface represents the activities that the {@link User} performed.
 *
 * @author Christopher Zhong
 */
public interface UserActivity {

    /**
     * Returns the IP address that the {@link User} use to log in.
     *
     * @return the IP address that the {@link User} use to log in.
     */
    String getIpAddress();

    /**
     * Returns the {@link ZonedDateTime} of said activity.
     *
     * @return the {@link ZonedDateTime} of said activity.
     */
    ZonedDateTime getActivityDate();

}
