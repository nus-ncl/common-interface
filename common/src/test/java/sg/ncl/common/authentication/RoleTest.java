package sg.ncl.common.authentication;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Te Ye
 */
public class RoleTest {

    @Test
    public void testRole() throws Exception {
        final Role role = Role.ADMIN;
        assertThat(role.getAuthority()).isEqualTo(role.toString());
    }

}
