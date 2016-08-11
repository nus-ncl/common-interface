package sg.ncl.service.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by dcszwang on 8/10/2016.
 */

@SpringBootApplication
public class MailApplication {
    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(MailApplication.class, args)) {
            // nothing to do
        }
    }
}
