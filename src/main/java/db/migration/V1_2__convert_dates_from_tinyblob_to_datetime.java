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

    private static final String SCHEMA = "prod";

    private static final String CREATED_DATE = "created_date";
    private static final String LAST_MODIFIED_DATE = "last_modified_date";
    private static final String LAST_RETRY_TIME = "last_retry_time";
    private static final String DATE = "date";
    private static final String JOINED_DATE = "joined_date";
    private static final String APPLICATION_DATE = "application_date";
    private static final String PROCESSED_DATE = "processed_date";

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {

        // migrate address table: created_date and last_modified_date
        migrate(jdbcTemplate, "addresses", new String[]{CREATED_DATE, LAST_MODIFIED_DATE});

        // migrate credentials table: created_date and last_modified_date
        migrate(jdbcTemplate, "credentials", new String[]{CREATED_DATE, LAST_MODIFIED_DATE});

        // migrate deterlab_projects table: created_date and last_modified_date
        migrate(jdbcTemplate, "deterlab_project", new String[]{CREATED_DATE, LAST_MODIFIED_DATE});

        // migrate deterlab_users table: created_date and last_modified_date
        migrate(jdbcTemplate, "deterlab_user", new String[]{CREATED_DATE, LAST_MODIFIED_DATE});

        // migrate email_retries table: created_date, last_modified_date, and last_retry_time
        migrate(jdbcTemplate, "email_retries", new String[]{CREATED_DATE, LAST_MODIFIED_DATE, LAST_RETRY_TIME});

        // migrate experiments table: created_date, last_modified_date
        migrate(jdbcTemplate, "experiments", new String[]{CREATED_DATE, LAST_MODIFIED_DATE});

        // migrate login_activities table: created_date, last_modified_date, date
        migrate(jdbcTemplate, "login_activities", new String[]{CREATED_DATE, LAST_MODIFIED_DATE, DATE});

        // migrate realizations table: created_date, last_modified_date
        migrate(jdbcTemplate, "realizations", new String[]{CREATED_DATE, LAST_MODIFIED_DATE});

        // migrate registrations table: created_date, last_modified_date
        migrate(jdbcTemplate, "registrations", new String[]{CREATED_DATE, LAST_MODIFIED_DATE});

        // migrate team_members table: created_date, last_modified_date, joined_date
        migrate(jdbcTemplate, "team_members", new String[]{CREATED_DATE, LAST_MODIFIED_DATE, JOINED_DATE});

        // migrate teams table: created_date, last_modified_date, application_date, processed_date
        migrate(jdbcTemplate, "teams", new String[]{CREATED_DATE, LAST_MODIFIED_DATE, APPLICATION_DATE, PROCESSED_DATE});

        // migrate user_details table: created_date, last_modified_date
        migrate(jdbcTemplate, "user_details", new String[]{CREATED_DATE, LAST_MODIFIED_DATE});

        // migrate users table: created_date, last_modified_date, application_date, processed_date
        migrate(jdbcTemplate, "users", new String[]{CREATED_DATE, LAST_MODIFIED_DATE, APPLICATION_DATE, PROCESSED_DATE});
    }

    private void migrate(final JdbcTemplate jdbcTemplate, final String table, final String[] columns) {
        for (String column : columns) {
            final String s1 = String.format("ALTER TABLE %s.%s CHANGE COLUMN %s old_%s TINYBLOB", SCHEMA, table, column, column);
            jdbcTemplate.update(s1);
            final String s2 = String.format("ALTER TABLE %s.%s ADD COLUMN %s DATETIME %s", SCHEMA, table, column, getType(column));
            jdbcTemplate.update(s2);
            final String s3 = String.format("SELECT id, old_%s FROM %s.%s", column, SCHEMA, table);
            final List<Map<String, Object>> list = jdbcTemplate.queryForList(s3);
            list.forEach(map -> {
                final Object o = map.get(String.format("old_%s", column));
                if (o != null) {
                    final Object id = map.get("id");
                    final ZonedDateTime date = deserialize((byte[]) o);
                    final String s4 = String.format("UPDATE %s.%s SET %s = ? WHERE id = ?", SCHEMA, table, column);
                    jdbcTemplate.update(s4, Timestamp.valueOf(date.toLocalDateTime()), id);
                    log.info("Updated {} entry: id={}, {}={}", table, id, column, date);
                }
            });
            final String s5 = String.format("ALTER TABLE %s.%s DROP COLUMN old_%s", SCHEMA, table, column);
            jdbcTemplate.update(s5);
        }
    }

    private String getType(final String column) {return column.equals(PROCESSED_DATE) ? "NULL DEFAULT NULL" : "NOT NULL";}

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
