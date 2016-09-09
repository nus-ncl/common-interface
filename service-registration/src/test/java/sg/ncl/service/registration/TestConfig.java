package sg.ncl.service.registration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import sg.ncl.service.registration.domain.RegistrationService;

import static org.mockito.Mockito.mock;

/**
 * @author Christopher Zhong
 */
@Configuration
public class TestConfig {

    private static final Logger logger = LoggerFactory.getLogger(TestConfig.class);

    @Bean
    @Profile("mock-registration-service")
    public RegistrationService registrationService() {
        logger.info("Mocking: {}", RegistrationService.class);
        return mock(RegistrationService.class);
    }
}
