package sg.ncl.service.experiment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.service.experiment.logic.ExperimentService;

import static org.mockito.Mockito.mock;

/**
 * @author Christopher Zhong
 */
@Configuration
public class TestConfig {

    @Bean
    @Profile("mock-deter-adapter")
    public AdapterDeterlab adapterDeterlab() {
        return mock(AdapterDeterlab.class);
    }

    @Bean
    @Profile("mock-experiment-service")
    public ExperimentService experimentService() {
        return mock(ExperimentService.class);
    }
}
