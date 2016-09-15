package sg.ncl.common.jwt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.core.Authentication;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Te Ye
 */
public class JwtAuthenticationProviderTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Before
    public void before() {
        jwtAuthenticationProvider = new JwtAuthenticationProvider();
    }

    @Test
    public void testNotAuthenticated() throws Exception {
        JwtToken jwtToken = mock(JwtToken.class);
        when(jwtToken.isAuthenticated()).thenReturn(false);
        Authentication resultToken = jwtAuthenticationProvider.authenticate(jwtToken);
        assertThat(resultToken.isAuthenticated(), is(jwtToken.isAuthenticated()));
    }
}
