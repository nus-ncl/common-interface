package sg.ncl.common.authentication;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import sg.ncl.common.test.AbstractTest;

import javax.inject.Inject;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Chris on 8/6/2016.
 */
@SpringBootTest(classes = TestApp.class)
public class AuthenticationAutoConfigurationTest extends AbstractTest {

    @Inject
    private ApplicationContext applicationContext;

    @Test
    public void testPasswordEncoder() throws Exception {
        final PasswordEncoder bean = applicationContext.getBean(PasswordEncoder.class);

        assertThat(bean, is(not(nullValue(PasswordEncoder.class))));
    }
}
