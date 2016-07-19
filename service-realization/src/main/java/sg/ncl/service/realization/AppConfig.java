package sg.ncl.service.realization;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.realization.AppConfig")
@Import({sg.ncl.adapter.deterlab.DeterlabAutoConfiguration.class})
public class AppConfig {}
