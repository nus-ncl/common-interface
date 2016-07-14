package sg.ncl.service.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import sg.ncl.service.authentication.data.jpa.CredentialsRepository;
import sg.ncl.service.authentication.logic.AuthenticationService;
import sg.ncl.service.authentication.logic.CredentialsService;

import static org.mockito.Mockito.mock;

/**
 * @author Christopher Zhong
 */
@Configuration
public class TestConfig {

    private static final Logger logger = LoggerFactory.getLogger(TestConfig.class);

    @Bean
    @Profile("mock-authentication-service")
    public AuthenticationService authenticationService() {
        logger.info("Mocking: {}", AuthenticationService.class);
        return mock(AuthenticationService.class);
    }

    @Bean
    @Profile("mock-credentials-service")
    public CredentialsService credentialsService() {
        logger.info("Mocking: {}", CredentialsService.class);
        return mock(CredentialsService.class);
    }

    @Bean
    @Profile("mock-password-encoder")
    public PasswordEncoder passwordEncoder() {
        logger.info("Mocking: {}", PasswordEncoder.class);
        return mock(PasswordEncoder.class);
    }

    @Bean
    @Profile("mock-credentials-repository")
    public CredentialsRepository credentialsRepository() {
        logger.info("Mocking: {}", CredentialsRepository.class);
        return mock(CredentialsRepository.class);
    }

}
