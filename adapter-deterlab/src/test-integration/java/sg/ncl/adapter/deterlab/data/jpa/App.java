package sg.ncl.adapter.deterlab.data.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import sg.ncl.adapter.deterlab.DeterlabAutoConfiguration;
import sg.ncl.common.jpa.UseJpa;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@Import(DeterlabAutoConfiguration.class)
public class App {

    public static void main(final String[] args) {
        SpringApplication.run(App.class, args);
    }

}
