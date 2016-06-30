package sg.ncl.common.exception;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@ConfigurationProperties(prefix = ExceptionProperties.PREFIX)
public class ExceptionProperties {

    public static final String PREFIX = "ncl.exception";

    private Map<String, String> mappings = new HashMap<String, String>() {{
        put(BadRequestException.class.getName(), HttpStatus.BAD_REQUEST.name());
        put(ConflictException.class.getName(), HttpStatus.CONFLICT.name());
        put(NotFoundException.class.getName(), HttpStatus.NOT_FOUND.name());
        put(NotModifiedException.class.getName(), HttpStatus.NOT_MODIFIED.name());
        put(UnauthorizedException.class.getName(), HttpStatus.UNAUTHORIZED.name());
    }};

    public Map<String, String> getMappings() {
        return mappings;
    }

    public void setMappings(final Map<String, String> mappings) {
        this.mappings = mappings;
    }

}
