package sg.ncl.service.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import sg.ncl.common.jwt.JwtConfig;

/**
 * @author Christopher Zhong
 */
@Configuration
@Import({JwtConfig.class})
public class AppConfig {}
