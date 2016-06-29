package sg.ncl.service.team;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sg.ncl.common.jpa.UseJpa;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
public class TeamApplication {

    public static void main(final String[] args) {
        SpringApplication.run(TeamApplication.class, args);
    }

}
