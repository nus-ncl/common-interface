package sg.ncl.common.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import sg.ncl.common.jwt.JwtAuthenticationProvider;
import sg.ncl.common.jwt.JwtAutoConfiguration;
import sg.ncl.common.jwt.JwtFilter;

import static org.mockito.Mockito.mock;

/**
 * @author Christopher Zhong
 */
@Configuration
@Import({JwtFilter.class, JwtAuthenticationProvider.class, JwtAutoConfiguration.class})
@Slf4j
public class TestConfig {
    @Bean
    @Profile("mock-authentication-auto-config-service")
    public AuthenticationAutoConfiguration authenticationAutoConfiguration() {
        log.info("Mocking: {}", AuthenticationAutoConfiguration.class);
        return mock(AuthenticationAutoConfiguration.class);
    }

}
