package sg.ncl.service.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import sg.ncl.adapter.deterlab.DeterLabAutoConfiguration;
import sg.ncl.common.DomainProperties;
import sg.ncl.common.jpa.UseJpa;
import sg.ncl.service.authentication.AuthenticationApplication;
import sg.ncl.service.mail.MailApplication;
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
        DeterLabAutoConfiguration.class,
        MailApplication.class,
        DomainProperties.class
})
public class RegistrationApplication {

    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(RegistrationApplication.class, args)) {
            // nothing to do
        }
    }

}
