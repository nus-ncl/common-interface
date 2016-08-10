package sg.ncl.service.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import sg.ncl.common.jpa.JpaAutoConfiguration;
import sg.ncl.common.jpa.UseJpa;
import sg.ncl.common.jwt.JwtAutoConfiguration;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
@Import({JpaAutoConfiguration.class, JwtAutoConfiguration.class})
public class UserApplication {

    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(UserApplication.class, args)) {
            // nothing to do
        }
    }

}
