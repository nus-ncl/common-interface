package db.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by dcszwang on 10/13/2016.
 */
@Slf4j
public class V1_2__convert_dates_from_tinyblob_to_datetime implements SpringJdbcMigration {

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {

        // migrate address table: created_date and last_modified_date
        migrate(jdbcTemplate, "addresses", new String[]{"created_date", "last_modified_date"});

        // migrate credentials table: created_date and last_modified_date
        migrate(jdbcTemplate, "credentials", new String[]{"created_date", "last_modified_date"});

        // migrate deterlab_projects table: created_date and last_modified_date
        migrate(jdbcTemplate, "deterlab_projects", new String[]{"created_date", "last_modified_date"});

        // migrate deterlab_users table: created_date and last_modified_date
        migrate(jdbcTemplate, "deterlab_users", new String[]{"created_date", "last_modified_date"});

        // migrate email_retries table: created_date, last_modified_date, and last_retry_time
        migrate(jdbcTemplate, "email_retries", new String[]{"created_date", "last_modified_date", "last_retry_time"});

        // TODO: migrate experiments table: ?
        // TODO: migrate login_activities table: ?
        // TODO: migrate realizations table: ?
        // TODO: migrate registrations table: ?
        // TODO: migrate team_members table: ?
        // TODO: migrate teams table: ?
        // TODO: migrate user_details table: ?
        // TODO: migrate users table: ?

    }

    private void migrate(final JdbcTemplate jdbcTemplate, final String table, final String[] columns) {
        for (String column : columns) {
            final String s1 = String.format("ALTER TABLE %s CHANGE COLUMN %s old_%s TINYBLOB", table, column, column);
            jdbcTemplate.update(s1);
            final String s2 = String.format("ALTER TABLE %s ADD COLUMN %s DATETIME NOT NULL", table, column);
            jdbcTemplate.update(s2);
            final String s3 = String.format("SELECT id, old_%s FROM %s", column, table);
            final List<Map<String, Object>> list = jdbcTemplate.queryForList(s3);
            list.forEach(m -> {
                final Object id = m.get("id");
                final ZonedDateTime date = deserialize((byte[]) m.get(String.format("old_%s", column)));
                final String s4 = String.format("UPDATE %s SET %s = ? WHERE id = ?", table, column);
                jdbcTemplate.update(s4, Timestamp.valueOf(date.toLocalDateTime()), id);
                log.info("Updated {} entry: id={}, {}={}", table, id, column, date);
                final String s5 = String.format("ALTER TABLE %s DROP COLUMN old_%s", table, column);
                jdbcTemplate.update(s5);
            });
        }
    }

    private ZonedDateTime deserialize(final byte[] bytes) {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try (final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            final Object object = objectInputStream.readObject();
            return ZonedDateTime.class.cast(object);
        } catch (IOException | ClassNotFoundException e) {
            log.error("{}", e);
            throw new IllegalArgumentException(e);
        }
    }

}
