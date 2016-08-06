package sg.ncl.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Configures exception handling.
 *
 * @author Christopher Zhong
 * @version 1.0
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(ExceptionProperties.class)
@Slf4j
public class ExceptionAutoConfiguration {

    private final ExceptionProperties properties;

    @Inject
    ExceptionAutoConfiguration(@NotNull final ExceptionProperties properties) {
        this.properties = properties;
    }

    @Bean
    public ExceptionHttpStatusMap exceptionToHttpStatus() {
        final ExceptionHttpStatusMap exceptionHttpStatusMap = new ExceptionHttpStatusMap();
        final Map<String, String> mappings = properties.getMappings();
        mappings.entrySet().forEach(entry -> {
            try {
                final Class<? extends Exception> clazz = Class.forName(entry.getKey()).asSubclass(Exception.class);
                final HttpStatus status = HttpStatus.valueOf(entry.getValue());
                log.info("Mapping: '{}' -> '{}'", clazz, status);
                exceptionHttpStatusMap.put(clazz, status);
            } catch (ClassNotFoundException | ClassCastException e) {
                log.warn("{}: '{}'", e, entry.getKey());
            } catch (IllegalArgumentException e) {
                log.warn("{}: '{}'", e, entry.getValue());
            }
        });
        return exceptionHttpStatusMap;
    }

}
