package sg.ncl.service.mail;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static sg.ncl.service.mail.MailProperties.PREFIX;

/**
 * Created by Chris on 8/26/2016.
 */
@ConfigurationProperties(prefix = PREFIX)
@Getter
@Setter
public class MailProperties {

    static final String PREFIX = "ncl.mail";

    /**
     * The initial delay.
     */
    private String delay;
    /**
     * The interval between retries.
     */
    private String interval;
    /**
     * The number of times to retry.
     */
    private String retry;

}
