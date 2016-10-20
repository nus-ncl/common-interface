package sg.ncl;

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcszwang on 10/13/2016.
 */

public class FlywayTest {

    private static final String SCHEMA = "prod";
    private DataSource dataSource;
    private JdbcTemplate template;
    private Flyway flyway;

    @Before
    public void setup() throws SQLException {
        this.dataSource = createDataSource();
        this.template = new JdbcTemplate(dataSource);
        this.flyway = new Flyway();
        this.flyway.setDataSource(dataSource);
    }

    @Test
    public void testConversionDatesFromBlob() throws Exception {

        flyway.migrate();

        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".addresses",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".credentials",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".credentials_roles",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".deterlab_project",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".deterlab_user",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".email_retries",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".experiments",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".user_details",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".users",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".login_activities",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".realizations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".registrations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".teams",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".team_members",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".users_teams",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".data",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".data_resources",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".data_users",
                Integer.class)).isEqualTo(0);

        List<Map<String, Object>> results = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + ".addresses");
        assertThat(results.get(8).toString().toUpperCase()).contains("FIELD=CREATED_DATE, TYPE=TIMESTAMP");
        assertThat(results.get(9).toString().toUpperCase()).contains("FIELD=LAST_MODIFIED_DATE, TYPE=TIMESTAMP");

        template.execute("DROP ALL OBJECTS");
    }

    private SimpleDriverDataSource createDataSource() {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass(org.h2.Driver.class);
        simpleDriverDataSource.setUrl("jdbc:h2:mem:prod;MODE=MySQL;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS \"public\"");
        simpleDriverDataSource.setUsername("sa");
        simpleDriverDataSource.setPassword("");
        return simpleDriverDataSource;
    }
}
