package sg.ncl.service.authentication.util;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.user.data.jpa.AddressEntity;
import sg.ncl.service.user.data.jpa.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.domain.UserStatus;

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
        return entity;
    }

    public static UserEntity getUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(ZonedDateTime.now());
        userEntity.setUserDetails(new UserDetailsEntity());
        userEntity.setEmailVerified(true);
        userEntity.setStatus(UserStatus.APPROVED);
        return userEntity;
    }
}
