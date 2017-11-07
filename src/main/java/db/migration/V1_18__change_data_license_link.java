package db.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by dcsjnh on 7/11/2017.
 */
@Slf4j
public class V1_18__change_data_license_link implements SpringJdbcMigration {

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
        log.info("Setting public domain license link.");
        updateLicense(jdbcTemplate, "CC0", "https://creativecommons.org/publicdomain/zero/1.0/");
    }

    private void updateLicense(final JdbcTemplate jdbcTemplate, final String acronym, final String link) {
        final String sql = "UPDATE prod.data_licenses SET link=? WHERE acronym=?";
        final int i = jdbcTemplate.update(sql, link, acronym);
        log.info("Number of rows affected: {}", i);
    }

}
