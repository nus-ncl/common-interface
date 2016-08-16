package sg.ncl.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Base class for configuring NCL domain.
 *
 * @author Christopher Zhong
 * @version 1.0
 */
@ConfigurationProperties(prefix = DomainProperties.PREFIX)
@Getter
@Setter
public class DomainProperties {

    static final String PREFIX = "ncl";

    /**
     * The domain.
     */
    private String domain;

}
