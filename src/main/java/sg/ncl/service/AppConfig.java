package sg.ncl.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import sg.ncl.service.experiment.*;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.AppConfig")
@Import({sg.ncl.service.authentication.AppConfig.class,
        sg.ncl.service.experiment.AppConfig.class,
        sg.ncl.service.realization.AppConfig.class,
        sg.ncl.service.user.AppConfig.class,
        sg.ncl.service.team.AppConfig.class,
        sg.ncl.service.version.AppConfig.class})
public class AppConfig {
}
