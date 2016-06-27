package sg.ncl.service.experiment;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import sg.ncl.service.experiment.domain.Experiment;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.experiment.AppConfig")
@Import({Experiment.class})
public class AppConfig {



}
