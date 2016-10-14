package sql2;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 10/12/2016.
 */
@Slf4j
public class V1_2__insert_address_data implements SpringJdbcMigration {

    private static final String SQL_INSERT_ADDRESS = "INSERT INTO addresses"
            + "(created_date, last_modified_date, version, address_1, address_2, city, country, region, zip_code) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";


    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {

        final int addressId = createAddress(jdbcTemplate, "Address2", "", "City", "Country", "Region", "123456");

    }

    private int createAddress(final JdbcTemplate jdbcTemplate, final String address1, final String address2, final String city, final String country, final String region, final String zip) throws SQLException {
        // insert the address
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final int i = jdbcTemplate.update(c -> {
            final ZonedDateTime now = ZonedDateTime.now();
            final PreparedStatement statement = c.prepareStatement(SQL_INSERT_ADDRESS, new String[]{"id"});
            statement.setObject(1, now);
            statement.setObject(2, now);
            statement.setInt(3, 0);
            statement.setString(4, address1);
            statement.setString(5, address2);
            statement.setString(6, city);
            statement.setString(7, country);
            statement.setString(8, region);
            statement.setString(9, zip);
            return statement;
        }, keyHolder);
        final int id = keyHolder.getKey().intValue();
        log.info("Insert {} address entry with id = {}", i, id);
        return id;
    }

}
