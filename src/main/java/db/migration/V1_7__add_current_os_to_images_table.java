package db.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by dcszwang on 11/11/2016.
 */
@Slf4j
public class V1_7__add_current_os_to_images_table implements SpringJdbcMigration {

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
        migrate(jdbcTemplate, "images", "current_os");
    }

    private void migrate(final JdbcTemplate jdbcTemplate, final String table, final String column) {
        final String s1 = String.format("ALTER TABLE prod.%s ADD COLUMN %s VARCHAR(255) NULL", table, column);
        jdbcTemplate.update(s1);
    }
}
