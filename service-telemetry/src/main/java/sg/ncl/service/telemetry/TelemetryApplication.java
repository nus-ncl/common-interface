package sg.ncl.service.telemetry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import sg.ncl.adapter.deterlab.DeterLabAutoConfiguration;

/**
 * Created by dcsyeoty on 16-Dec-16.
 */
@SpringBootApplication
@Import({DeterLabAutoConfiguration.class})
public class TelemetryApplication {

    private TelemetryApplication() {

    }

    public static void main(String[] args) {
        final SpringApplication application = new SpringApplication(TelemetryApplication.class);
        application.run(args);
    }

}
