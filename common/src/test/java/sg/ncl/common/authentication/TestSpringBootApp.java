package sg.ncl.common.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import sg.ncl.common.jwt.JwtAuthenticationProvider;
import sg.ncl.common.jwt.JwtAutoConfiguration;
import sg.ncl.common.jwt.JwtFilter;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@SpringBootApplication
@Import({AuthenticationAutoConfiguration.class, JwtFilter.class, JwtAutoConfiguration.class, JwtAuthenticationProvider.class})
public class TestSpringBootApp {
    public static void main(String[] args) {
        SpringApplication.run(TestSpringBootApp.class, args);
    }
}
