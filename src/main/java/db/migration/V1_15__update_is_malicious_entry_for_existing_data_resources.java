package db.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * populates is_malicious column for existing data resources by setting to 'N'
 * is_malicious column is NULL at this stage after v1_14
 * Created by teye
 */
@Slf4j
public class V1_15__update_is_malicious_entry_for_existing_data_resources implements SpringJdbcMigration {

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
        migrate(jdbcTemplate, "data_resources", "is_malicious");
    }

    private void migrate(final JdbcTemplate jdbcTemplate, final String table, final String column) {
        final String s1 = String.format("SELECT id, %s FROM prod.%s", column, table);
        final List<Map<String, Object>> list = jdbcTemplate.queryForList(s1);
        list.forEach(map -> {
            final Object content = map.get(String.format("is_malicious", column));
            final Object id = map.get("id");

            // only update columns that are null
            // not needed but just in case
            log.info("Content: {}", content);
            if(content == null || content.equals("")) {
                String isMalicious = "N";
                final String s2 = String.format("UPDATE prod.%s SET %s = ? WHERE id = ?", table, column);
                jdbcTemplate.update(s2, isMalicious, id);
                log.info("Updated {} entry: id={}, {}={}", table, id, column, isMalicious);
            }
        });
    }
}
