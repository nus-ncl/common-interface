package sg.ncl.service.authentication;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import sg.ncl.service.authentication.services.AuthenticationService;

import javax.sql.DataSource;

/**
 * @author Christopher Zhong
 */
@Configuration
public class TestConfig {

    @Bean
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    @Profile("mock-authentication-service")
    public AuthenticationService authenticationService() {
        return Mockito.mock(AuthenticationService.class);
    }

}
