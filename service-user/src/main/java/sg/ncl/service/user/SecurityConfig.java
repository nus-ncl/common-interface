package sg.ncl.service.user;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.user.SecurityConfig")
@Order(8)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/users").authenticated().and()
                .httpBasic();
    }

}
