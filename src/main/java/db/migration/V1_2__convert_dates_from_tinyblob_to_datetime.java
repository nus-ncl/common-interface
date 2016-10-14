package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by dcszwang on 10/13/2016.
 */
public class V1_2__convert_dates_from_tinyblob_to_datetime implements SpringJdbcMigration {

    private static final String SQL_UPDATE_ADDRESS = "ALTER TABLE addresses "
            + "CHANGE COLUMN created_date old_created_date TINYBLOB,"
            + "ADD COLUMN created_date DATETIME,"
            + "CHANGE COLUMN last_modified_date old_last_modified_date TINYBLOB,"
            + "ADD COLUMN last_modified_date DATETIME"
;

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {

        // migrate address table: created_date and last_modified_date
        jdbcTemplate.update(SQL_UPDATE_ADDRESS);
    }
}
