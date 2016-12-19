package sg.ncl.service.analytic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import sg.ncl.common.jpa.UseJpa;


/**
 * @author Vu
 */
@SpringBootApplication
@UseJpa
public class AnalyticApplication {

    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(AnalyticApplication.class, args)) {
            // nothing to do
        }
    }

}
