package sg.ncl.service.analytics;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sg.ncl.adapter.deterlab.DeterLabAutoConfiguration;
import sg.ncl.common.jpa.UseJpa;

import java.io.IOException;

/**
 * @author: Tran Ly  Vu
 * @version: 1.0
 */
@SpringBootApplication
@UseJpa
@Import({DeterLabAutoConfiguration.class})
public class AnalyticsApplication {

    private static final String ALERT_DISK_USAGE_TEMPLATE = "alertDiskUsageTemplate.ftl";

    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(AnalyticsApplication.class, args)) {
            // nothing to do
        }
    }

    @Bean
    Template alertDiskUsageTemplate(final Configuration configuration) throws IOException {
        return configuration.getTemplate(ALERT_DISK_USAGE_TEMPLATE);
    }
}
