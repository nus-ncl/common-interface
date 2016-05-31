package sg.ncl.service.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import sg.ncl.common.jwt.JwtConfig;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.authentication.AppConfig")
@Import({JwtConfig.class})
public class AppConfig {}
