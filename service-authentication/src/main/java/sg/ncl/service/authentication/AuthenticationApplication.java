package sg.ncl.service.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sg.ncl.common.jpa.UseJpa;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
public class AuthenticationApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

}
