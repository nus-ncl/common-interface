package sg.ncl.service.user;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import sg.ncl.common.jpa.JpaAutoConfiguration;
import sg.ncl.common.jwt.JwtAutoConfiguration;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.user.AppConfig")
@Import({JpaAutoConfiguration.class, JwtAutoConfiguration.class})
//@Configuration
public class AppConfig {}
