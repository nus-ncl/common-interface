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

    final static String schema = "prod";

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {

        // migrate address table: created_date and last_modified_date
        migrate(jdbcTemplate, schema, "addresses", new String[]{"created_date", "last_modified_date"});

        // migrate credentials table: created_date and last_modified_date
        migrate(jdbcTemplate, schema, "credentials", new String[]{"created_date", "last_modified_date"});

        // migrate deterlab_projects table: created_date and last_modified_date
        migrate(jdbcTemplate, schema, "deterlab_project", new String[]{"created_date", "last_modified_date"});

        // migrate deterlab_users table: created_date and last_modified_date
        migrate(jdbcTemplate, schema, "deterlab_user", new String[]{"created_date", "last_modified_date"});

        // migrate email_retries table: created_date, last_modified_date, and last_retry_time
        migrate(jdbcTemplate, schema, "email_retries", new String[]{"created_date", "last_modified_date", "last_retry_time"});

        // migrate experiments table: created_date, last_modified_date
        migrate(jdbcTemplate, schema, "experiments", new String[]{"created_date", "last_modified_date"});

        // migrate login_activities table: created_date, last_modified_date, date
        migrate(jdbcTemplate, schema, "login_activities", new String[]{"created_date", "last_modified_date", "date"});

        // migrate realizations table: created_date, last_modified_date
        migrate(jdbcTemplate, schema, "realizations", new String[]{"created_date", "last_modified_date"});

        // migrate registrations table: created_date, last_modified_date
        migrate(jdbcTemplate, schema, "registrations", new String[]{"created_date", "last_modified_date"});

        // migrate team_members table: created_date, last_modified_date, joined_date
        migrate(jdbcTemplate, schema, "team_members", new String[]{"created_date", "last_modified_date", "joined_date"});

        // migrate teams table: created_date, last_modified_date, application_date, processed_date
        migrate(jdbcTemplate, schema, "teams", new String[]{"created_date", "last_modified_date", "application_date", "processed_date"});

        // migrate user_details table: created_date, last_modified_date
        migrate(jdbcTemplate, schema, "user_details", new String[]{"created_date", "last_modified_date"});

        // migrate users table: created_date, last_modified_date, application_date, processed_date
        migrate(jdbcTemplate, schema, "users", new String[]{"created_date", "last_modified_date", "application_date", "processed_date"});

    }

    private void migrate(final JdbcTemplate jdbcTemplate, final String schema, final String table, final String[] columns) {
        for (String column : columns) {
            final String s1 = String.format("ALTER TABLE %s.%s CHANGE COLUMN %s old_%s TINYBLOB", schema, table, column, column);
            jdbcTemplate.update(s1);
            String s2 = String.format("ALTER TABLE %s.%s ADD COLUMN %s DATETIME%s", schema, table, column, column == "processed_date" ? "" : " NOT NULL");
            jdbcTemplate.update(s2);
            final String s3 = String.format("SELECT id, old_%s FROM %s.%s", column, schema, table);
            final List<Map<String, Object>> list = jdbcTemplate.queryForList(s3);
            list.forEach(m -> {
                final Object id = m.get("id");
                final Object d = m.get(String.format("old_%s", column));
                if(null != d) {
                    final ZonedDateTime date = deserialize((byte[]) d);
                    final String s4 = String.format("UPDATE %s.%s SET %s = ? WHERE id = ?", schema, table, column);
                    jdbcTemplate.update(s4, Timestamp.valueOf(date.toLocalDateTime()), id);
                    log.info("Updated {} entry: id={}, {}={}", table, id, column, date);
                }
            });
            final String s5 = String.format("ALTER TABLE %s.%s DROP COLUMN old_%s", schema, table, column);
            jdbcTemplate.update(s5);
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
