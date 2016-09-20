package sg.ncl.service.authentication.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.common.authentication.Role;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Chris on 9/15/2016.
 */
public class AuthorizationInfoTest {

    private final String id = RandomStringUtils.randomAlphanumeric(20);
    private final String token = RandomStringUtils.randomAlphanumeric(20);
    private final List<Role> roles = Arrays.asList(Role.ADMIN, Role.ADMIN, Role.USER);
    private final AuthorizationInfo authorizationInfo = new AuthorizationInfo(id, token, roles);

    @Test
    public void getId() throws Exception {
        assertThat(authorizationInfo.getId()).isEqualTo(id);
    }

    @Test
    public void getToken() throws Exception {
        assertThat(authorizationInfo.getToken()).isEqualTo(token);
    }

    @Test
    public void getRoles() throws Exception {
        assertThat(authorizationInfo.getRoles()).containsOnly(Role.ADMIN, Role.USER).hasSize(2);
    }

}
