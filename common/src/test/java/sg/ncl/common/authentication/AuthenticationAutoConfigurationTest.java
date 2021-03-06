package sg.ncl.common.authentication;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sg.ncl.common.jwt.JwtAuthenticationProvider;
import sg.ncl.common.jwt.JwtFilter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Chris on 8/6/2016.
 */
public class AuthenticationAutoConfigurationTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private AuthenticationProperties properties;
    @Mock
    private JwtFilter filter;
    @Mock
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    private AuthenticationAutoConfiguration configuration;

    @Before
    public void before() {
        configuration = new AuthenticationAutoConfiguration(properties, filter, jwtAuthenticationProvider);
    }

    @Test
    public void testPasswordEncoder() throws Exception {
        final PasswordEncoder encoder = configuration.passwordEncoder();

        assertThat(encoder).isNotNull();
        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

}
