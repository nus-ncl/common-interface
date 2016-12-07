package sg.ncl.service.transmission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by dcsjnh on 11/23/2016.
 */
@SpringBootApplication
public class UploadApplication {
    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(UploadApplication.class, args)) {}
    }
}
