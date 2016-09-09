package sg.ncl.common.authentication;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sg.ncl.common.jwt.JwtFilter;

import javax.inject.Inject;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Chris on 8/6/2016.
 */
public class AuthenticationAutoConfigurationTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private AuthenticationProperties properties;

    @Inject
    private JwtFilter jwtFilter;

    private AuthenticationAutoConfiguration configuration;

    @Before
    public void before() {
        configuration = new AuthenticationAutoConfiguration(properties, jwtFilter);
    }

    @Test
    public void testPasswordEncoder() throws Exception {
        final PasswordEncoder encoder = configuration.passwordEncoder();

        assertThat(encoder, is(not(nullValue(PasswordEncoder.class))));
        assertThat(encoder, is(instanceOf(BCryptPasswordEncoder.class)));
    }

}
