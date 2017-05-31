package db.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * Created by dcsjnh on 30/5/2017.
 */
@Slf4j
public class V1_15__insert_data_licenses implements SpringJdbcMigration {

    private static final String SQL_INSERT_LICENSE =
                    "INSERT INTO prod.data_licenses " +
                    "(created_date, last_modified_date, version, name, acronym, description, link) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private int createLicense(final JdbcTemplate jdbcTemplate,
                              final String name,
                              final String acronym,
                              final String description,
                              final String link) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final Timestamp now = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());
        final int version = 0;
        final int i = jdbcTemplate.update(c -> {
            final PreparedStatement statement = c.prepareStatement(SQL_INSERT_LICENSE, new String[]{"id"});
            statement.setObject(1, now);
            statement.setObject(2, now);
            statement.setInt(3, version);
            statement.setString(4, name);
            statement.setString(5, acronym);
            statement.setString(6, description);
            statement.setString(7, link);
            return statement;
        }, keyHolder);
        final int id = keyHolder.getKey().intValue();
        log.info("Inserted {} category entry: id={}, created_date={}, last_modified_date={}, version={}, name={}, description={}",
                 i, id, now, now, version, name, description);
        return id;
    }

    private void updateData(final JdbcTemplate jdbcTemplate, final int license_id) {
        final String sql = "UPDATE prod.data SET license_id=?";
        final int i = jdbcTemplate.update(sql, license_id);
        log.info("Number of rows affected: {}", i);
    }

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
        log.info("Creating licenses and getting id for license 'CC BY-NC-ND'.");
        createLicense(jdbcTemplate,
                "Public Domain",
                "CC0",
                "Your work/data is free for use by anyone for any purpose without any restriction.",
                "https://creativecommons.org/share-your-work/public-domain/cc0/");
        createLicense(jdbcTemplate,
                "Creative Commons Attribution",
                "CC BY",
                "This license lets others distribute, remix, tweak, and build upon your work/data, even commercially, as long as they credit you for the original creation.",
                "https://creativecommons.org/licenses/by/4.0/");
        createLicense(jdbcTemplate,
                "Creative Commons Attribution ShareAlike",
                "CC BY-SA",
                "This license lets others remix, tweak, and build upon your work/data even for commercial purposes, as long as they credit you and license their new creations under the identical terms.",
                "https://creativecommons.org/licenses/by-sa/4.0/");
        createLicense(jdbcTemplate,
                "Creative Commons Attribution No Derivatives",
                "CC BY-ND",
                "This license allows for redistribution, commercial and non-commercial, as long as it is passed along unchanged and in whole, with credit to you.",
                "https://creativecommons.org/licenses/by-nd/4.0/");
        createLicense(jdbcTemplate,
                "Creative Commons Attribution Non-Commercial",
                "CC BY-NC",
                "This license lets others remix, tweak, and build upon your work/data non-commercially, and  their new works must acknowledge you.",
                "https://creativecommons.org/licenses/by-nc/4.0/");
        createLicense(jdbcTemplate,
                "Creative Commons Attribution Non-Commercial ShareAlike",
                "CC BY-NC-SA",
                "This license lets others remix, tweak, and build upon your work/data non-commercially, as long as they credit you and license their new creations under the identical terms.",
                "https://creativecommons.org/licenses/by-nc-sa/4.0");
        int license_id = createLicense(jdbcTemplate,
                "Creative Commons Attribution Non-Commercial No Derivatives",
                "CC BY-NC-ND",
                "This license lets others to download your works/data and share them with others as long as they credit you, but they can’t change them in any way or use them commercially.",
                "https://creativecommons.org/licenses/by-nc-nd/4.0");

        log.info("Setting id for license 'CC BY-NC-ND' into existing datasets.");
        updateData(jdbcTemplate, license_id);
    }

}
