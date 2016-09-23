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
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import sg.ncl.common.jwt.JwtAuthenticationProvider;
import sg.ncl.common.jwt.JwtFilter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Initializes a {@link PasswordEncoder} and configures {@link HttpSecurity}.
 *
 * @author Christopher Zhong
 * @version 1.0
 */
@Configuration
@ConditionalOnClass({BCryptPasswordEncoder.class, HttpSecurity.class})
@EnableConfigurationProperties(AuthenticationProperties.class)
@EnableGlobalAuthentication
@EnableGlobalMethodSecurity
@EnableWebSecurity
@Import({JwtFilter.class, JwtAuthenticationProvider.class})
@Slf4j
public class AuthenticationAutoConfiguration extends WebSecurityConfigurerAdapter {

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
    protected void configure(final HttpSecurity http) throws Exception {
        permitUri(http
                .csrf().disable() // RESTful APIs are immune to CSRF
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // RESTful APIs should be stateless
                .exceptionHandling().authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
                .formLogin().disable() // not needed for RESTful APIs
                .logout().disable() // not needed for RESTful APIs
                .httpBasic().disable() // not using basic authentication
                .rememberMe().disable() // JWT do not need to remember me
                .requestCache().disable() // RESTful APIs should not require caching
                .x509().disable() // not using x509

                .addFilterAt(jwtFilter, BasicAuthenticationFilter.class)

                // add url that no need be authenticated
                .authorizeRequests())
                .anyRequest().authenticated();
    }

    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry permitUri(final ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry http) {
        getUri().forEach((uri, method) -> {
            log.info("Permitting {} {}", method, uri);
            http.antMatchers(method, uri).permitAll();
        });
        return http;
    }

    private Map<String, HttpMethod> getUri() {
        return properties.getUri().entrySet().stream().filter(this::filter).collect(Collectors.toMap(Entry::getKey, this::resolve));
    }

    private boolean filter(final Entry<String, String> e) {
        final HttpMethod method = resolve(e);
        if (method == null) {
            log.warn("Invalid method for URI: {} = {}", e.getKey(), e.getValue());
        }
        return method != null;
    }

    private HttpMethod resolve(final Entry<String, String> e) {
        return HttpMethod.resolve(e.getValue());
    }

}
