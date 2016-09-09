package sg.ncl.service.authentication.util;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.domain.Role;

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

}
