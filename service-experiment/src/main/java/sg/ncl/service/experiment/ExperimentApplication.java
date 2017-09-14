package sg.ncl.service.experiment;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sg.ncl.adapter.deterlab.DeterLabAutoConfiguration;
import sg.ncl.common.jpa.UseJpa;
import sg.ncl.service.realization.RealizationApplication;
import sg.ncl.service.team.TeamApplication;

import java.io.IOException;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
@Import({
        DeterLabAutoConfiguration.class,
        TeamApplication.class,
        RealizationApplication.class,
})
public class ExperimentApplication {

    private static final String INTERNET_REQUEST_TEMPLATE = "internetRequestTemplate.tfl";

    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(ExperimentApplication.class, args)) {
            // nothing to do
        }
    }

    @Bean
    Template internetRequestTemplate(final Configuration configuration) throws IOException {
        return configuration.getTemplate(INTERNET_REQUEST_TEMPLATE);
    }

}
