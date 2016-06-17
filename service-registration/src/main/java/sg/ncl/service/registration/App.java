package sg.ncl.service.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import sg.ncl.common.jpa.UseJpa;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
public class App {

    public static void main(final String[] args) {
        SpringApplication.run(App.class, args);
    }

}
