package sg.ncl.service.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import sg.ncl.common.jpa.UseJpa;

/**
 * Created by dcszwang on 10/5/2016.
 */
@SpringBootApplication
@UseJpa
public class ImageApplication {
    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(ImageApplication.class, args)) {
            // nothing to do
        }
    }
}
