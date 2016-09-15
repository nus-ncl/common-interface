package sg.ncl.service.authentication.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.common.authentication.Role;
import sg.ncl.service.authentication.domain.CredentialsStatus;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Christopher Zhong
 */
public class CredentialsInfoTest {

    private final String id = RandomStringUtils.randomAlphanumeric(20);
    private final String username = RandomStringUtils.randomAlphanumeric(20);
    private final String password = RandomStringUtils.randomAlphanumeric(20);
    private final CredentialsInfo credentialsInfo = new CredentialsInfo(id, username, password, CredentialsStatus.ACTIVE, Arrays.asList(Role.ADMIN, Role.ADMIN, Role.USER));

    @Test
    public void testGetId() throws Exception {
        assertThat(credentialsInfo.getId()).isEqualTo(id);
    }

    @Test
    public void testGetUsername() throws Exception {
        assertThat(credentialsInfo.getUsername()).isEqualTo(username);
    }

    @Test
    public void testGetPassword() throws Exception {
        assertThat(credentialsInfo.getPassword()).isEqualTo(password);

    }

    @Test
    public void testGetStatus() throws Exception {
        assertThat(credentialsInfo.getStatus()).isEqualTo(CredentialsStatus.ACTIVE);
    }

    @Test
    public void testGetRoles() throws Exception {
        assertThat(credentialsInfo.getRoles()).containsOnly(Role.ADMIN, Role.USER).hasSize(2);
    }

}
