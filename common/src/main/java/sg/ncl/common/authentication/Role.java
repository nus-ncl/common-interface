package sg.ncl.common.authentication;

import org.springframework.security.core.GrantedAuthority;

/**
 * The {@link Role} enumerates all the roles for Credentials.
 *
 * @author Te Ye
 */
public enum Role implements GrantedAuthority {

    ADMIN, USER;

    @Override
    public String getAuthority() {
        return name();
    }

    public static boolean contains(final String name) {
        try {
            valueOf(name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
