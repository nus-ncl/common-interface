package sg.ncl.common.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@SpringBootApplication
@UseJpa
public class TestSpringBootApp {
    public static void main(String[] args) {
        SpringApplication.run(TestSpringBootApp.class, args);
    }
}
