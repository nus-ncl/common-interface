package sg.ncl.service.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import sg.ncl.service.authentication.services.AuthenticationService;

import static org.mockito.Mockito.mock;

/**
 * @author Christopher Zhong
 */
@Configuration
public class TestConfig {

    @Bean
    @Profile("mock-authentication-service")
    public AuthenticationService authenticationService() {
        return mock(AuthenticationService.class);
    }

}
