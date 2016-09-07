package sg.ncl.common.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Initializes a {@link PasswordEncoder} and configures {@link HttpSecurity}.
 *
 * @author Christopher Zhong
 * @version 1.0
 */
@Configuration
@ConditionalOnClass({BCryptPasswordEncoder.class, HttpSecurity.class})
@EnableConfigurationProperties(AuthenticationProperties.class)
@Slf4j
public class AuthenticationAutoConfiguration extends WebSecurityConfigurerAdapter {

    private static final String DEFAULT_URL = "/authentications";

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
        http
                .authorizeRequests().antMatchers(getUrl()).permitAll().and();
        http
                .authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("**/*.html").permitAll()
                .antMatchers("**/*.css").permitAll()
                .antMatchers("**/*.js").permitAll();
        http
                // TODO add authentication
                .authorizeRequests().anyRequest().permitAll().and();
    }

    private String getUrl() {
        final String url = properties.getUrl();
        if (url == null || url.isEmpty()) {
            log.warn("An authentication path was not defined; using default: '{}'", DEFAULT_URL);
            return DEFAULT_URL;
        }
        log.info("Using specified authentication path: '{}'", url);
        return url;
    }

}
