package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by dcszwang on 10/13/2016.
 */
public class V1_2__convert_tinyblob_to_datetime implements SpringJdbcMigration {

    static final String SQL_UPDATE_ADDRESS = "ALTER TABLE addresses CHANGE created_date created_date_blob TINYBLOB; "
            + "ALTER TABLE addresses ADD created_date DATETIME";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {

        // convert addresses table

    }
}