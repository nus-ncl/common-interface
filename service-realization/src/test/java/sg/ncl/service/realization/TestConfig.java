package sg.ncl.service.realization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;

/**
 * @author Christopher Zhong
 */
@Configuration
public class TestConfig {

//    @Bean
//    DataSource dataSource() {
//        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
//    }

}