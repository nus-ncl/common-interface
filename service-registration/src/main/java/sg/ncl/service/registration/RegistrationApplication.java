package sg.ncl.service.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import sg.ncl.adapter.deterlab.DeterlabAutoConfiguration;
import sg.ncl.common.jpa.UseJpa;
import sg.ncl.service.authentication.AuthenticationApplication;
import sg.ncl.service.team.TeamApplication;
import sg.ncl.service.user.UserApplication;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
@Import({
        AuthenticationApplication.class,
        TeamApplication.class,
        UserApplication.class,
        DeterlabAutoConfiguration.class
})
public class RegistrationApplication {

    public static void main(final String[] args) {
        SpringApplication.run(RegistrationApplication.class, args);
    }

}
