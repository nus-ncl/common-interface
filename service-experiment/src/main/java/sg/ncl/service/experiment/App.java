package sg.ncl.service.experiment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import sg.ncl.common.jpa.UseJpa;

/**
 * @author Christopher Zhong
 */
//@SpringBootApplication
//@EnableJpaRepositories
//@EnableJpaAuditing
//@EnableTransactionManagement
@SpringBootApplication
@UseJpa
public class App {

    public static void main(final String[] args) {
        SpringApplication.run(App.class, args);
    }

}
