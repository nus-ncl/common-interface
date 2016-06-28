package sg.ncl.service.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.jpa.JpaAutoConfiguration;
import sg.ncl.common.jwt.JwtAutoConfiguration;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.authentication.AppConfig")
@Import({ExceptionAutoConfiguration.class, JpaAutoConfiguration.class, JwtAutoConfiguration.class})
public class AppConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .authorizeRequests().antMatchers("/authentication").permitAll().and()
                .authorizeRequests().anyRequest().permitAll();
    }

}
