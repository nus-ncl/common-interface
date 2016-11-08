package sg.ncl.service.authentication.util;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.common.authentication.Role;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.PasswordResetRequestEntity;
import sg.ncl.service.authentication.data.jpa.PasswordResetRequestRepository;

import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
public class TestUtil {

    public static CredentialsEntity getCredentialsEntity() {
        return getCredentialsEntity(RandomStringUtils.randomAlphanumeric(20), RandomStringUtils.randomAlphanumeric(20), RandomStringUtils.randomAlphanumeric(20));
    }

    public static CredentialsEntity getCredentialsEntity(final String id, final String username, final String password) {
        final CredentialsEntity entity = new CredentialsEntity();
        entity.setId(id);
        entity.setUsername(username);
        entity.setPassword(password);
        entity.addRole(Role.USER);
        return entity;
    }

    public static PasswordResetRequestEntity getPasswordResetRequestEntity() {
        return getPasswordResetRequestEntity(
                RandomStringUtils.randomAlphanumeric(20), RandomStringUtils.randomAlphabetic(10), ZonedDateTime.now());
    }

    public static PasswordResetRequestEntity getPasswordResetRequestEntity(String hash, String username, ZonedDateTime time) {
        final PasswordResetRequestEntity entity = new PasswordResetRequestEntity();
        entity.setHash(hash);
        entity.setUsername(username);
        entity.setTime(time);
        return entity;
    }
}
