package sg.ncl.service.authentication.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.authentication.domain.CredentialsStatus;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 */
public class CredentialsInfoTest {

    private final String username = RandomStringUtils.randomAlphanumeric(20);
    private final String password = RandomStringUtils.randomAlphanumeric(20);
    private final String userId = RandomStringUtils.randomAlphanumeric(20);
    private final CredentialsInfo credentialsInfo = new CredentialsInfo(userId, username, password, CredentialsStatus.ACTIVE);

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
        assertThat(credentialsInfo.getId(), is(equalTo(userId)));
    }

    @Test
    public void testGetStatus() throws Exception {
        assertThat(credentialsInfo.getStatus(), is(equalTo(CredentialsStatus.ACTIVE)));
    }

}
