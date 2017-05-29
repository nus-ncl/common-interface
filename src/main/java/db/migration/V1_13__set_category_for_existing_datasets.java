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
 * Created by dcsjnh on 11/5/2017.
 */
@Slf4j
public class V1_13__set_category_for_existing_datasets implements SpringJdbcMigration {

    private static final String SQL_INSERT_CATEGORY =
                    "INSERT INTO prod.data_categories " +
                    "(created_date, last_modified_date, version, name, description) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private int createCategory(final JdbcTemplate jdbcTemplate, final String name, final String description) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final Timestamp now = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());
        final int version = 0;
        final int i = jdbcTemplate.update(c -> {
            final PreparedStatement statement = c.prepareStatement(SQL_INSERT_CATEGORY, new String[]{"id"});
            statement.setObject(1, now);
            statement.setObject(2, now);
            statement.setInt(3, version);
            statement.setString(4, name);
            statement.setString(5, description);
            return statement;
        }, keyHolder);
        final int id = keyHolder.getKey().intValue();
        log.info("Inserted {} category entry: id={}, created_date={}, last_modified_date={}, version={}, name={}, description={}", i, id, now, now, version, name, description);
        return id;
    }

    private void updateData(final JdbcTemplate jdbcTemplate, final int category_id) {
        final String sql = "UPDATE prod.data SET category_id=?";
        final int i = jdbcTemplate.update(sql, category_id);
        log.info("Number of rows affected: {}", i);
    }

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
        log.info("Creating categories and getting id for category 'Other'.");
        createCategory(jdbcTemplate, "Network", "Data related to computer networks, such as BGP Routing Data, DNS Data, Traffic Flow data etc.");
        createCategory(jdbcTemplate, "System", "Data related to traditional computer systems, such as system logs, system vulnerabilities and exploits etc.");
        createCategory(jdbcTemplate, "Mobile", "Data related to mobile system and network, such as malicious mobile apps, 2G/3G/4G network data etc.");
        createCategory(jdbcTemplate, "Web", "Web data such as web hacking incidents data, Alexa top 1M websites, malicious/phishing websites etc.");
        createCategory(jdbcTemplate, "CPS", "Data related to any particular Cyber-Physical Systems such as power grid, urban transport system etc.");
        createCategory(jdbcTemplate, "Malware", "Computer malware, virus, worm and trojans etc.");
        createCategory(jdbcTemplate, "Cloud", "Data related to cloud computing and server virtualization technologies.");
        createCategory(jdbcTemplate, "Personal", "Data related to personal privacy such as healthcare data, transaction records etc.");
        createCategory(jdbcTemplate, "Threat Feeds", "Data feeds related to cyber threat such as VXvault, Tracker, Malware Domain List, MX Phishing DB etc.");
        int category_id = createCategory(jdbcTemplate, "Other", "Data that cannot be clearly classified into the existing categories, e.g., password frequency dataset.");

        log.info("Setting id for category 'Other' into existing datasets.");
        updateData(jdbcTemplate, category_id);
    }

}
