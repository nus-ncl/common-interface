package sg.ncl.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for configuration of exception handling.
 *
 * @author Christopher Zhong
 * @version 1.0
 */
@ConfigurationProperties(prefix = ExceptionProperties.PREFIX)
@Getter
@Setter
public class ExceptionProperties {

    static final String PREFIX = "ncl.exception";

    /**
     * A {@link Map} representing the mapping of {@link Exception}s to {@link org.springframework.http.HttpStatus}es.
     */
    private Map<String, String> mappings = new HashMap<>();

}
