package sg.ncl.service.experiment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import sg.ncl.adapter.deterlab.DeterlabAutoConfiguration;
import sg.ncl.common.jpa.UseJpa;
import sg.ncl.service.realization.RealizationApplication;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
@Import({
        DeterlabAutoConfiguration.class,
        RealizationApplication.class,
})
public class ExperimentApplication {

    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(ExperimentApplication.class, args)) {
            // nothing to do
        }
    }

}
