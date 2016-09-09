package sg.ncl.common.authentication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.common.authentication.AuthenticationPropertiesTest.TestConfig;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Chris on 8/6/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class, webEnvironment = WebEnvironment.NONE)
public class AuthenticationPropertiesTest {

    @Inject
    private AuthenticationProperties properties;

    @Test
    public void testGetUrl() throws Exception {
        assertThat(properties.getUrl()).isEqualTo("/testAuthenticationsUrl");
    }

    @Configuration
    @EnableConfigurationProperties(AuthenticationProperties.class)
    static class TestConfig {
    }

}
