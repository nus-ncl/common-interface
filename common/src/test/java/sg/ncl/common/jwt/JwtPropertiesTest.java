package sg.ncl.common.jwt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.common.jwt.JwtPropertiesTest.TestConfig;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(properties = {
        "ncl.jwt.api-key=1234",
        "ncl.jwt.expiry-duration=PT1H",
        "ncl.jwt.signing-algorithm=HS256"
})
public class JwtPropertiesTest {

    @Configuration
    @EnableConfigurationProperties(JwtProperties.class)
    static class TestConfig {
    }

    @Inject
    private JwtProperties jwtProperties;

    @Test
    public void testSignatureAlgorithm() throws Exception {
        assertThat(jwtProperties.getSigningAlgorithm()).isEqualTo("HS256");
    }

    @Test
    public void testApiKey() throws Exception {
        assertThat(jwtProperties.getApiKey()).isEqualTo("1234");
    }

    @Test
    public void testExpiryDuration() throws Exception {
        assertThat(jwtProperties.getExpiryDuration()).isEqualTo("PT1H");
    }

}
