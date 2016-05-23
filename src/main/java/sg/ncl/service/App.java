package sg.ncl.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication(exclude = {EnableWebSecurity.class, SpringBootApplication.class})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
