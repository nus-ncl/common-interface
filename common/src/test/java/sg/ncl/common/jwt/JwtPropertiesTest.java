package sg.ncl.common.jwt;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sg.ncl.common.test.AbstractTest;

import javax.inject.Inject;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@SpringBootTest(classes = TestApp.class)
public class JwtPropertiesTest extends AbstractTest {

    @Inject
    private JwtProperties jwtProperties;

    @Test
    public void testApiKey() throws Exception {
        assertThat(jwtProperties.getApiKey(), is(equalTo("123")));
    }

    @Test
    public void testSignatureAlgorithm() throws Exception {
        assertThat(jwtProperties.getSigningAlgorithm(), is(equalTo("HS256")));
    }

    @Test
    public void testExpiryDuration() throws Exception {
        assertThat(jwtProperties.getExpiryDuration(), is(equalTo("PT1H")));
    }

}
