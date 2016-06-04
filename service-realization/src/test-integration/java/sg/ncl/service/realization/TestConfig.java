package sg.ncl.service.realization;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Named;
import javax.sql.DataSource;

/**
 * @author Christopher Zhong
 */
@ContextConfiguration(classes = App.class)
public class TestConfig {

    @Named
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

}
