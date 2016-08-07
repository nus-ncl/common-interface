package sg.ncl.common.jwt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSpringBootApp.class, webEnvironment = WebEnvironment.NONE)
public class JwtPropertiesTest {

    @Inject
    private JwtProperties jwtProperties;

    @Test
    public void testSignatureAlgorithm() throws Exception {
        assertThat(jwtProperties.getSigningAlgorithm(), is(equalTo("HS256")));
    }

    @Test
    public void testApiKey() throws Exception {
        assertThat(jwtProperties.getApiKey(), is(equalTo("123")));
    }

    @Test
    public void testExpiryDuration() throws Exception {
        assertThat(jwtProperties.getExpiryDuration(), is(equalTo("PT1H")));
    }

}
