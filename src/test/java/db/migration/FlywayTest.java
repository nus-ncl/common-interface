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
        this.flyway = new Flyway();
        this.flyway.setDataSource(dataSource);
        this.flyway.setSchemas("ncl");
    }

    @Test
    public void testEmptySchemaInitializeTables() throws Exception {
        template.update("DROP SCHEMA IF EXISTS ncl");
        flyway.setLocations("classpath:sql");
        flyway.migrate();

        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.addresses",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.credentials",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.credentials_roles",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.deterlab_project",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.deterlab_user",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.email_retries",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.experiments",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.user_details",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.users",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.login_activities",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.realizations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.registrations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.teams",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.team_members",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.users_teams",
                Integer.class)).isEqualTo(0);
    }

/*
    @Test
    public void testEmptySchemaInitializeTablesAndData() throws Exception {
        template.update("DROP SCHEMA IF EXISTS ncl");
        flyway.setLocations("classpath:sql2");
        flyway.migrate();

        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.addresses",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.credentials",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.credentials_roles",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.deterlab_project",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.deterlab_user",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.email_retries",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.experiments",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.user_details",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.users",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.login_activities",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.realizations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.registrations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.teams",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.team_members",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from ncl.users_teams",
                Integer.class)).isEqualTo(1);
    }
*/

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
