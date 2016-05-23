package sg.ncl.service.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//        @Bean
//    public AuthenticationProvider authenticationProvider() {
//            AuthenticationProvider
//        return new SpringAuthenticationProvider();
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new SpringDataUserDetailsService();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .httpBasic().and()
                .authorizeRequests().antMatchers("/authentication").permitAll();
    }

}
