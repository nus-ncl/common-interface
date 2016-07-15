package sg.ncl.common.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@SpringBootApplication
@UseJpa
public class TestApp {
    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }
}
