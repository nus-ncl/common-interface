package sg.ncl.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.annotation.Import;
import sg.ncl.service.authentication.AuthenticationApplication;
import sg.ncl.service.data.DataApplication;
import sg.ncl.service.experiment.ExperimentApplication;
import sg.ncl.service.image.ImageApplication;
import sg.ncl.service.mail.MailApplication;
import sg.ncl.service.realization.RealizationApplication;
import sg.ncl.service.registration.RegistrationApplication;
import sg.ncl.service.team.TeamApplication;
import sg.ncl.service.user.UserApplication;
import sg.ncl.service.version.VersionApplication;
import sg.ncl.adapter.deterlab.AdapterDeterLab;

import java.sql.SQLException;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@Import({
        AuthenticationApplication.class,
        DataApplication.class,
        ExperimentApplication.class,
        ImageApplication.class,
        MailApplication.class,
        RealizationApplication.class,
        RegistrationApplication.class,
        TeamApplication.class,
        UserApplication.class,
        VersionApplication.class,
        // add AdapterDeterLab class
        AdapterDeterLab.class
})
public class ServicesInOneApplication {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        final SpringApplication application = new SpringApplication(ServicesInOneApplication.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.run(args);
    }

}
