package sg.ncl.adapter.openstack;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import sg.ncl.common.jpa.UseJpa;

/**
 * Author: Tran Ly Vu
 */

@Configuration
@ComponentScan
@EnableConfigurationProperties(OpenStackConnectionProperties.class)
@UseJpa
public class OpenStackAutoConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
