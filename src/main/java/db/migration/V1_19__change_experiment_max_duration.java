package db.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Set the default max_duration column under experiments table all to '0'
 * Created by dcsyeoty
 */
@Slf4j
public class V1_19__change_experiment_max_duration implements SpringJdbcMigration {

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
        log.debug("Changing experiment max_duration");
        updateMaxDuration(jdbcTemplate, 0);
    }

    private void updateMaxDuration(final JdbcTemplate jdbcTemplate, final int maxDuration) {
        final String sql = "UPDATE prod.experiments SET max_duration=?";
        final int i = jdbcTemplate.update(sql, maxDuration);
        log.debug("Number of rows affected: {}", i);
    }

}
