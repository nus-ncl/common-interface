package sg.ncl.service.registration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.jpa.JpaConfig;
import sg.ncl.common.jwt.JwtAutoConfiguration;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.registration.AppConfig")
@Import({sg.ncl.service.authentication.AuthenticationApplication.class, sg.ncl.service.team.App.class, sg.ncl.service.user.App.class})
public class AppConfig  {



}
