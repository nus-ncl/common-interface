package sg.ncl.common.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import sg.ncl.common.test.AbstractTest;

import javax.inject.Inject;
import java.security.Key;
import java.time.Duration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@SpringApplicationConfiguration(TestApp.class)
public class JwtAutoConfigurationTest extends AbstractTest {

    @Inject
    private ApplicationContext applicationContext;

    @Test
    public void testPasswordEncoder() throws Exception {
        final PasswordEncoder bean = applicationContext.getBean(PasswordEncoder.class);

        assertThat(bean, is(not(nullValue(PasswordEncoder.class))));
    }

    @Test
    public void testSignatureAlgorithm() throws Exception {
        final SignatureAlgorithm bean = applicationContext.getBean(SignatureAlgorithm.class);

        assertThat(bean, is(not(nullValue(SignatureAlgorithm.class))));
    }

    @Test
    public void testKey() throws Exception {
        final Key bean = applicationContext.getBean(Key.class);

        assertThat(bean, is(not(nullValue(Key.class))));
    }

    @Test
    public void testDuration() throws Exception {
        final Duration bean = applicationContext.getBean(Duration.class);

        assertThat(bean, is(not(nullValue(Duration.class))));
    }

}
