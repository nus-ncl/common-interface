package db.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by dcszwang on 11/4/2016.
 */
@Slf4j
public class V1_4__change_content_format_to_text_email implements SpringJdbcMigration {

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
        migrate(jdbcTemplate, "email_retries", new String[]{"content"});
    }

    private void migrate(final JdbcTemplate jdbcTemplate, final String table, final String[] columns) {
        for (String column : columns) {
            final String s1 = String.format("ALTER TABLE prod.%s CHANGE COLUMN %s old_%s TINYBLOB", table, column, column);
            jdbcTemplate.update(s1);
            final String s2 = String.format("ALTER TABLE prod.%s ADD COLUMN %s LONGTEXT NOT NULL AFTER old_%s", table, column, column);
            jdbcTemplate.update(s2);
            final String s3 = String.format("SELECT id, old_%s FROM prod.%s", column, table);
            final List<Map<String, Object>> list = jdbcTemplate.queryForList(s3);
            list.forEach(map -> {
                final Object content = map.get(String.format("old_%s", column));
                final Object id = map.get("id");

                final String s4 = String.format("UPDATE prod.%s SET %s = ? WHERE id = ?", table, column);
                jdbcTemplate.update(s4, content, id);
                log.info("Updated {} entry: id={}, {}={}", table, id, column, content);
            });
            final String s5 = String.format("ALTER TABLE prod.%s DROP COLUMN old_%s", table, column);
            jdbcTemplate.update(s5);
        }
    }
}
