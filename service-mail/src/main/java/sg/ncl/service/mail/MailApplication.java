package sg.ncl.service.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import sg.ncl.common.jpa.UseJpa;

/**
 * Created by dcszwang on 8/10/2016.
 */

@SpringBootApplication
@UseJpa
@EnableScheduling
public class MailApplication {

    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(MailApplication.class, args)) {
            // nothing to do
        }
    }
}
