package db.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcszwang on 10/13/2016.
 */

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = {"flyway.enabled=false"})
public class FlywayTest {

    static final String schema = "prod";

    @Configuration
    static class TestConfig {
    }

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate template;
    private Flyway flyway;

    @Before
    public void setup() {
        this.template = new JdbcTemplate(dataSource);
        this.flyway = new Flyway();
        this.flyway.setDataSource(dataSource);
    }

    @Test
    public void testEmptySchemaInitializeTables() throws Exception {
        flyway.setLocations("classpath:sql");
        flyway.migrate();

        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".addresses",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".credentials",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".credentials_roles",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".deterlab_project",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".deterlab_user",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".email_retries",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".experiments",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".user_details",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".users",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".login_activities",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".realizations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".registrations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".teams",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".team_members",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".users_teams",
                Integer.class)).isEqualTo(0);

        template.execute("DROP ALL OBJECTS");
    }


    @Test
    public void testEmptySchemaInitializeTablesAndData() throws Exception {

        flyway.setLocations("classpath:sql", "classpath:sql1");
        flyway.migrate();

        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".addresses",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".credentials",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".credentials_roles",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".deterlab_project",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".deterlab_user",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".email_retries",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".experiments",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".user_details",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".users",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".login_activities",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".realizations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".registrations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".teams",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".team_members",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".users_teams",
                Integer.class)).isEqualTo(1);

        List<Map<String, Object>> results = this.template.queryForList("SHOW COLUMNS FROM " + schema + ".addresses");
        assertThat(results.get(1).toString()).contains("FIELD=CREATED_DATE, TYPE=BLOB");
        assertThat(results.get(2).toString()).contains("FIELD=LAST_MODIFIED_DATE, TYPE=BLOB");

        template.execute("DROP ALL OBJECTS");
    }

    @Test
    public void testConversionDatesFromBlob() throws Exception {

        flyway.setLocations("classpath:sql", "classpath:sql1", "classpath:sql2");
        flyway.migrate();

        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".addresses",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".credentials",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".credentials_roles",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".deterlab_project",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".deterlab_user",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".email_retries",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".experiments",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".user_details",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".users",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".login_activities",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".realizations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".registrations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".teams",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".team_members",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + schema + ".users_teams",
                Integer.class)).isEqualTo(1);

        List<Map<String, Object>> results = this.template.queryForList("SHOW COLUMNS FROM " + schema + ".addresses");
        assertThat(results.get(8).toString()).contains("FIELD=CREATED_DATE, TYPE=TIMESTAMP");
        assertThat(results.get(9).toString()).contains("FIELD=LAST_MODIFIED_DATE, TYPE=TIMESTAMP");

        template.execute("DROP ALL OBJECTS");
    }

    public void wipe() throws SQLException {
        final String[] tables = {
                "addresses",
                "credentials",
                "credentials_roles",
                "deterlab_project",
                "deterlab_user",
                "email_retries",
                "experiments",
                "login_activities",
                "realizations",
                "registrations",
                "schema_version",
                "team_members",
                "teams",
                "user_details",
                "users",
                "users_teams"
        };
        for (String table : tables) {
            final String sql = "DELETE FROM " + table;
            int i = this.template.update(sql);
            log.info("Delete {} entry/entries from '{}' table", i, table);
        }
    }
}
