package sg.ncl.common.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import sg.ncl.common.jwt.JwtAuthenticationProvider;
import sg.ncl.common.jwt.JwtFilter;

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
@Import(JwtAuthenticationProvider.class)
@Slf4j
public class AuthenticationAutoConfiguration extends WebSecurityConfigurerAdapter {

//    private static final String DEFAULT_URL = "/authentications";

    private final AuthenticationProperties properties;
    private final JwtFilter jwtFilter;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Inject
    AuthenticationAutoConfiguration(@NotNull final AuthenticationProperties properties, @NotNull final JwtFilter jwtFilter, @NotNull final JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.properties = properties;
        this.jwtFilter = jwtFilter;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable() // RESTful APIs are immune to CSRF
                .formLogin().disable() // not needed for RESTful APIs
                .logout().disable() // not needed for RESTful APIs
//                .openidLogin().disable() // not using OpenID
                .httpBasic().disable() // not using basic authentication
                .rememberMe().disable() // JWT do not need to remember me
                .requestCache().disable() // RESTful APIs should not require caching
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // RESTful APIs should be stateless
                .x509().disable(); // not using x509

        // add url that no need be authenticated
        http
                .authorizeRequests().antMatchers(HttpMethod.GET, "/users").permitAll().and()
                .authorizeRequests().anyRequest().authenticated();
//        http
//                .authorizeRequests().anyRequest().authenticated();
        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

//    private String getUrl() {
//        final String url = properties.getUrl();
//        if (url == null || url.isEmpty()) {
//            log.warn("An authentication path was not defined; using default: '{}'", DEFAULT_URL);
//            return DEFAULT_URL;
//        }
//        log.info("Using specified authentication path: '{}'", url);
//        return url;
//    }

}
