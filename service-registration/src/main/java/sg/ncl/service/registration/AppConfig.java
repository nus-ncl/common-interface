package sg.ncl.service.registration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.registration.AppConfig")
@Import({sg.ncl.service.authentication.AuthenticationApplication.class, sg.ncl.service.team.TeamApplication.class, sg.ncl.service.user.UserApplication.class, sg.ncl.adapter.deterlab.DeterlabAutoConfiguration.class})
public class AppConfig  {



}
