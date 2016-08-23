package sg.ncl.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sg.ncl.common.jpa.UseJpa;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
