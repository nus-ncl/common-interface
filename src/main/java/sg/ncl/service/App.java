package sg.ncl.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication(scanBasePackageClasses = App.class)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
