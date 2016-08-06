package sg.ncl.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(ExceptionProperties.class)
public class ExceptionAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAutoConfiguration.class);

    private final ExceptionProperties properties;

    @Inject
    ExceptionAutoConfiguration(@NotNull final ExceptionProperties properties) {
        this.properties = properties;
    }

    @Bean
    public ExceptionToHttpStatus exceptionToHttpStatus() {
        final ExceptionToHttpStatus exceptionToHttpStatus = new ExceptionToHttpStatus();
        final Map<String, String> mappings = properties.getMappings();
        mappings.entrySet().forEach(entry -> {
            try {
                final Class<? extends Exception> clazz = Class.forName(entry.getKey()).asSubclass(Exception.class);
                final HttpStatus status = HttpStatus.valueOf(entry.getValue());
                logger.info("Mapped: {} -> {}", clazz, status);
                exceptionToHttpStatus.put(clazz, status);
            } catch (ClassNotFoundException | ClassCastException e) {
                logger.warn("{}: {}", e.getClass().getName(), entry.getKey());
            } catch (IllegalArgumentException e) {
                logger.warn("{}: {}", e.getClass().getName(), entry.getValue());
            }
        });
        return exceptionToHttpStatus;
    }

}
