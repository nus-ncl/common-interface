package sg.ncl.service.team;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import sg.ncl.common.authentication.AuthenticationAutoConfiguration;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.jpa.UseJpa;
import sg.ncl.common.jwt.JwtAutoConfiguration;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
@Import({
        AuthenticationAutoConfiguration.class,
        ExceptionAutoConfiguration.class,
        JwtAutoConfiguration.class
})
public class TeamApplication {

    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(TeamApplication.class, args)) {
            // nothing to do
        }
    }

}
