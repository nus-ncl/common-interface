package sg.ncl.service.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import sg.ncl.adapter.deterlab.DeterLabAutoConfiguration;
import sg.ncl.common.jpa.UseJpa;

/**
 * @author: Tran Ly  Vu
 * @version: 1.0
 */
@SpringBootApplication
@UseJpa
@Import({DeterLabAutoConfiguration.class})
public class AnalyticsApplication {

    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(AnalyticsApplication.class, args)) {
            // nothing to do
        }
    }
}
