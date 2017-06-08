package db.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by teye
 */
@Slf4j
public class V1_16__add_is_malicious_to_data_resources_table implements SpringJdbcMigration {

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
        migrate(jdbcTemplate, "data_resources", "is_malicious");
    }

    private void migrate(final JdbcTemplate jdbcTemplate, final String table, final String column) {
        final String s1 = String.format("ALTER TABLE prod.%s ADD COLUMN %s CHAR(1) NOT NULL", table, column);
        jdbcTemplate.update(s1);
    }
}
