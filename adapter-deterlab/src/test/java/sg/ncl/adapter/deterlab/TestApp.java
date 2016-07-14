package sg.ncl.adapter.deterlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import sg.ncl.common.jpa.UseJpa;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
@Import(DeterlabAutoConfiguration.class)
public class TestApp {

    public static void main(final String[] args) {
        SpringApplication.run(TestApp.class, args);
    }

}
