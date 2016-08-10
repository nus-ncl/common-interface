package sg.ncl.service.realization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import sg.ncl.adapter.deterlab.DeterLabAutoConfiguration;
import sg.ncl.common.jpa.UseJpa;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
@Import({DeterLabAutoConfiguration.class})
public class RealizationApplication {

    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(RealizationApplication.class, args)) {
            // nothing to do
        }
    }

}
