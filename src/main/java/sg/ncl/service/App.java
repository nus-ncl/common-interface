package sg.ncl.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication(scanBasePackageClasses = App.class)
@Import({sg.ncl.service.authentication.App.class,
        sg.ncl.service.experiment.App.class,
        sg.ncl.service.realization.App.class,
        sg.ncl.service.user.App.class,
        sg.ncl.service.team.App.class,
        sg.ncl.service.version.App.class})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
