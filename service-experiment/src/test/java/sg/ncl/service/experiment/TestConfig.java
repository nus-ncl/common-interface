package sg.ncl.service.experiment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.experiment.domain.ExperimentService;

import static org.mockito.Mockito.mock;

/**
 * @author Christopher Zhong
 */
@Configuration
public class TestConfig {

    @Bean
    @Profile("mock-deter-adapter")
    public AdapterDeterLab adapterDeterLab() {
        return mock(AdapterDeterLab.class);
    }

    @Bean
    @Profile("mock-experiment-service")
    public ExperimentService experimentService() {
        return mock(ExperimentService.class);
    }
}
