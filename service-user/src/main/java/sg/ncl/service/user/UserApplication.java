package sg.ncl.service.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sg.ncl.common.jpa.UseJpa;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
public class UserApplication {

    public static void main(final String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
