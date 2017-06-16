package sg.ncl.service.data;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import sg.ncl.common.jpa.UseJpa;

import java.io.IOException;

/**
 * Created by dcszwang on 10/5/2016.
 */
@SpringBootApplication
@EnableScheduling
@UseJpa
public class DataApplication {

    private static final String REQUEST_ACCESS_TEMPLATE = "requestAccessTemplate.ftl";
    private static final String APPROVED_ACCESS_TEMPLATE = "approvedAccessTemplate.ftl";

    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(DataApplication.class, args)) {
            // nothing to do
        }
    }

    @Bean
    Template requestAccessTemplate(final Configuration configuration) throws IOException {
        return configuration.getTemplate(REQUEST_ACCESS_TEMPLATE);
    }

    @Bean
    Template approvedAccessTemplate(final Configuration configuration) throws IOException {
        return configuration.getTemplate(APPROVED_ACCESS_TEMPLATE);
    }

}
