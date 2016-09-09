package sg.ncl.service.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Created by dcszwang on 9/7/2016.
 */
@SpringBootApplication
@Import(MailAutoConfiguration.class)
public class TestSpringBootApp {
    public static void main(String[] args) {
        SpringApplication.run(TestSpringBootApp.class, args);
    }
}