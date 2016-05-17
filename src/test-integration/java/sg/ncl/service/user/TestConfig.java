package sg.ncl.service.user;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import sg.ncl.service.App;

import javax.sql.DataSource;

/**
 * @author Christopher Zhong
 */
@ContextConfiguration(classes = {App.class})
public class TestConfig {

    @Bean
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

}
