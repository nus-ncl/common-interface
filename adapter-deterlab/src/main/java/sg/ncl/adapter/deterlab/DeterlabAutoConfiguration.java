package sg.ncl.adapter.deterlab;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import sg.ncl.common.jpa.UseJpa;

/**
 * @author Yeo Te Ye
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(ConnectionProperties.class)
@UseJpa
public class DeterlabAutoConfiguration {

    //    @Bean
//    @ConditionalOnMissingBean(AdapterDeterlab.class)
//    public AdapterDeterlab adapterDeterlab(DeterlabUserRepository deterlabUserRepository, Co) {
//        return new AdapterDeterlab(deterlabUserRepository);
//    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
