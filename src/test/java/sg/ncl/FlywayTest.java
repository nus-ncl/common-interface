package sg.ncl;

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcszwang on 10/13/2016.
 */

public class FlywayTest {

    private static final String ADDRESSES = "addresses";
    private static final String CREDENTIALS = "credentials";
    private static final String CREDENTIALS_ROLES = "credentials_roles";
    private static final String DATA = "data";
    private static final String DATA_ACCESS_REQUESTS = "data_access_requests";
    private static final String DATA_CATEGORIES = "data_categories";
    private static final String DATA_DOWNLOADS = "data_downloads";
    private static final String DATA_KEYWORDS = "data_keywords";
    private static final String DATA_LICENSES = "data_licenses";
    private static final String DATA_PUBLIC_DOWNLOADS = "data_public_downloads";
    private static final String DATA_PUBLIC_USERS = "data_public_users";
    private static final String DATA_RESOURCES = "data_resources";
    private static final String DATA_USERS = "data_users";
    private static final String DETERLAB_PROJECT = "deterlab_project";
    private static final String DETERLAB_USER = "deterlab_user";
    private static final String EMAIL_RETRIES = "email_retries";
    private static final String EXPERIMENTS = "experiments";
    private static final String IMAGES = "images";
    private static final String LOGIN_ACTIVITIES = "login_activities";
    private static final String PASSWORD_RESET_REQUESTS = "password_reset_requests";
    private static final String REALIZATIONS = "realizations";
    private static final String REGISTRATIONS = "registrations";
    private static final String TEAM_MEMBERS = "team_members";
    private static final String TEAM_QUOTAS = "team_quotas";
    private static final String TEAMS = "teams";
    private static final String USER_DETAILS = "user_details";
    private static final String USERS = "users";
    private static final String USERS_TEAMS = "users_teams";
    private static final String SCHEMA = "prod";

    private DataSource dataSource;
    private JdbcTemplate template;
    private Flyway flyway;
    private List<String> tables;

    @Before
    public void setup() throws SQLException {
        this.dataSource = createDataSource();
        this.template = new JdbcTemplate(dataSource);
        this.flyway = new Flyway();
        this.flyway.setDataSource(dataSource);
        this.flyway.migrate();

        this.tables = new ArrayList<>();
        this.tables.add(ADDRESSES);
        this.tables.add(CREDENTIALS);
        this.tables.add(CREDENTIALS_ROLES);
        this.tables.add(DATA);
        this.tables.add(DATA_ACCESS_REQUESTS);
        this.tables.add(DATA_CATEGORIES);
        this.tables.add(DATA_DOWNLOADS);
        this.tables.add(DATA_KEYWORDS);
        this.tables.add(DATA_LICENSES);
        this.tables.add(DATA_PUBLIC_DOWNLOADS);
        this.tables.add(DATA_PUBLIC_USERS);
        this.tables.add(DATA_RESOURCES);
        this.tables.add(DATA_USERS);
        this.tables.add(DETERLAB_PROJECT);
        this.tables.add(DETERLAB_USER);
        this.tables.add(EMAIL_RETRIES);
        this.tables.add(EXPERIMENTS);
        this.tables.add(IMAGES);
        this.tables.add(LOGIN_ACTIVITIES);
        this.tables.add(PASSWORD_RESET_REQUESTS);
        this.tables.add(REALIZATIONS);
        this.tables.add(REGISTRATIONS);
        this.tables.add(TEAM_MEMBERS);
        this.tables.add(TEAM_QUOTAS);
        this.tables.add(TEAMS);
        this.tables.add(USER_DETAILS);
        this.tables.add(USERS);
        this.tables.add(USERS_TEAMS);
    }

    @Test
    public void testNumOfTables() throws Exception {
        // make sure that we have the exact number of tables
        List<Map<String, Object>> tableNames = this.template.queryForList("SHOW TABLES FROM " + SCHEMA);
        assertThat(tableNames.size()).isEqualTo(this.tables.size());
    }

    @Test
    public void testTableNames() throws Exception {
        // make sure all tables name matches
        List<Map<String, Object>> tableNames = this.template.queryForList("SHOW TABLES FROM " + SCHEMA);
        for (int i = 0; i < this.tables.size(); i++) {
            assertThat((String) tableNames.get(i).get("TABLE_NAME")).isEqualTo(tables.get(i));
            assertThat((String) tableNames.get(i).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        }
    }

    @Test
    public void testAddressesTable() throws Exception {
        // make sure 'addresses' table has expected number of columns
        List<Map<String, Object>> addressesTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + ADDRESSES);
        assertThat(addressesTable.size()).isEqualTo(10);

        // make sure 'addresses' table has expected column name and type
        assertThat((String) addressesTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) addressesTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) addressesTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) addressesTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) addressesTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) addressesTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(1).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) addressesTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) addressesTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(2).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) addressesTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) addressesTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(3).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(4).get("FIELD")).isEqualTo("address_1");
        assertThat((String) addressesTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) addressesTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(4).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(5).get("FIELD")).isEqualTo("address_2");
        assertThat((String) addressesTable.get(5).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) addressesTable.get(5).get("NULL")).isEqualTo("YES");
        assertThat((String) addressesTable.get(5).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(6).get("FIELD")).isEqualTo("city");
        assertThat((String) addressesTable.get(6).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) addressesTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(6).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(7).get("FIELD")).isEqualTo("country");
        assertThat((String) addressesTable.get(7).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) addressesTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(7).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(8).get("FIELD")).isEqualTo("region");
        assertThat((String) addressesTable.get(8).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) addressesTable.get(8).get("NULL")).isEqualTo("YES");
        assertThat((String) addressesTable.get(8).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(9).get("FIELD")).isEqualTo("zip_code");
        assertThat((String) addressesTable.get(9).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) addressesTable.get(9).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(9).get("KEY")).isEmpty();


        List<Map<String, Object>> addressesConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'addresses'");

        assertThat((String) addressesConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) addressesConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
    }

        @Test
        public void testCredentialsTable() throws Exception {
            // make sure 'credentials' table has expected number of columns
            List<Map<String, Object>> credentialsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + CREDENTIALS);
            assertThat(credentialsTable.size()).isEqualTo(7);

            // make sure 'credentials' table has expected column name and type
            assertThat((String) credentialsTable.get(0).get("FIELD")).isEqualTo("id");
            assertThat((String) credentialsTable.get(0).get("TYPE")).isEqualTo("varchar(255)");
            assertThat((String) credentialsTable.get(0).get("NULL")).isEqualTo("NO");
            assertThat((String) credentialsTable.get(0).get("KEY")).isEqualTo("PRI");

            assertThat((String) credentialsTable.get(1).get("FIELD")).isEqualTo("created_date");
            assertThat((String) credentialsTable.get(1).get("TYPE")).contains("timestamp");
            assertThat((String) credentialsTable.get(1).get("NULL")).isEqualTo("NO");
            assertThat((String) credentialsTable.get(1).get("KEY")).isEmpty();

            assertThat((String) credentialsTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
            assertThat((String) credentialsTable.get(2).get("TYPE")).contains("timestamp");
            assertThat((String) credentialsTable.get(2).get("NULL")).isEqualTo("NO");
            assertThat((String) credentialsTable.get(2).get("KEY")).isEmpty();

            assertThat((String) credentialsTable.get(3).get("FIELD")).isEqualTo("version");
            assertThat((String) credentialsTable.get(3).get("TYPE")).contains("bigint");
            assertThat((String) credentialsTable.get(3).get("NULL")).isEqualTo("NO");
            assertThat((String) credentialsTable.get(3).get("KEY")).isEmpty();

            assertThat((String) credentialsTable.get(4).get("FIELD")).isEqualTo("password");
            assertThat((String) credentialsTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
            assertThat((String) credentialsTable.get(4).get("NULL")).isEqualTo("NO");
            assertThat((String) credentialsTable.get(4).get("KEY")).isEmpty();

            assertThat((String) credentialsTable.get(5).get("FIELD")).isEqualTo("status");
            assertThat((String) credentialsTable.get(5).get("TYPE")).isEqualTo("varchar(255)");
            assertThat((String) credentialsTable.get(5).get("NULL")).isEqualTo("NO");
            assertThat((String) credentialsTable.get(5).get("KEY")).isEmpty();

            assertThat((String) credentialsTable.get(6).get("FIELD")).isEqualTo("username");
            assertThat((String) credentialsTable.get(6).get("TYPE")).isEqualTo("varchar(255)");
            assertThat((String) credentialsTable.get(6).get("NULL")).isEqualTo("NO");
            assertThat((String) credentialsTable.get(6).get("KEY")).isEqualTo("UNI");


            List<Map<String, Object>> credentialsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'credentials'");

            assertThat((String) credentialsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
            assertThat((String) credentialsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
            assertThat((String) credentialsConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
            assertThat((String) credentialsConstraints.get(1).get("COLUMN_LIST")).isEqualTo("USERNAME");
        }

    @Test
    public void testCredentialsRolesTable() throws Exception {
        // make sure 'credentials_roles' table has expected number of columns
        List<Map<String, Object>> credentialsRolesTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + CREDENTIALS_ROLES);
        assertThat(credentialsRolesTable.size()).isEqualTo(2);

        // make sure 'credentials_roles' table has expected column name and type
        assertThat((String) credentialsRolesTable.get(0).get("FIELD")).isEqualTo("credentials_id");
        assertThat((String) credentialsRolesTable.get(0).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) credentialsRolesTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) credentialsRolesTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) credentialsRolesTable.get(1).get("FIELD")).isEqualTo("role");
        assertThat((String) credentialsRolesTable.get(1).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) credentialsRolesTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) credentialsRolesTable.get(1).get("KEY")).isEqualTo("PRI");

        List<Map<String, Object>> credentialsRolesConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'credentials_roles'");
        assertThat((String) credentialsRolesConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("REFERENTIAL");
        assertThat((String) credentialsRolesConstraints.get(0).get("COLUMN_LIST")).isEqualTo("CREDENTIALS_ID");
        assertThat((String) credentialsRolesConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) credentialsRolesConstraints.get(1).get("COLUMN_LIST")).isEqualTo("CREDENTIALS_ID,ROLE");
    }

    @Test
    public void testDataTable() throws Exception {
        // make sure 'data' table has expected number of columns
        List<Map<String, Object>> dataTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + DATA);
        assertThat(dataTable.size()).isEqualTo(12);

        // make sure 'data' table has expected column name and type
        assertThat((String) dataTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) dataTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) dataTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) dataTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) dataTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) dataTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) dataTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) dataTable.get(1).get("KEY")).isEmpty();

        assertThat((String) dataTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) dataTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) dataTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) dataTable.get(2).get("KEY")).isEmpty();

        assertThat((String) dataTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) dataTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) dataTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) dataTable.get(3).get("KEY")).isEmpty();

        assertThat((String) dataTable.get(4).get("FIELD")).isEqualTo("name");
        assertThat((String) dataTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) dataTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) dataTable.get(4).get("KEY")).isEqualTo("UNI");

        assertThat((String) dataTable.get(5).get("FIELD")).isEqualTo("description");
        assertThat((String) dataTable.get(5).get("TYPE")).contains("clob");
        assertThat((String) dataTable.get(5).get("NULL")).isEqualTo("YES");
        assertThat((String) dataTable.get(5).get("KEY")).isEmpty();

        assertThat((String) dataTable.get(6).get("FIELD")).isEqualTo("accessibility");
        assertThat((String) dataTable.get(6).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) dataTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) dataTable.get(6).get("KEY")).isEmpty();

        assertThat((String) dataTable.get(7).get("FIELD")).isEqualTo("visibility");
        assertThat((String) dataTable.get(7).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) dataTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) dataTable.get(7).get("KEY")).isEmpty();

        assertThat((String) dataTable.get(8).get("FIELD")).isEqualTo("contributor_id");
        assertThat((String) dataTable.get(8).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) dataTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) dataTable.get(8).get("KEY")).isEmpty();

        assertThat((String) dataTable.get(9).get("FIELD")).isEqualTo("category_id");
        assertThat((String) dataTable.get(9).get("TYPE")).contains("bigint");
        assertThat((String) dataTable.get(9).get("NULL")).isEqualTo("NO");
        assertThat((String) dataTable.get(9).get("KEY")).isEmpty();

        assertThat((String) dataTable.get(10).get("FIELD")).isEqualTo("license_id");
        assertThat((String) dataTable.get(10).get("TYPE")).contains("bigint");
        assertThat((String) dataTable.get(10).get("NULL")).isEqualTo("NO");
        assertThat((String) dataTable.get(10).get("KEY")).isEmpty();

        assertThat((String) dataTable.get(11).get("FIELD")).isEqualTo("released_date");
        assertThat((String) dataTable.get(11).get("TYPE")).contains("timestamp");
        assertThat((String) dataTable.get(11).get("NULL")).isEqualTo("NO");
        assertThat((String) dataTable.get(11).get("KEY")).isEmpty();

        List<Map<String, Object>> dataConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'data'");

        assertThat((String) dataConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) dataConstraints.get(1).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) dataConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) dataConstraints.get(0).get("COLUMN_LIST")).isEqualTo("NAME");
    }

    @Test
    public void testDataAccessRequestsTable() throws Exception {
        // make sure 'data_access_requests' table has expected number of columns
        List<Map<String, Object>> dataAccessRequestsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + DATA_ACCESS_REQUESTS);
        assertThat(dataAccessRequestsTable.size()).isEqualTo(9);

        // make sure 'data_access_requests' table has expected column name and type
        assertThat((String) dataAccessRequestsTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) dataAccessRequestsTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) dataAccessRequestsTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) dataAccessRequestsTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) dataAccessRequestsTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) dataAccessRequestsTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) dataAccessRequestsTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) dataAccessRequestsTable.get(1).get("KEY")).isEmpty();

        assertThat((String) dataAccessRequestsTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) dataAccessRequestsTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) dataAccessRequestsTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) dataAccessRequestsTable.get(2).get("KEY")).isEmpty();

        assertThat((String) dataAccessRequestsTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) dataAccessRequestsTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) dataAccessRequestsTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) dataAccessRequestsTable.get(3).get("KEY")).isEmpty();

        assertThat((String) dataAccessRequestsTable.get(4).get("FIELD")).isEqualTo("data_id");
        assertThat((String) dataAccessRequestsTable.get(4).get("TYPE")).contains("bigint");
        assertThat((String) dataAccessRequestsTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) dataAccessRequestsTable.get(4).get("KEY")).isEmpty();

        assertThat((String) dataAccessRequestsTable.get(5).get("FIELD")).isEqualTo("requester_id");
        assertThat((String) dataAccessRequestsTable.get(5).get("TYPE")).contains("varchar(255)");
        assertThat((String) dataAccessRequestsTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) dataAccessRequestsTable.get(5).get("KEY")).isEmpty();

        assertThat((String) dataAccessRequestsTable.get(6).get("FIELD")).isEqualTo("reason");
        assertThat((String) dataAccessRequestsTable.get(6).get("TYPE")).contains("clob");
        assertThat((String) dataAccessRequestsTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) dataAccessRequestsTable.get(6).get("KEY")).isEmpty();

        assertThat((String) dataAccessRequestsTable.get(7).get("FIELD")).isEqualTo("request_date");
        assertThat((String) dataAccessRequestsTable.get(7).get("TYPE")).contains("timestamp");
        assertThat((String) dataAccessRequestsTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) dataAccessRequestsTable.get(7).get("KEY")).isEmpty();

        assertThat((String) dataAccessRequestsTable.get(8).get("FIELD")).isEqualTo("approved_date");
        assertThat((String) dataAccessRequestsTable.get(8).get("TYPE")).contains("timestamp");
        assertThat((String) dataAccessRequestsTable.get(8).get("NULL")).isEqualTo("YES");
        assertThat((String) dataAccessRequestsTable.get(8).get("KEY")).isEmpty();

        List<Map<String, Object>> dataAccessRequestsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'data_access_requests'");

        assertThat((String) dataAccessRequestsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) dataAccessRequestsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
    }

    @Test
    public void testDataCategoriesTable() throws Exception {
        // make sure 'data_categories' table has expected number of columns
        List<Map<String, Object>> dataCategoriesTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + DATA_CATEGORIES);
        assertThat(dataCategoriesTable.size()).isEqualTo(6);

        // make sure 'data_categories' table has expected column name and type
        assertThat((String) dataCategoriesTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) dataCategoriesTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) dataCategoriesTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) dataCategoriesTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) dataCategoriesTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) dataCategoriesTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) dataCategoriesTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) dataCategoriesTable.get(1).get("KEY")).isEmpty();

        assertThat((String) dataCategoriesTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) dataCategoriesTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) dataCategoriesTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) dataCategoriesTable.get(2).get("KEY")).isEmpty();

        assertThat((String) dataCategoriesTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) dataCategoriesTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) dataCategoriesTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) dataCategoriesTable.get(3).get("KEY")).isEmpty();

        assertThat((String) dataCategoriesTable.get(4).get("FIELD")).isEqualTo("name");
        assertThat((String) dataCategoriesTable.get(4).get("TYPE")).contains("varchar(255)");
        assertThat((String) dataCategoriesTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) dataCategoriesTable.get(4).get("KEY")).isEmpty();

        assertThat((String) dataCategoriesTable.get(5).get("FIELD")).isEqualTo("description");
        assertThat((String) dataCategoriesTable.get(5).get("TYPE")).contains("clob");
        assertThat((String) dataCategoriesTable.get(5).get("NULL")).isEqualTo("YES");
        assertThat((String) dataCategoriesTable.get(5).get("KEY")).isEmpty();

        List<Map<String, Object>> dataCategoriesConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'data_categories'");

        assertThat((String) dataCategoriesConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) dataCategoriesConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
    }

    @Test
    public void testDateKeywordsTable() throws Exception {
        // make sure 'data_keywords' table has expected number of columns
        List<Map<String, Object>> dataKeywordsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + DATA_KEYWORDS);
        assertThat(dataKeywordsTable.size()).isEqualTo(2);

        // make sure 'data_keywords' table has expected column name and type
        assertThat((String) dataKeywordsTable.get(0).get("FIELD")).isEqualTo("data_id");
        assertThat((String) dataKeywordsTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) dataKeywordsTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) dataKeywordsTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) dataKeywordsTable.get(1).get("FIELD")).isEqualTo("keyword");
        assertThat((String) dataKeywordsTable.get(1).get("TYPE")).contains("varchar(255)");
        assertThat((String) dataKeywordsTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) dataKeywordsTable.get(1).get("KEY")).isEqualTo("PRI");

        List<Map<String, Object>> dataKeywordsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'data_keywords'");

        assertThat((String) dataKeywordsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("REFERENTIAL");
        assertThat((String) dataKeywordsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("DATA_ID");
        assertThat((String) dataKeywordsConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) dataKeywordsConstraints.get(1).get("COLUMN_LIST")).isEqualTo("DATA_ID,KEYWORD");
    }

    @Test
    public void testDataResourcesTable() throws Exception {
        // make sure 'data_resources' table has expected number of columns
        List<Map<String, Object>> dataResourcesTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + DATA_RESOURCES);
        assertThat(dataResourcesTable.size()).isEqualTo(8);

        // make sure 'data_resources' table has expected column name and type
        assertThat((String) dataResourcesTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) dataResourcesTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) dataResourcesTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) dataResourcesTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) dataResourcesTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) dataResourcesTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) dataResourcesTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) dataResourcesTable.get(1).get("KEY")).isEmpty();

        assertThat((String) dataResourcesTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) dataResourcesTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) dataResourcesTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) dataResourcesTable.get(2).get("KEY")).isEmpty();

        assertThat((String) dataResourcesTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) dataResourcesTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) dataResourcesTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) dataResourcesTable.get(3).get("KEY")).isEmpty();

        assertThat((String) dataResourcesTable.get(4).get("FIELD")).isEqualTo("uri");
        assertThat((String) dataResourcesTable.get(4).get("TYPE")).contains("clob");
        assertThat((String) dataResourcesTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) dataResourcesTable.get(4).get("KEY")).isEmpty();

        assertThat((String) dataResourcesTable.get(5).get("FIELD")).isEqualTo("data_id");
        assertThat((String) dataResourcesTable.get(5).get("TYPE")).contains("bigint");
        assertThat((String) dataResourcesTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) dataResourcesTable.get(5).get("KEY")).isEmpty();

        assertThat((String) dataResourcesTable.get(6).get("FIELD")).isEqualTo("is_malicious");
        assertThat((String) dataResourcesTable.get(6).get("TYPE")).contains("char");
        assertThat((String) dataResourcesTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) dataResourcesTable.get(6).get("KEY")).isEmpty();

        assertThat((String) dataResourcesTable.get(7).get("FIELD")).isEqualTo("is_scanned");
        assertThat((String) dataResourcesTable.get(7).get("TYPE")).contains("char");
        assertThat((String) dataResourcesTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) dataResourcesTable.get(7).get("KEY")).isEmpty();

        List<Map<String, Object>> dataResourcesConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'data_resources'");

        assertThat((String) dataResourcesConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) dataResourcesConstraints.get(1).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) dataResourcesConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("REFERENTIAL");
        assertThat((String) dataResourcesConstraints.get(0).get("COLUMN_LIST")).isEqualTo("DATA_ID");
    }

    @Test
    public void testDataDownloadsTable() throws Exception {
        // make sure 'data_downloads' table has expected number of columns
        List<Map<String, Object>> dataDownloadsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + DATA_DOWNLOADS);
        assertThat(dataDownloadsTable.size()).isEqualTo(8);

        // make sure 'data_resources' table has expected column name and type
        assertThat((String) dataDownloadsTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) dataDownloadsTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) dataDownloadsTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) dataDownloadsTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) dataDownloadsTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) dataDownloadsTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) dataDownloadsTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) dataDownloadsTable.get(1).get("KEY")).isEmpty();

        assertThat((String) dataDownloadsTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) dataDownloadsTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) dataDownloadsTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) dataDownloadsTable.get(2).get("KEY")).isEmpty();

        assertThat((String) dataDownloadsTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) dataDownloadsTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) dataDownloadsTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) dataDownloadsTable.get(3).get("KEY")).isEmpty();

        assertThat((String) dataDownloadsTable.get(4).get("FIELD")).isEqualTo("data_id");
        assertThat((String) dataDownloadsTable.get(4).get("TYPE")).contains("bigint");
        assertThat((String) dataDownloadsTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) dataDownloadsTable.get(4).get("KEY")).isEmpty();

        assertThat((String) dataDownloadsTable.get(5).get("FIELD")).isEqualTo("resource_id");
        assertThat((String) dataDownloadsTable.get(5).get("TYPE")).contains("bigint");
        assertThat((String) dataDownloadsTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) dataDownloadsTable.get(5).get("KEY")).isEmpty();

        assertThat((String) dataDownloadsTable.get(6).get("FIELD")).isEqualTo("download_date");
        assertThat((String) dataDownloadsTable.get(6).get("TYPE")).contains("timestamp");
        assertThat((String) dataDownloadsTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) dataDownloadsTable.get(6).get("KEY")).isEmpty();

        assertThat((String) dataDownloadsTable.get(7).get("FIELD")).isEqualTo("hashed_user_id");
        assertThat((String) dataDownloadsTable.get(7).get("TYPE")).contains("varchar(255)");
        assertThat((String) dataDownloadsTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) dataDownloadsTable.get(7).get("KEY")).isEmpty();

        List<Map<String, Object>> dataDownloadsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'data_downloads'");

        assertThat((String) dataDownloadsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) dataDownloadsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
    }

    @Test
    public void testDataUsersTable() throws Exception {
        // make sure 'data_users' table has expected number of columns
        List<Map<String, Object>> dataUsersTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + DATA_USERS);
        assertThat(dataUsersTable.size()).isEqualTo(2);

        // make sure 'data_users' table has expected column name and type
        assertThat((String) dataUsersTable.get(0).get("FIELD")).isEqualTo("data_id");
        assertThat((String) dataUsersTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) dataUsersTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) dataUsersTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) dataUsersTable.get(1).get("FIELD")).isEqualTo("user_id");
        assertThat((String) dataUsersTable.get(1).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) dataUsersTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) dataUsersTable.get(1).get("KEY")).isEqualTo("PRI");

        List<Map<String, Object>> dataUsersConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'data_users'");
        assertThat((String) dataUsersConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("REFERENTIAL");
        assertThat((String) dataUsersConstraints.get(0).get("COLUMN_LIST")).isEqualTo("DATA_ID");
        assertThat((String) dataUsersConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) dataUsersConstraints.get(1).get("COLUMN_LIST")).isEqualTo("DATA_ID,USER_ID");
    }

    @Test
    public void testDeterlabProjectTable() throws Exception {
        // make sure 'deterlab_project' table has expected number of columns
        List<Map<String, Object>> deterlabProjectTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + DETERLAB_PROJECT);
        assertThat(deterlabProjectTable.size()).isEqualTo(6);

        // make sure 'deterlab_project' table has expected column name and type
        assertThat((String) deterlabProjectTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) deterlabProjectTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) deterlabProjectTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabProjectTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) deterlabProjectTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) deterlabProjectTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) deterlabProjectTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabProjectTable.get(1).get("KEY")).isEmpty();

        assertThat((String) deterlabProjectTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) deterlabProjectTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) deterlabProjectTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabProjectTable.get(2).get("KEY")).isEmpty();

        assertThat((String) deterlabProjectTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) deterlabProjectTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) deterlabProjectTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabProjectTable.get(3).get("KEY")).isEmpty();

        assertThat((String) deterlabProjectTable.get(4).get("FIELD")).isEqualTo("deter_project_id");
        assertThat((String) deterlabProjectTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) deterlabProjectTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabProjectTable.get(4).get("KEY")).isEqualTo("UNI");

        assertThat((String) deterlabProjectTable.get(5).get("FIELD")).isEqualTo("ncl_team_id");
        assertThat((String) deterlabProjectTable.get(5).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) deterlabProjectTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabProjectTable.get(5).get("KEY")).isEqualTo("UNI");


        List<Map<String, Object>> deterlabProjectConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'deterlab_project'");

        assertThat((String) deterlabProjectConstraints.get(2).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) deterlabProjectConstraints.get(2).get("COLUMN_LIST")).isEqualTo("DETER_PROJECT_ID");
        assertThat((String) deterlabProjectConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) deterlabProjectConstraints.get(1).get("COLUMN_LIST")).isEqualTo("NCL_TEAM_ID");
        assertThat((String) deterlabProjectConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) deterlabProjectConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
    }

    @Test
    public void testDeterlabUserTable() throws Exception {
        // make sure 'deterlab_user' table has expected number of columns
        List<Map<String, Object>> deterlabUserTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + DETERLAB_USER);
        assertThat(deterlabUserTable.size()).isEqualTo(6);

        // make sure 'deterlab_user' table has expected column name and type
        assertThat((String) deterlabUserTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) deterlabUserTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) deterlabUserTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabUserTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) deterlabUserTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) deterlabUserTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) deterlabUserTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabUserTable.get(1).get("KEY")).isEmpty();

        assertThat((String) deterlabUserTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) deterlabUserTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) deterlabUserTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabUserTable.get(2).get("KEY")).isEmpty();

        assertThat((String) deterlabUserTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) deterlabUserTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) deterlabUserTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabUserTable.get(3).get("KEY")).isEmpty();

        assertThat((String) deterlabUserTable.get(4).get("FIELD")).isEqualTo("deter_user_id");
        assertThat((String) deterlabUserTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) deterlabUserTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabUserTable.get(4).get("KEY")).isEqualTo("UNI");

        assertThat((String) deterlabUserTable.get(5).get("FIELD")).isEqualTo("ncl_user_id");
        assertThat((String) deterlabUserTable.get(5).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) deterlabUserTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabUserTable.get(5).get("KEY")).isEqualTo("UNI");


        List<Map<String, Object>> deterlabUserConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'deterlab_user'");

        assertThat((String) deterlabUserConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) deterlabUserConstraints.get(1).get("COLUMN_LIST")).isEqualTo("DETER_USER_ID");
        assertThat((String) deterlabUserConstraints.get(2).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) deterlabUserConstraints.get(2).get("COLUMN_LIST")).isEqualTo("NCL_USER_ID");
        assertThat((String) deterlabUserConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) deterlabUserConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
    }

    @Test
    public void testEmailRetriesTable() throws Exception {
        // make sure 'email_retries' table has expected number of columns
        List<Map<String, Object>> emailRetriesTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + EMAIL_RETRIES);
        assertThat(emailRetriesTable.size()).isEqualTo(15);

        // make sure 'email_retries' table has expected column name and type
        assertThat((String) emailRetriesTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) emailRetriesTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) emailRetriesTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) emailRetriesTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) emailRetriesTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) emailRetriesTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(1).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) emailRetriesTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) emailRetriesTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(2).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) emailRetriesTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) emailRetriesTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(3).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(4).get("FIELD")).isEqualTo("bcc");
        assertThat((String) emailRetriesTable.get(4).get("TYPE")).contains("blob");
        assertThat((String) emailRetriesTable.get(4).get("NULL")).isEqualTo("YES");
        assertThat((String) emailRetriesTable.get(4).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(5).get("FIELD")).isEqualTo("cc");
        assertThat((String) emailRetriesTable.get(5).get("TYPE")).contains("blob");
        assertThat((String) emailRetriesTable.get(5).get("NULL")).isEqualTo("YES");
        assertThat((String) emailRetriesTable.get(5).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(6).get("FIELD")).isEqualTo("content");
        assertThat((String) emailRetriesTable.get(6).get("TYPE")).contains("clob");
        assertThat((String) emailRetriesTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(6).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(7).get("FIELD")).isEqualTo("error_message");
        assertThat((String) emailRetriesTable.get(7).get("TYPE")).contains("clob");
        assertThat((String) emailRetriesTable.get(7).get("NULL")).isEqualTo("YES");
        assertThat((String) emailRetriesTable.get(7).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(8).get("FIELD")).isEqualTo("html");
        assertThat((String) emailRetriesTable.get(8).get("TYPE")).isEqualTo("boolean(1)");
        assertThat((String) emailRetriesTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(8).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(9).get("FIELD")).isEqualTo("last_retry_time");
        assertThat((String) emailRetriesTable.get(9).get("TYPE")).contains("timestamp");
        assertThat((String) emailRetriesTable.get(9).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(9).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(10).get("FIELD")).isEqualTo("recipients");
        assertThat((String) emailRetriesTable.get(10).get("TYPE")).contains("blob");
        assertThat((String) emailRetriesTable.get(10).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(10).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(11).get("FIELD")).isEqualTo("retry_times");
        assertThat((String) emailRetriesTable.get(11).get("TYPE")).isEqualTo("integer(10)");
        assertThat((String) emailRetriesTable.get(11).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(11).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(12).get("FIELD")).isEqualTo("sender");
        assertThat((String) emailRetriesTable.get(12).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) emailRetriesTable.get(12).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(12).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(13).get("FIELD")).isEqualTo("sent");
        assertThat((String) emailRetriesTable.get(13).get("TYPE")).isEqualTo("boolean(1)");
        assertThat((String) emailRetriesTable.get(13).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(13).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(14).get("FIELD")).isEqualTo("subject");
        assertThat((String) emailRetriesTable.get(14).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) emailRetriesTable.get(14).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(14).get("KEY")).isEmpty();

        List<Map<String, Object>> emailRetriesConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'email_retries'");

        assertThat((String) emailRetriesConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) emailRetriesConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
    }

    @Test
    public void testExperimentsTable() throws Exception {
        // make sure 'experiments' table has expected number of columns
        List<Map<String, Object>> experimentsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + EXPERIMENTS);
        assertThat(experimentsTable.size()).isEqualTo(13);

        // make sure 'experiments' table has expected column name and type
        assertThat((String) experimentsTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) experimentsTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) experimentsTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) experimentsTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) experimentsTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) experimentsTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(1).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) experimentsTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) experimentsTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(2).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) experimentsTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) experimentsTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(3).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(4).get("FIELD")).isEqualTo("description");
        assertThat((String) experimentsTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) experimentsTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(4).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(5).get("FIELD")).isEqualTo("idle_swap");
        assertThat((String) experimentsTable.get(5).get("TYPE")).contains("integer");
        assertThat((String) experimentsTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(5).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(6).get("FIELD")).isEqualTo("max_duration");
        assertThat((String) experimentsTable.get(6).get("TYPE")).contains("integer");
        assertThat((String) experimentsTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(6).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(7).get("FIELD")).isEqualTo("name");
        assertThat((String) experimentsTable.get(7).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) experimentsTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(7).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(8).get("FIELD")).isEqualTo("ns_file");
        assertThat((String) experimentsTable.get(8).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) experimentsTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(8).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(9).get("FIELD")).isEqualTo("ns_file_content");
        assertThat((String) experimentsTable.get(9).get("TYPE")).contains("clob");
        assertThat((String) experimentsTable.get(9).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(9).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(10).get("FIELD")).isEqualTo("team_id");
        assertThat((String) experimentsTable.get(10).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) experimentsTable.get(10).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(10).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(11).get("FIELD")).isEqualTo("team_name");
        assertThat((String) experimentsTable.get(11).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) experimentsTable.get(11).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(11).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(12).get("FIELD")).isEqualTo("user_id");
        assertThat((String) experimentsTable.get(12).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) experimentsTable.get(12).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(12).get("KEY")).isEmpty();

        List<Map<String, Object>> experimentsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'experiments'");

        assertThat((String) experimentsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) experimentsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
    }

    @Test
    public void testLoginActivitiesTable() throws Exception {
        // make sure 'login_activities' table has expected number of columns
        List<Map<String, Object>> loginActivitiesTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + LOGIN_ACTIVITIES);
        assertThat(loginActivitiesTable.size()).isEqualTo(7);

        // make sure 'login_activities' table has expected column name and type
        assertThat((String) loginActivitiesTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) loginActivitiesTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) loginActivitiesTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) loginActivitiesTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) loginActivitiesTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) loginActivitiesTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) loginActivitiesTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) loginActivitiesTable.get(1).get("KEY")).isEmpty();

        assertThat((String) loginActivitiesTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) loginActivitiesTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) loginActivitiesTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) loginActivitiesTable.get(2).get("KEY")).isEmpty();

        assertThat((String) loginActivitiesTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) loginActivitiesTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) loginActivitiesTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) loginActivitiesTable.get(3).get("KEY")).isEmpty();

        assertThat((String) loginActivitiesTable.get(4).get("FIELD")).isEqualTo("date");
        assertThat((String) loginActivitiesTable.get(4).get("TYPE")).contains("timestamp");
        assertThat((String) loginActivitiesTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) loginActivitiesTable.get(4).get("KEY")).isEmpty();

        assertThat((String) loginActivitiesTable.get(5).get("FIELD")).isEqualTo("ip_address");
        assertThat((String) loginActivitiesTable.get(5).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) loginActivitiesTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) loginActivitiesTable.get(5).get("KEY")).isEmpty();

        assertThat((String) loginActivitiesTable.get(6).get("FIELD")).isEqualTo("user_id");
        assertThat((String) loginActivitiesTable.get(6).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) loginActivitiesTable.get(6).get("NULL")).isEqualTo("YES");
        assertThat((String) loginActivitiesTable.get(6).get("KEY")).isEmpty();

        List<Map<String, Object>> loginActivitiesConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'login_activities'");

        assertThat((String) loginActivitiesConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) loginActivitiesConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) loginActivitiesConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("REFERENTIAL");
        assertThat((String) loginActivitiesConstraints.get(1).get("COLUMN_LIST")).isEqualTo("USER_ID");
    }

    @Test
    public void testRealizationsTable() throws Exception {
        // make sure 'realizations' table has expected number of columns
        List<Map<String, Object>> realizationsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + REALIZATIONS);
        assertThat(realizationsTable.size()).isEqualTo(13);

        // make sure 'realizations' table has expected column name and type
        assertThat((String) realizationsTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) realizationsTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) realizationsTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) realizationsTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) realizationsTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) realizationsTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(1).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) realizationsTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) realizationsTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(2).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) realizationsTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) realizationsTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(3).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(4).get("FIELD")).isEqualTo("details");
        assertThat((String) realizationsTable.get(4).get("TYPE")).contains("clob");
        assertThat((String) realizationsTable.get(4).get("NULL")).isEqualTo("YES");
        assertThat((String) realizationsTable.get(4).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(5).get("FIELD")).isEqualTo("experiment_id");
        assertThat((String) realizationsTable.get(5).get("TYPE")).contains("bigint");
        assertThat((String) realizationsTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(5).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(6).get("FIELD")).isEqualTo("experiment_name");
        assertThat((String) realizationsTable.get(6).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) realizationsTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(6).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(7).get("FIELD")).isEqualTo("idle_minutes");
        assertThat((String) realizationsTable.get(7).get("TYPE")).contains("bigint");
        assertThat((String) realizationsTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(7).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(8).get("FIELD")).isEqualTo("num_nodes");
        assertThat((String) realizationsTable.get(8).get("TYPE")).contains("integer");
        assertThat((String) realizationsTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(8).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(9).get("FIELD")).isEqualTo("running_minutes");
        assertThat((String) realizationsTable.get(9).get("TYPE")).contains("bigint");
        assertThat((String) realizationsTable.get(9).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(9).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(10).get("FIELD")).isEqualTo("state");
        assertThat((String) realizationsTable.get(10).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) realizationsTable.get(10).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(10).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(11).get("FIELD")).isEqualTo("team_id");
        assertThat((String) realizationsTable.get(11).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) realizationsTable.get(11).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(11).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(12).get("FIELD")).isEqualTo("user_id");
        assertThat((String) realizationsTable.get(12).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) realizationsTable.get(12).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(12).get("KEY")).isEmpty();

        List<Map<String, Object>> realizationsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'realizations'");

        assertThat((String) realizationsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) realizationsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
    }

    @Test
    public void testRegistrationsTable() throws Exception {
        // make sure 'registrations' table has expected number of columns
        List<Map<String, Object>> registrationsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + REGISTRATIONS);
        assertThat(registrationsTable.size()).isEqualTo(18);

        // make sure 'registrations' table has expected column name and type
        assertThat((String) registrationsTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) registrationsTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) registrationsTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) registrationsTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) registrationsTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) registrationsTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(1).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) registrationsTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) registrationsTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(2).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) registrationsTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) registrationsTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(3).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(4).get("FIELD")).isEqualTo("pid");
        assertThat((String) registrationsTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(4).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(5).get("FIELD")).isEqualTo("uid");
        assertThat((String) registrationsTable.get(5).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(5).get("KEY")).isEqualTo("UNI");

        assertThat((String) registrationsTable.get(6).get("FIELD")).isEqualTo("usr_addr");
        assertThat((String) registrationsTable.get(6).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(6).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(7).get("FIELD")).isEqualTo("usr_addr2");
        assertThat((String) registrationsTable.get(7).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(7).get("NULL")).isEqualTo("YES");
        assertThat((String) registrationsTable.get(7).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(8).get("FIELD")).isEqualTo("usr_affil");
        assertThat((String) registrationsTable.get(8).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(8).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(9).get("FIELD")).isEqualTo("usr_affil_abbrev");
        assertThat((String) registrationsTable.get(9).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(9).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(9).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(10).get("FIELD")).isEqualTo("usr_city");
        assertThat((String) registrationsTable.get(10).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(10).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(10).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(11).get("FIELD")).isEqualTo("usr_country");
        assertThat((String) registrationsTable.get(11).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(11).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(11).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(12).get("FIELD")).isEqualTo("usr_email");
        assertThat((String) registrationsTable.get(12).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(12).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(12).get("KEY")).isEqualTo("UNI");

        assertThat((String) registrationsTable.get(13).get("FIELD")).isEqualTo("usr_name");
        assertThat((String) registrationsTable.get(13).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(13).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(13).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(14).get("FIELD")).isEqualTo("usr_phone");
        assertThat((String) registrationsTable.get(14).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(14).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(14).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(15).get("FIELD")).isEqualTo("usr_state");
        assertThat((String) registrationsTable.get(15).get("TYPE")).contains("varchar(255)");
        assertThat((String) registrationsTable.get(15).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(15).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(16).get("FIELD")).isEqualTo("usr_title");
        assertThat((String) registrationsTable.get(16).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(16).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(16).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(17).get("FIELD")).isEqualTo("usr_zip");
        assertThat((String) registrationsTable.get(17).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(17).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(17).get("KEY")).isEmpty();

        List<Map<String, Object>> registrationsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'registrations'");

        assertThat((String) registrationsConstraints.get(2).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) registrationsConstraints.get(2).get("COLUMN_LIST")).isEqualTo("USR_EMAIL");
        assertThat((String) registrationsConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) registrationsConstraints.get(1).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) registrationsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) registrationsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("UID");
    }

    @Test
    public void testTeamMembersTable() throws Exception {
        // make sure 'team_members' table has expected number of columns
        List<Map<String, Object>> teamMembersTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + TEAM_MEMBERS);
        assertThat(teamMembersTable.size()).isEqualTo(9);

        // make sure 'team_members' table has expected column name and type
        assertThat((String) teamMembersTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) teamMembersTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) teamMembersTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) teamMembersTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) teamMembersTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) teamMembersTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(1).get("KEY")).isEmpty();

        assertThat((String) teamMembersTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) teamMembersTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) teamMembersTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(2).get("KEY")).isEmpty();

        assertThat((String) teamMembersTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) teamMembersTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) teamMembersTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(3).get("KEY")).isEmpty();

        assertThat((String) teamMembersTable.get(4).get("FIELD")).isEqualTo("joined_date");
        assertThat((String) teamMembersTable.get(4).get("TYPE")).contains("timestamp");
        assertThat((String) teamMembersTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(4).get("KEY")).isEmpty();

        assertThat((String) teamMembersTable.get(5).get("FIELD")).isEqualTo("status");
        assertThat((String) teamMembersTable.get(5).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamMembersTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(5).get("KEY")).isEmpty();

        assertThat((String) teamMembersTable.get(6).get("FIELD")).isEqualTo("member_type");
        assertThat((String) teamMembersTable.get(6).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamMembersTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(6).get("KEY")).isEmpty();

        assertThat((String) teamMembersTable.get(7).get("FIELD")).isEqualTo("user_id");
        assertThat((String) teamMembersTable.get(7).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamMembersTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(7).get("KEY")).isEqualTo("UNI");

        assertThat((String) teamMembersTable.get(8).get("FIELD")).isEqualTo("team_id");
        assertThat((String) teamMembersTable.get(8).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamMembersTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(8).get("KEY")).isEqualTo("UNI");

        List<Map<String, Object>> teamMembersConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'team_members'");

        assertThat((String) teamMembersConstraints.get(2).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) teamMembersConstraints.get(2).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) teamMembersConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("REFERENTIAL");
        assertThat((String) teamMembersConstraints.get(1).get("COLUMN_LIST")).isEqualTo("TEAM_ID");
        assertThat((String) teamMembersConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) teamMembersConstraints.get(0).get("COLUMN_LIST")).isEqualTo("TEAM_ID,USER_ID");
    }

    @Test
    public void testTeamsTable() throws Exception {
        // make sure 'teams' table has expected number of columns
        List<Map<String, Object>> teamsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + TEAMS);
        assertThat(teamsTable.size()).isEqualTo(14);

        // make sure 'teams' table has expected column name and type
        assertThat((String) teamsTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) teamsTable.get(0).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamsTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) teamsTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) teamsTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) teamsTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) teamsTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) teamsTable.get(1).get("KEY")).isEmpty();

        assertThat((String) teamsTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) teamsTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) teamsTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) teamsTable.get(2).get("KEY")).isEmpty();

        assertThat((String) teamsTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) teamsTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) teamsTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) teamsTable.get(3).get("KEY")).isEmpty();

        assertThat((String) teamsTable.get(4).get("FIELD")).isEqualTo("application_date");
        assertThat((String) teamsTable.get(4).get("TYPE")).contains("timestamp");
        assertThat((String) teamsTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) teamsTable.get(4).get("KEY")).isEmpty();

        assertThat((String) teamsTable.get(5).get("FIELD")).isEqualTo("description");
        assertThat((String) teamsTable.get(5).get("TYPE")).contains("clob");
        assertThat((String) teamsTable.get(5).get("NULL")).isEqualTo("YES");
        assertThat((String) teamsTable.get(5).get("KEY")).isEmpty();

        assertThat((String) teamsTable.get(6).get("FIELD")).isEqualTo("name");
        assertThat((String) teamsTable.get(6).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamsTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) teamsTable.get(6).get("KEY")).isEqualTo("UNI");

        assertThat((String) teamsTable.get(7).get("FIELD")).isEqualTo("organisation_type");
        assertThat((String) teamsTable.get(7).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamsTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) teamsTable.get(7).get("KEY")).isEmpty();

        assertThat((String) teamsTable.get(8).get("FIELD")).isEqualTo("privacy");
        assertThat((String) teamsTable.get(8).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamsTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) teamsTable.get(8).get("KEY")).isEmpty();

        assertThat((String) teamsTable.get(9).get("FIELD")).isEqualTo("processed_date");
        assertThat((String) teamsTable.get(9).get("TYPE")).contains("timestamp");
        assertThat((String) teamsTable.get(9).get("NULL")).isEqualTo("YES");
        assertThat((String) teamsTable.get(9).get("KEY")).isEmpty();

        assertThat((String) teamsTable.get(10).get("FIELD")).isEqualTo("status");
        assertThat((String) teamsTable.get(10).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamsTable.get(10).get("NULL")).isEqualTo("NO");
        assertThat((String) teamsTable.get(10).get("KEY")).isEmpty();

        assertThat((String) teamsTable.get(11).get("FIELD")).isEqualTo("visibility");
        assertThat((String) teamsTable.get(11).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamsTable.get(11).get("NULL")).isEqualTo("NO");
        assertThat((String) teamsTable.get(11).get("KEY")).isEmpty();

        assertThat((String) teamsTable.get(12).get("FIELD")).isEqualTo("website");
        assertThat((String) teamsTable.get(12).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamsTable.get(12).get("NULL")).isEqualTo("NO");
        assertThat((String) teamsTable.get(12).get("KEY")).isEmpty();

        List<Map<String, Object>> teamsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'teams'");

        assertThat((String) teamsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) teamsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) teamsConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) teamsConstraints.get(1).get("COLUMN_LIST")).isEqualTo("NAME");
    }

    @Test
    public void testUserDetailsTable() throws Exception {
        // make sure 'user_details' table has expected number of columns
        List<Map<String, Object>> userDetailsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + USER_DETAILS);
        assertThat(userDetailsTable.size()).isEqualTo(13);

        // make sure 'user_details' table has expected column name and type
        assertThat((String) userDetailsTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) userDetailsTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) userDetailsTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) userDetailsTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) userDetailsTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) userDetailsTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) userDetailsTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) userDetailsTable.get(1).get("KEY")).isEmpty();

        assertThat((String) userDetailsTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) userDetailsTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) userDetailsTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) userDetailsTable.get(2).get("KEY")).isEmpty();

        assertThat((String) userDetailsTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) userDetailsTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) userDetailsTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) userDetailsTable.get(3).get("KEY")).isEmpty();

        assertThat((String) userDetailsTable.get(4).get("FIELD")).isEqualTo("email");
        assertThat((String) userDetailsTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) userDetailsTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) userDetailsTable.get(4).get("KEY")).isEqualTo("UNI");

        assertThat((String) userDetailsTable.get(5).get("FIELD")).isEqualTo("first_name");
        assertThat((String) userDetailsTable.get(5).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) userDetailsTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) userDetailsTable.get(5).get("KEY")).isEmpty();

        assertThat((String) userDetailsTable.get(6).get("FIELD")).isEqualTo("institution");
        assertThat((String) userDetailsTable.get(6).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) userDetailsTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) userDetailsTable.get(6).get("KEY")).isEmpty();

        assertThat((String) userDetailsTable.get(7).get("FIELD")).isEqualTo("institution_abbreviation");
        assertThat((String) userDetailsTable.get(7).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) userDetailsTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) userDetailsTable.get(7).get("KEY")).isEmpty();

        assertThat((String) userDetailsTable.get(8).get("FIELD")).isEqualTo("institution_web");
        assertThat((String) userDetailsTable.get(8).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) userDetailsTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) userDetailsTable.get(8).get("KEY")).isEmpty();

        assertThat((String) userDetailsTable.get(9).get("FIELD")).isEqualTo("job_title");
        assertThat((String) userDetailsTable.get(9).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) userDetailsTable.get(9).get("NULL")).isEqualTo("NO");
        assertThat((String) userDetailsTable.get(9).get("KEY")).isEmpty();

        assertThat((String) userDetailsTable.get(10).get("FIELD")).isEqualTo("last_name");
        assertThat((String) userDetailsTable.get(10).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) userDetailsTable.get(10).get("NULL")).isEqualTo("NO");
        assertThat((String) userDetailsTable.get(10).get("KEY")).isEmpty();

        assertThat((String) userDetailsTable.get(11).get("FIELD")).isEqualTo("phone");
        assertThat((String) userDetailsTable.get(11).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) userDetailsTable.get(11).get("NULL")).isEqualTo("NO");
        assertThat((String) userDetailsTable.get(11).get("KEY")).isEmpty();

        assertThat((String) userDetailsTable.get(12).get("FIELD")).isEqualTo("address_id");
        assertThat((String) userDetailsTable.get(12).get("TYPE")).contains("bigint");
        assertThat((String) userDetailsTable.get(12).get("NULL")).isEqualTo("NO");
        assertThat((String) userDetailsTable.get(12).get("KEY")).isEmpty();

        List<Map<String, Object>> userDetailsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'user_details'");

        assertThat((String) userDetailsConstraints.get(2).get("CONSTRAINT_TYPE")).isEqualTo("REFERENTIAL");
        assertThat((String) userDetailsConstraints.get(2).get("COLUMN_LIST")).isEqualTo("ADDRESS_ID");
        assertThat((String) userDetailsConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) userDetailsConstraints.get(1).get("COLUMN_LIST")).isEqualTo("EMAIL");
        assertThat((String) userDetailsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) userDetailsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
    }

    @Test
    public void testUsersTable() throws Exception {
        // make sure 'users' table has expected number of columns
        List<Map<String, Object>> usersTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + USERS);
        assertThat(usersTable.size()).isEqualTo(10);

        // make sure 'users' table has expected column name and type
        assertThat((String) usersTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) usersTable.get(0).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) usersTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) usersTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) usersTable.get(1).get("FIELD")).isEqualTo("created_date");
        assertThat((String) usersTable.get(1).get("TYPE")).contains("timestamp");
        assertThat((String) usersTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) usersTable.get(1).get("KEY")).isEmpty();

        assertThat((String) usersTable.get(2).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) usersTable.get(2).get("TYPE")).contains("timestamp");
        assertThat((String) usersTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) usersTable.get(2).get("KEY")).isEmpty();

        assertThat((String) usersTable.get(3).get("FIELD")).isEqualTo("version");
        assertThat((String) usersTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) usersTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) usersTable.get(3).get("KEY")).isEmpty();

        assertThat((String) usersTable.get(4).get("FIELD")).isEqualTo("application_date");
        assertThat((String) usersTable.get(4).get("TYPE")).contains("timestamp");
        assertThat((String) usersTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) usersTable.get(4).get("KEY")).isEmpty();

        assertThat((String) usersTable.get(5).get("FIELD")).isEqualTo("is_email_verified");
        assertThat((String) usersTable.get(5).get("TYPE")).isEqualTo("char(1)");
        assertThat((String) usersTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) usersTable.get(5).get("KEY")).isEmpty();

        assertThat((String) usersTable.get(6).get("FIELD")).isEqualTo("processed_date");
        assertThat((String) usersTable.get(6).get("TYPE")).contains("timestamp");
        assertThat((String) usersTable.get(6).get("NULL")).isEqualTo("YES");
        assertThat((String) usersTable.get(6).get("KEY")).isEmpty();

        assertThat((String) usersTable.get(7).get("FIELD")).isEqualTo("status");
        assertThat((String) usersTable.get(7).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) usersTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) usersTable.get(7).get("KEY")).isEmpty();

        assertThat((String) usersTable.get(8).get("FIELD")).isEqualTo("verification_key");
        assertThat((String) usersTable.get(8).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) usersTable.get(8).get("NULL")).isEqualTo("YES");
        assertThat((String) usersTable.get(8).get("KEY")).isEmpty();

        assertThat((String) usersTable.get(9).get("FIELD")).isEqualTo("user_details_id");
        assertThat((String) usersTable.get(9).get("TYPE")).contains("bigint");
        assertThat((String) usersTable.get(9).get("NULL")).isEqualTo("NO");
        assertThat((String) usersTable.get(9).get("KEY")).isEqualTo("UNI");

        List<Map<String, Object>> usersConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'users'");

        assertThat((String) usersConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) usersConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) usersConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("REFERENTIAL");
        assertThat((String) usersConstraints.get(1).get("COLUMN_LIST")).isEqualTo("USER_DETAILS_ID");
        assertThat((String) usersConstraints.get(2).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) usersConstraints.get(2).get("COLUMN_LIST")).isEqualTo("USER_DETAILS_ID");
    }

    @Test
    public void testUsersTeamsTable() throws Exception {
        // make sure 'users_teams' table has expected number of columns
        List<Map<String, Object>> userTeamsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + USERS_TEAMS);
        assertThat(userTeamsTable.size()).isEqualTo(2);

        // make sure 'users_teams' table has expected column name and type
        assertThat((String) userTeamsTable.get(0).get("FIELD")).isEqualTo("user_id");
        assertThat((String) userTeamsTable.get(0).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) userTeamsTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) userTeamsTable.get(0).get("KEY")).isEqualTo("UNI");

        assertThat((String) userTeamsTable.get(1).get("FIELD")).isEqualTo("team_id");
        assertThat((String) userTeamsTable.get(1).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) userTeamsTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) userTeamsTable.get(1).get("KEY")).isEqualTo("UNI");

        List<Map<String, Object>> userTeamsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'users_teams'");
        assertThat((String) userTeamsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("REFERENTIAL");
        assertThat((String) userTeamsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("USER_ID");
        assertThat((String) userTeamsConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) userTeamsConstraints.get(1).get("COLUMN_LIST")).isEqualTo("USER_ID,TEAM_ID");
    }

    @Test
    public void testDataInsertion() throws Exception {
        // make sure that initial data has been added into tables
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".addresses",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".credentials",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".credentials_roles",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".deterlab_project",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".deterlab_user",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".email_retries",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".experiments",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".user_details",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".users",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".login_activities",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".realizations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".registrations",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".teams",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".team_members",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".users_teams",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".data",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".data_resources",
                Integer.class)).isEqualTo(0);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".data_users",
                Integer.class)).isEqualTo(0);
    }

    private SimpleDriverDataSource createDataSource() {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass(org.h2.Driver.class);
        simpleDriverDataSource.setUrl("jdbc:h2:mem:prod;MODE=MySQL;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS \"public\"");
        simpleDriverDataSource.setUsername("sa");
        simpleDriverDataSource.setPassword("");
        return simpleDriverDataSource;
    }

}
