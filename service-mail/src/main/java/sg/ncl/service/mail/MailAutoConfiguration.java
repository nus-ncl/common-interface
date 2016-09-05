package sg.ncl.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.inject.Inject;
import java.time.Duration;
import java.time.format.DateTimeParseException;

/**
 * Created by Chris on 9/5/2016.
 */
@Configuration
@EnableConfigurationProperties(MailProperties.class)
@Slf4j
public class MailAutoConfiguration {

    static final Duration DEFAULT_DELAY = Duration.ofMinutes(5);
    static final Duration DEFAULT_INTERVAL = Duration.ofMinutes(10);
    static final int DEFAULT_RETRY = 3;

    private final MailProperties properties;

    @Inject
    MailAutoConfiguration(final MailProperties properties) {
        this.properties = properties;
    }

    @Bean(name = "delayDuration")
    Duration delayDuration() {
        final String value = properties.getDelay();
        if (value == null || value.isEmpty()) {
            log.warn("No delay was specified; using default: {}", DEFAULT_DELAY);
            return DEFAULT_DELAY;
        }
        try {
            final Duration duration = Duration.parse(value);
            log.info("Using specified delay duration: {}", duration);
            return duration;
        } catch (DateTimeParseException e) {
            log.warn("{}: '{}'; using default: {}", e, value, DEFAULT_DELAY);
            return DEFAULT_DELAY;
        }
    }

    @Bean(name = "intervalDuration")
    Duration intervalDuration() {
        final String value = properties.getInterval();
        if (value == null || value.isEmpty()) {
            log.warn("No interval was specified; using default: {}", DEFAULT_INTERVAL);
            return DEFAULT_INTERVAL;
        }
        try {
            final Duration duration = Duration.parse(value);
            log.info("Using specified interval duration: {}", duration);
            return duration;
        } catch (DateTimeParseException e) {
            log.warn("{}: '{}'; using default: {}", e, value, DEFAULT_INTERVAL);
            return DEFAULT_INTERVAL;
        }
    }

    TaskExecutor taskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        return executor;
    }

    TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        return scheduler;
    }

}
