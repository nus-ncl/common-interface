package sg.ncl.common.authentication;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.web.WebAppConfiguration;
import sg.ncl.common.jwt.JwtAuthenticationProvider;
import sg.ncl.common.jwt.JwtFilter;

/**
 * @author Christopher Zhong
 */
@SpringBootTest(classes = AuthenticationAutoConfiguration.class)
@WebAppConfiguration
public abstract class AbstractTest extends sg.ncl.common.test.AbstractTest {}
