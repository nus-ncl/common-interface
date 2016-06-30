package sg.ncl.common.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@Configuration
@EnableConfigurationProperties(AuthenticationProperties.class)
public class AuthenticationAutoConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAutoConfiguration.class);

    @Inject
    private AuthenticationProperties properties;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable();
        final String url = properties.getUrl();
        if (url == null || url.isEmpty()) {
            logger.info("No authentication path defined");
        } else {
            logger.info("Authentication path: {}", url);
            http
                    .authorizeRequests().antMatchers("/authentication").permitAll().and();
        }
        http
                // TODO add authentication
                .authorizeRequests().anyRequest().permitAll();
    }

}
