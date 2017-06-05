package sg.ncl.service.data;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import sg.ncl.common.jpa.UseJpa;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Created by dcszwang on 10/5/2016.
 */
@SpringBootApplication
@EnableAsync
@UseJpa
public class DataApplication extends AsyncConfigurerSupport {

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

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("avscanner-service-");
        executor.initialize();
        return executor;
    }

}
