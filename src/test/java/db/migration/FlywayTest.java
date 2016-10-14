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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcszwang on 10/13/2016.
 */

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = {"flyway.enabled=false"})
public class FlywayTest {

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
        this.template.update("CREATE SCHEMA IF NOT EXISTS ncl");

        flyway = new Flyway();
        flyway.setDataSource(dataSource);

    }


    @Test
    public void testEmptySchemaInitializeTables() throws Exception {

        System.out.println(template.queryForList("SHOW SCHEMAS", String.class));

        flyway.setLocations("classpath:sql");
        flyway.migrate();



        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.addresses",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from credentials",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from credentials_roles",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from deterlab_project",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from deterlab_user",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from email_retries",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from experiments",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from user_details",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from users",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from login_activities",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from realizations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from registrations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from teams",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from team_members",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from users_teams",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from schema_version",
                Integer.class)).isEqualTo(1);
    }

    @Test
    public void testEmptySchemaInitializeTablesAndData() throws Exception {
        flyway.setLocations("classpath:sql2");
        flyway.migrate();

        assertThat(this.template.queryForObject("SELECT COUNT(*) from addresses",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from credentials",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from credentials_roles",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from deterlab_project",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from deterlab_user",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from email_retries",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from experiments",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from user_details",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from users",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from login_activities",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from realizations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from registrations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from teams",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from team_members",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from users_teams",
                Integer.class)).isEqualTo(1);
    }

    public void wipe() throws SQLException {
        final String[] tables = {
                "credentials_roles",
                "credentials",
                "deterlab_project",
                "deterlab_user",
                "email_retries",
                "experiments",
                "login_activities",
                "realizations",
                "registrations",
                "team_members",
                "teams",
                "users_teams",
                "users",
                "user_details",
                "addresses"
        };
        for (String table : tables) {
            final String sql = "DELETE FROM " + table;
            int i = template.update(sql);
            log.info("Delete {} entry/entries from '{}' table", i, table);
        }
    }
}
