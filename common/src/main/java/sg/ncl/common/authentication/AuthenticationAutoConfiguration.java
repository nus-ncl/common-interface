package sg.ncl.common.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@Configuration
@ConditionalOnClass({BCryptPasswordEncoder.class, HttpSecurity.class})
@EnableConfigurationProperties(AuthenticationProperties.class)
public class AuthenticationAutoConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAutoConfiguration.class);

    private final AuthenticationProperties properties;

    @Inject
    AuthenticationAutoConfiguration(@NotNull final AuthenticationProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
                    .authorizeRequests().antMatchers(url).permitAll().and();
        }
        http
                // TODO add authentication
                .authorizeRequests().anyRequest().permitAll();
    }

}
