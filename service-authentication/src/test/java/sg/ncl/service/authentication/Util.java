package sg.ncl.service.authentication;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;

/**
 * @author Christopher Zhong
 */
public class Util {

    public static CredentialsEntity getCredentialsEntity() {
        final CredentialsEntity entity = new CredentialsEntity();
        entity.setId(RandomStringUtils.randomAlphanumeric(20));
        entity.setUsername(RandomStringUtils.randomAlphanumeric(20));
        entity.setPassword(RandomStringUtils.randomAlphanumeric(20));
        return entity;
    }

}
