package sg.ncl.service.version;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import sg.ncl.service.version.domain.VersionInfo;

import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@Profile("version")
public class App {

    @Bean
    VersionInfo versionInfo() {
        return new VersionInfo(1, 0, "3ade5", ZonedDateTime.now());
    }

    public static void main(final String[] args) {
        SpringApplication.run(App.class, args);
    }

}
