package sg.ncl.service.authentication.dtos;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.authentication.domain.CredentialsStatus;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author Christopher Zhong
 */
public class CredentialsInfoTest {

    private final String username = RandomStringUtils.randomAlphanumeric(20);
    private final String password = RandomStringUtils.randomAlphanumeric(20);
    private final String userId = RandomStringUtils.randomAlphanumeric(20);
    private final CredentialsInfo credentialsInfo = new CredentialsInfo(username, password, userId, CredentialsStatus.ACTIVE);

    @Test
    public void testGetUsername() throws Exception {
        assertThat(credentialsInfo.getUsername(), is(equalTo(username)));
    }

    @Test
    public void testGetPassword() throws Exception {
        assertThat(credentialsInfo.getPassword(), is(equalTo(password)));

    }

    @Test
    public void testGetUserId() throws Exception {
        assertThat(credentialsInfo.getUserId(), is(equalTo(userId)));
    }

    @Test
    public void testGetStatus() throws Exception {
        assertThat(credentialsInfo.getStatus(), is(equalTo(CredentialsStatus.ACTIVE)));
    }

}
