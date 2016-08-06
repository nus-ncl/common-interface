package sg.ncl.service.realization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import sg.ncl.service.realization.logic.RealizationService;

import static org.mockito.Mockito.mock;

/**
 * @author Christopher Zhong
 */
@Configuration
public class TestConfig {

    @Bean
    @Profile("mock-realization-service")
    public RealizationService realizationService() {
//        logger.info("Mocking: {}", RealizationService.class);
        return mock(RealizationService.class);
    }

}
