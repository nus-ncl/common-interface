package sg.ncl.common.jwt;

import io.jsonwebtoken.Claims;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import sg.ncl.common.authentication.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by Chris on 9/23/2016.
 */
public class JwtTokenTest {

    static class RolesImpl extends ArrayList<String> implements Roles {
        RolesImpl(Collection<? extends String> c) {
            addAll(c);
        }
    }

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Test
    public void testJwtTokenString() throws Exception {
        final String token = "token";

        final JwtToken jwtToken = new JwtToken(token);

        assertThat(jwtToken.getClaims()).isNull();
        assertThat(jwtToken.getCredentials()).isEqualTo(token);
        assertThat(jwtToken.getPrincipal()).isNull();
        assertThat(jwtToken.getAuthorities()).isEmpty();
        assertThat(jwtToken.getDetails()).isNull();
        assertThat(jwtToken.isAuthenticated()).isFalse();
    }

    @Test
    public void testJwtTokenStringClaims() throws Exception {
        final String token = "token";
        final Claims claims = mock(Claims.class);
        final Roles roles = new RolesImpl(Arrays.asList(Role.ADMIN.name(), "s1", "s2", "s3", Role.USER.name()));
        doReturn(roles).when(claims).get(Roles.KEY, Roles.class);

        final JwtToken jwtToken = new JwtToken(token, claims);

        assertThat(jwtToken.getClaims()).isNotNull();
        assertThat(jwtToken.getCredentials()).isEqualTo(token);
        assertThat(jwtToken.getPrincipal()).isEqualTo(claims);
        assertThat(jwtToken.getAuthorities()).containsExactly(Role.ADMIN, Role.USER);
        assertThat(jwtToken.getDetails()).isEqualTo(claims);
        assertThat(jwtToken.isAuthenticated()).isTrue();
    }

}
