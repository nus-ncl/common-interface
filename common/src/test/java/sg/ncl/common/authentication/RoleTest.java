package sg.ncl.common.authentication;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Te Ye
 */
public class RoleTest {

    @Test
    public void testRoleAdmin() throws Exception {
        final Role role = Role.ADMIN;
        assertThat(role.getAuthority()).isEqualTo("ADMIN");
    }

    @Test
    public void testRoleUser() throws Exception {
        final Role role = Role.USER;
        assertThat(role.getAuthority()).isEqualTo("USER");
    }

}
