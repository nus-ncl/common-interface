package sg.ncl.service.authentication;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import sg.ncl.service.authentication.services.AuthenticationService;

/**
 * @author Christopher Zhong
 */
@Configuration
public class TestConfig {

    @Bean
    @Profile("mock-authentication-service")
    public AuthenticationService authenticationService() {
        return Mockito.mock(AuthenticationService.class);
    }

}
