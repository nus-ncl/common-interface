package sg.ncl.service.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
public class AuthenticationApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

}
