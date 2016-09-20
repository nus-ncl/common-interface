package sg.ncl.common.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

/**
 * @author Christopher Zhong
 */
@Configuration
@Slf4j
public class TestConfig {
    @Bean
    @Profile("mock-authentication-auto-config-service")
    public AuthenticationAutoConfiguration authenticationAutoConfiguration() {
        log.info("Mocking: {}", AuthenticationAutoConfiguration.class);
        return mock(AuthenticationAutoConfiguration.class);
    }

}
