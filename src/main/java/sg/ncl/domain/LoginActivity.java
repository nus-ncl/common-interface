package sg.ncl.domain;

import java.time.ZonedDateTime;

/**
 * The {@link LoginActivity} interface represents the login activities of a {@link User}.
 *
 * @author Christopher Zhong
 */
public interface LoginActivity {

    /**
     * Returns the IP address that the {@link User} used to log in.
     *
     * @return the IP address that the {@link User} used to log in.
     */
    String getIpAddress();

    /**
     * Returns the {@link ZonedDateTime} that the {@link User} used to log in.
     *
     * @return the {@link ZonedDateTime} that the {@link User} used to log in.
     */
    ZonedDateTime getDate();

}
