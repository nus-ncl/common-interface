package sg.ncl.service.experiment;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.experiment.AppConfig")
@Import({sg.ncl.adapter.deterlab.DeterlabAutoConfiguration.class})
public class AppConfig {



}
