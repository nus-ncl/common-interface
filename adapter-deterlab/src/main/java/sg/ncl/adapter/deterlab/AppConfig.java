package sg.ncl.adapter.deterlab;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.adapter.deterlab.AppConfig")
@Import({App.class})
public class AppConfig  {



}
