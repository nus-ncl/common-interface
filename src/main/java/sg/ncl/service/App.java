package sg.ncl.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import sg.ncl.common.jpa.UseJpa;

import java.sql.SQLException;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
public class App {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        final ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
        final ConfigurableEnvironment environment = context.getEnvironment();

        final FirstRun firstRun = context.getBean(FirstRun.class);
        if (environment.getProperty("wipe") != null) {
            firstRun.wipe();
        }
        if (environment.getProperty("initialize") != null) {
            firstRun.initialize();
        }
    }

}
