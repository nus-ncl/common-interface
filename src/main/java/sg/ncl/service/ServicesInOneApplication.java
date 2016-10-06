package sg.ncl.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import sg.ncl.service.authentication.AuthenticationApplication;
import sg.ncl.service.data.DataApplication;
import sg.ncl.service.experiment.ExperimentApplication;
import sg.ncl.service.mail.MailApplication;
import sg.ncl.service.realization.RealizationApplication;
import sg.ncl.service.registration.RegistrationApplication;
import sg.ncl.service.team.TeamApplication;
import sg.ncl.service.user.UserApplication;
import sg.ncl.service.version.VersionApplication;

import java.sql.SQLException;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@Import({
        AuthenticationApplication.class,
        DataApplication.class,
        ExperimentApplication.class,
        MailApplication.class,
        RealizationApplication.class,
        RegistrationApplication.class,
        TeamApplication.class,
        UserApplication.class,
        VersionApplication.class
})
public class ServicesInOneApplication {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        final ConfigurableApplicationContext context = SpringApplication.run(ServicesInOneApplication.class, args);
        final ConfigurableEnvironment environment = context.getEnvironment();

        final FirstRun firstRun = context.getBean(FirstRun.class);
        if (environment.containsProperty("reset")) {
            firstRun.reset();
        }
    }

}
