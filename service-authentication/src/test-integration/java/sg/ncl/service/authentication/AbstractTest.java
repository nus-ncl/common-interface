package sg.ncl.service.authentication;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author Christopher Zhong
 */
@SpringApplicationConfiguration(AuthenticationApplication.class)
@WebAppConfiguration
public abstract class AbstractTest extends sg.ncl.common.test.AbstractTest {}
