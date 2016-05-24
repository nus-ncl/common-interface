package sg.ncl.service.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import sg.ncl.common.jpa.UseJpa;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
@Profile("user")
public class App {

    public static void main(final String[] args) {
        SpringApplication.run(App.class, args);
    }

}
