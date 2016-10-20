package sg.ncl;

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

import java.sql.SQLException;
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
    private static final String DATA_RESOURCES = "data_resources";
    private static final String DATA_USERS = "data_users";
    private static final String DETERLAB_PROJECT = "deterlab_project";
    private static final String DETERLAB_USER = "deterlab_user";
    private static final String EMAIL_RETRIES = "email_retries";
    private static final String EXPERIMENTS = "experiments";
    private static final String LOGIN_ACTIVITIES = "login_activities";
    private static final String REALIZATIONS = "realizations";
    private static final String REGISTRATIONS = "registrations";
    private static final String TEAM_MEMBERS = "team_members";
    private static final String TEAMS = "teams";
    private static final String USER_DETAILS = "user_details";
    private static final String USERS = "users";
    private static final String USERS_TEAMS = "users_teams";
    private static final String SCHEMA = "prod";

    private DataSource dataSource;
    private JdbcTemplate template;
    private Flyway flyway;

    @Before
    public void setup() throws SQLException {
        this.dataSource = createDataSource();
        this.template = new JdbcTemplate(dataSource);
        this.flyway = new Flyway();
        this.flyway.setDataSource(dataSource);
    }

    @Test
    public void testConversionDatesFromBlob() throws Exception {

        flyway.migrate();

        // make sure that we have the exact number of tables
        List<Map<String, Object>> tableNames = this.template.queryForList("SHOW TABLES FROM " + SCHEMA);
        assertThat(tableNames.size()).isEqualTo(18);

        // make sure all tables name matches
        assertThat(tableNames.get(0).get("TABLE_NAME")).isEqualTo(ADDRESSES);
        assertThat(tableNames.get(0).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(1).get("TABLE_NAME")).isEqualTo(CREDENTIALS);
        assertThat(tableNames.get(1).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(2).get("TABLE_NAME")).isEqualTo(CREDENTIALS_ROLES);
        assertThat(tableNames.get(2).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(3).get("TABLE_NAME")).isEqualTo(DATA);
        assertThat(tableNames.get(3).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(4).get("TABLE_NAME")).isEqualTo(DATA_RESOURCES);
        assertThat(tableNames.get(4).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(5).get("TABLE_NAME")).isEqualTo(DATA_USERS);
        assertThat(tableNames.get(5).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(6).get("TABLE_NAME")).isEqualTo(DETERLAB_PROJECT);
        assertThat(tableNames.get(6).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(7).get("TABLE_NAME")).isEqualTo(DETERLAB_USER);
        assertThat(tableNames.get(7).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(8).get("TABLE_NAME")).isEqualTo(EMAIL_RETRIES);
        assertThat(tableNames.get(8).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(9).get("TABLE_NAME")).isEqualTo(EXPERIMENTS);
        assertThat(tableNames.get(9).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(10).get("TABLE_NAME")).isEqualTo(LOGIN_ACTIVITIES);
        assertThat(tableNames.get(10).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(11).get("TABLE_NAME")).isEqualTo(REALIZATIONS);
        assertThat(tableNames.get(11).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(12).get("TABLE_NAME")).isEqualTo(REGISTRATIONS);
        assertThat(tableNames.get(12).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(13).get("TABLE_NAME")).isEqualTo(TEAM_MEMBERS);
        assertThat(tableNames.get(13).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(14).get("TABLE_NAME")).isEqualTo(TEAMS);
        assertThat(tableNames.get(14).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(15).get("TABLE_NAME")).isEqualTo(USER_DETAILS);
        assertThat(tableNames.get(15).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(16).get("TABLE_NAME")).isEqualTo(USERS);
        assertThat(tableNames.get(16).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);
        assertThat(tableNames.get(17).get("TABLE_NAME")).isEqualTo(USERS_TEAMS);
        assertThat(tableNames.get(17).get("TABLE_SCHEMA")).isEqualTo(SCHEMA);

        // make sure 'addresses' table has expected number of columns
        List<Map<String, Object>> addressesTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + ADDRESSES);
        assertThat(addressesTable.size()).isEqualTo(10);

        // make sure 'addresses' table has expected column name and type
        assertThat((String) addressesTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) addressesTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) addressesTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) addressesTable.get(1).get("FIELD")).isEqualTo("version");
        assertThat((String) addressesTable.get(1).get("TYPE")).contains("bigint");
        assertThat((String) addressesTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(1).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(2).get("FIELD")).isEqualTo("address_1");
        assertThat((String) addressesTable.get(2).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) addressesTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(2).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(3).get("FIELD")).isEqualTo("address_2");
        assertThat((String) addressesTable.get(3).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) addressesTable.get(3).get("NULL")).isEqualTo("YES");
        assertThat((String) addressesTable.get(3).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(4).get("FIELD")).isEqualTo("city");
        assertThat((String) addressesTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) addressesTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(4).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(5).get("FIELD")).isEqualTo("country");
        assertThat((String) addressesTable.get(5).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) addressesTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(5).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(6).get("FIELD")).isEqualTo("region");
        assertThat((String) addressesTable.get(6).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) addressesTable.get(6).get("NULL")).isEqualTo("YES");
        assertThat((String) addressesTable.get(6).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(7).get("FIELD")).isEqualTo("zip_code");
        assertThat((String) addressesTable.get(7).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) addressesTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(7).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(8).get("FIELD")).isEqualTo("created_date");
        assertThat((String) addressesTable.get(8).get("TYPE")).contains("timestamp");
        assertThat((String) addressesTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(8).get("KEY")).isEmpty();

        assertThat((String) addressesTable.get(9).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) addressesTable.get(9).get("TYPE")).contains("timestamp");
        assertThat((String) addressesTable.get(9).get("NULL")).isEqualTo("NO");
        assertThat((String) addressesTable.get(9).get("KEY")).isEmpty();

        List<Map<String, Object>> addressesConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'addresses'");

        assertThat((String) addressesConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) addressesConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");


        // make sure 'credentials' table has expected number of columns
        List<Map<String, Object>> credentialsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + CREDENTIALS);
        assertThat(credentialsTable.size()).isEqualTo(7);

        // make sure 'credentials' table has expected column name and type
        assertThat((String) credentialsTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) credentialsTable.get(0).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) credentialsTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) credentialsTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) credentialsTable.get(1).get("FIELD")).isEqualTo("version");
        assertThat((String) credentialsTable.get(1).get("TYPE")).contains("bigint");
        assertThat((String) credentialsTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) credentialsTable.get(1).get("KEY")).isEmpty();

        assertThat((String) credentialsTable.get(2).get("FIELD")).isEqualTo("password");
        assertThat((String) credentialsTable.get(2).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) credentialsTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) credentialsTable.get(2).get("KEY")).isEmpty();

        assertThat((String) credentialsTable.get(3).get("FIELD")).isEqualTo("status");
        assertThat((String) credentialsTable.get(3).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) credentialsTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) credentialsTable.get(3).get("KEY")).isEmpty();

        assertThat((String) credentialsTable.get(4).get("FIELD")).isEqualTo("username");
        assertThat((String) credentialsTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) credentialsTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) credentialsTable.get(4).get("KEY")).isEqualTo("UNI");

        assertThat((String) credentialsTable.get(5).get("FIELD")).isEqualTo("created_date");
        assertThat((String) credentialsTable.get(5).get("TYPE")).contains("timestamp");
        assertThat((String) credentialsTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) credentialsTable.get(5).get("KEY")).isEmpty();

        assertThat((String) credentialsTable.get(6).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) credentialsTable.get(6).get("TYPE")).contains("timestamp");
        assertThat((String) credentialsTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) credentialsTable.get(6).get("KEY")).isEmpty();

        List<Map<String, Object>> credentialsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'credentials'");

        assertThat((String) credentialsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) credentialsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) credentialsConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) credentialsConstraints.get(1).get("COLUMN_LIST")).isEqualTo("USERNAME");


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


        // make sure 'data' table has expected number of columns
        List<Map<String, Object>> dataTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + DATA);
        assertThat(dataTable.size()).isEqualTo(9);

        System.out.println(dataTable.get(0));
        System.out.println(dataTable.get(1));
        System.out.println(dataTable.get(2));
        System.out.println(dataTable.get(3));
        System.out.println(dataTable.get(4));
        System.out.println(dataTable.get(5));
        System.out.println(dataTable.get(6));

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



        List<Map<String, Object>> dataConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'data'");
        System.out.println(dataConstraints.size());
        System.out.println(dataConstraints.get(0));
        System.out.println(dataConstraints.get(1));
        assertThat((String) dataConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) dataConstraints.get(1).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) dataConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) dataConstraints.get(0).get("COLUMN_LIST")).isEqualTo("NAME");


        // make sure 'data_resources' table has expected number of columns
        List<Map<String, Object>> dataResourcesTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + DATA_RESOURCES);
        assertThat(dataResourcesTable.size()).isEqualTo(6);

        System.out.println(dataResourcesTable.get(0));
        System.out.println(dataResourcesTable.get(1));
        System.out.println(dataResourcesTable.get(2));
        System.out.println(dataResourcesTable.get(3));
        System.out.println(dataResourcesTable.get(4));
        System.out.println(dataResourcesTable.get(5));

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

        List<Map<String, Object>> dataResourcesConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'data_resources'");
        System.out.println(dataResourcesConstraints.size());
        System.out.println(dataResourcesConstraints.get(0));
        System.out.println(dataResourcesConstraints.get(1));
        assertThat((String) dataResourcesConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) dataResourcesConstraints.get(1).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) dataResourcesConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("REFERENTIAL");
        assertThat((String) dataResourcesConstraints.get(0).get("COLUMN_LIST")).isEqualTo("DATA_ID");


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


        // make sure 'deterlab_project' table has expected number of columns
        List<Map<String, Object>> deterlabProjectTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + DETERLAB_PROJECT);
        assertThat(deterlabProjectTable.size()).isEqualTo(6);

        System.out.println(deterlabProjectTable.get(0));
        System.out.println(deterlabProjectTable.get(1));
        System.out.println(deterlabProjectTable.get(2));
        System.out.println(deterlabProjectTable.get(3));
        System.out.println(deterlabProjectTable.get(4));
        System.out.println(deterlabProjectTable.get(5));

        // make sure 'deterlab_project' table has expected column name and type
        assertThat((String) deterlabProjectTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) deterlabProjectTable.get(0).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) deterlabProjectTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabProjectTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) deterlabProjectTable.get(1).get("FIELD")).isEqualTo("version");
        assertThat((String) deterlabProjectTable.get(1).get("TYPE")).contains("bigint");
        assertThat((String) deterlabProjectTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabProjectTable.get(1).get("KEY")).isEmpty();

        assertThat((String) deterlabProjectTable.get(2).get("FIELD")).isEqualTo("deter_project_id");
        assertThat((String) deterlabProjectTable.get(2).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) deterlabProjectTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabProjectTable.get(2).get("KEY")).isEqualTo("UNI");

        assertThat((String) deterlabProjectTable.get(3).get("FIELD")).isEqualTo("ncl_team_id");
        assertThat((String) deterlabProjectTable.get(3).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) deterlabProjectTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabProjectTable.get(3).get("KEY")).isEqualTo("UNI");

        assertThat((String) deterlabProjectTable.get(4).get("FIELD")).isEqualTo("created_date");
        assertThat((String) deterlabProjectTable.get(4).get("TYPE")).contains("timestamp");
        assertThat((String) deterlabProjectTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabProjectTable.get(4).get("KEY")).isEmpty();

        assertThat((String) deterlabProjectTable.get(5).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) deterlabProjectTable.get(5).get("TYPE")).contains("timestamp");
        assertThat((String) deterlabProjectTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabProjectTable.get(5).get("KEY")).isEmpty();

        List<Map<String, Object>> deterlabProjectConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'deterlab_project'");
        System.out.println(deterlabProjectConstraints.size());
        System.out.println(deterlabProjectConstraints.get(0));
        System.out.println(deterlabProjectConstraints.get(1));
        System.out.println(deterlabProjectConstraints.get(2));
        assertThat((String) deterlabProjectConstraints.get(2).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) deterlabProjectConstraints.get(2).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) deterlabProjectConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) deterlabProjectConstraints.get(1).get("COLUMN_LIST")).isEqualTo("DETER_PROJECT_ID");
        assertThat((String) deterlabProjectConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) deterlabProjectConstraints.get(0).get("COLUMN_LIST")).isEqualTo("NCL_TEAM_ID");



        // make sure 'deterlab_user' table has expected number of columns
        List<Map<String, Object>> deterlabUserTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + DETERLAB_USER);
        assertThat(deterlabUserTable.size()).isEqualTo(6);

        // make sure 'deterlab_user' table has expected column name and type
        assertThat((String) deterlabUserTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) deterlabUserTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) deterlabUserTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabUserTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) deterlabUserTable.get(1).get("FIELD")).isEqualTo("version");
        assertThat((String) deterlabUserTable.get(1).get("TYPE")).contains("bigint");
        assertThat((String) deterlabUserTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabUserTable.get(1).get("KEY")).isEmpty();

        assertThat((String) deterlabUserTable.get(2).get("FIELD")).isEqualTo("deter_user_id");
        assertThat((String) deterlabUserTable.get(2).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) deterlabUserTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabUserTable.get(2).get("KEY")).isEqualTo("UNI");

        assertThat((String) deterlabUserTable.get(3).get("FIELD")).isEqualTo("ncl_user_id");
        assertThat((String) deterlabUserTable.get(3).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) deterlabUserTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabUserTable.get(3).get("KEY")).isEqualTo("UNI");

        assertThat((String) deterlabUserTable.get(4).get("FIELD")).isEqualTo("created_date");
        assertThat((String) deterlabUserTable.get(4).get("TYPE")).contains("timestamp");
        assertThat((String) deterlabUserTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabUserTable.get(4).get("KEY")).isEmpty();

        assertThat((String) deterlabUserTable.get(5).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) deterlabUserTable.get(5).get("TYPE")).contains("timestamp");
        assertThat((String) deterlabUserTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) deterlabUserTable.get(5).get("KEY")).isEmpty();

        List<Map<String, Object>> deterlabUserConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'deterlab_user'");
        System.out.println(deterlabUserConstraints.size());
        System.out.println(deterlabUserConstraints.get(0));
        System.out.println(deterlabUserConstraints.get(1));
        System.out.println(deterlabUserConstraints.get(2));
        assertThat((String) deterlabUserConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) deterlabUserConstraints.get(1).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) deterlabUserConstraints.get(2).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) deterlabUserConstraints.get(2).get("COLUMN_LIST")).isEqualTo("DETER_USER_ID");
        assertThat((String) deterlabUserConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) deterlabUserConstraints.get(0).get("COLUMN_LIST")).isEqualTo("NCL_USER_ID");


        // make sure 'email_retries' table has expected number of columns
        List<Map<String, Object>> emailRetriesTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + EMAIL_RETRIES);
        assertThat(emailRetriesTable.size()).isEqualTo(15);

        // make sure 'email_retries' table has expected column name and type
        assertThat((String) emailRetriesTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) emailRetriesTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) emailRetriesTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) emailRetriesTable.get(1).get("FIELD")).isEqualTo("version");
        assertThat((String) emailRetriesTable.get(1).get("TYPE")).contains("bigint");
        assertThat((String) emailRetriesTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(1).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(2).get("FIELD")).isEqualTo("bcc");
        assertThat((String) emailRetriesTable.get(2).get("TYPE")).contains("blob");
        assertThat((String) emailRetriesTable.get(2).get("NULL")).isEqualTo("YES");
        assertThat((String) emailRetriesTable.get(2).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(3).get("FIELD")).isEqualTo("cc");
        assertThat((String) emailRetriesTable.get(3).get("TYPE")).contains("blob");
        assertThat((String) emailRetriesTable.get(3).get("NULL")).isEqualTo("YES");
        assertThat((String) emailRetriesTable.get(3).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(4).get("FIELD")).isEqualTo("content");
        assertThat((String) emailRetriesTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) emailRetriesTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(4).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(5).get("FIELD")).isEqualTo("error_message");
        assertThat((String) emailRetriesTable.get(5).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) emailRetriesTable.get(5).get("NULL")).isEqualTo("YES");
        assertThat((String) emailRetriesTable.get(5).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(6).get("FIELD")).isEqualTo("html");
        assertThat((String) emailRetriesTable.get(6).get("TYPE")).isEqualTo("boolean(1)");
        assertThat((String) emailRetriesTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(6).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(7).get("FIELD")).isEqualTo("recipients");
        assertThat((String) emailRetriesTable.get(7).get("TYPE")).contains("blob");
        assertThat((String) emailRetriesTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(7).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(8).get("FIELD")).isEqualTo("retry_times");
        assertThat((String) emailRetriesTable.get(8).get("TYPE")).isEqualTo("integer(10)");
        assertThat((String) emailRetriesTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(8).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(9).get("FIELD")).isEqualTo("sender");
        assertThat((String) emailRetriesTable.get(9).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) emailRetriesTable.get(9).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(9).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(10).get("FIELD")).isEqualTo("sent");
        assertThat((String) emailRetriesTable.get(10).get("TYPE")).isEqualTo("boolean(1)");
        assertThat((String) emailRetriesTable.get(10).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(10).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(11).get("FIELD")).isEqualTo("subject");
        assertThat((String) emailRetriesTable.get(11).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) emailRetriesTable.get(11).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(11).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(12).get("FIELD")).isEqualTo("created_date");
        assertThat((String) emailRetriesTable.get(12).get("TYPE")).contains("timestamp");
        assertThat((String) emailRetriesTable.get(12).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(12).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(13).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) emailRetriesTable.get(13).get("TYPE")).contains("timestamp");
        assertThat((String) emailRetriesTable.get(13).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(13).get("KEY")).isEmpty();

        assertThat((String) emailRetriesTable.get(14).get("FIELD")).isEqualTo("last_retry_time");
        assertThat((String) emailRetriesTable.get(14).get("TYPE")).contains("timestamp");
        assertThat((String) emailRetriesTable.get(14).get("NULL")).isEqualTo("NO");
        assertThat((String) emailRetriesTable.get(14).get("KEY")).isEmpty();

        List<Map<String, Object>> emailRetriesConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'email_retries'");

        assertThat((String) emailRetriesConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) emailRetriesConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");



        // make sure 'experiments' table has expected number of columns
        List<Map<String, Object>> experimentsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + EXPERIMENTS);
        assertThat(experimentsTable.size()).isEqualTo(13);

        // make sure 'experiments' table has expected column name and type
        assertThat((String) experimentsTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) experimentsTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) experimentsTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) experimentsTable.get(1).get("FIELD")).isEqualTo("version");
        assertThat((String) experimentsTable.get(1).get("TYPE")).contains("bigint");
        assertThat((String) experimentsTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(1).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(2).get("FIELD")).isEqualTo("description");
        assertThat((String) experimentsTable.get(2).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) experimentsTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(2).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(3).get("FIELD")).isEqualTo("idle_swap");
        assertThat((String) experimentsTable.get(3).get("TYPE")).contains("integer");
        assertThat((String) experimentsTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(3).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(4).get("FIELD")).isEqualTo("max_duration");
        assertThat((String) experimentsTable.get(4).get("TYPE")).contains("integer");
        assertThat((String) experimentsTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(4).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(5).get("FIELD")).isEqualTo("name");
        assertThat((String) experimentsTable.get(5).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) experimentsTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(5).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(6).get("FIELD")).isEqualTo("ns_file");
        assertThat((String) experimentsTable.get(6).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) experimentsTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(6).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(7).get("FIELD")).isEqualTo("ns_file_content");
        assertThat((String) experimentsTable.get(7).get("TYPE")).contains("clob");
        assertThat((String) experimentsTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(7).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(8).get("FIELD")).isEqualTo("team_id");
        assertThat((String) experimentsTable.get(8).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) experimentsTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(8).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(9).get("FIELD")).isEqualTo("team_name");
        assertThat((String) experimentsTable.get(9).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) experimentsTable.get(9).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(9).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(10).get("FIELD")).isEqualTo("user_id");
        assertThat((String) experimentsTable.get(10).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) experimentsTable.get(10).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(10).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(11).get("FIELD")).isEqualTo("created_date");
        assertThat((String) experimentsTable.get(11).get("TYPE")).contains("timestamp");
        assertThat((String) experimentsTable.get(11).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(11).get("KEY")).isEmpty();

        assertThat((String) experimentsTable.get(12).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) experimentsTable.get(12).get("TYPE")).contains("timestamp");
        assertThat((String) experimentsTable.get(12).get("NULL")).isEqualTo("NO");
        assertThat((String) experimentsTable.get(12).get("KEY")).isEmpty();

        List<Map<String, Object>> experimentsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'experiments'");

        assertThat((String) experimentsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) experimentsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");


        // make sure 'login_activities' table has expected number of columns
        List<Map<String, Object>> loginActivitiesTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + LOGIN_ACTIVITIES);
        assertThat(loginActivitiesTable.size()).isEqualTo(7);

        // make sure 'login_activities' table has expected column name and type
        assertThat((String) loginActivitiesTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) loginActivitiesTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) loginActivitiesTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) loginActivitiesTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) loginActivitiesTable.get(1).get("FIELD")).isEqualTo("version");
        assertThat((String) loginActivitiesTable.get(1).get("TYPE")).contains("bigint");
        assertThat((String) loginActivitiesTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) loginActivitiesTable.get(1).get("KEY")).isEmpty();

        assertThat((String) loginActivitiesTable.get(2).get("FIELD")).isEqualTo("ip_address");
        assertThat((String) loginActivitiesTable.get(2).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) loginActivitiesTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) loginActivitiesTable.get(2).get("KEY")).isEmpty();

        assertThat((String) loginActivitiesTable.get(3).get("FIELD")).isEqualTo("user_id");
        assertThat((String) loginActivitiesTable.get(3).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) loginActivitiesTable.get(3).get("NULL")).isEqualTo("YES");
        assertThat((String) loginActivitiesTable.get(3).get("KEY")).isEmpty();

        assertThat((String) loginActivitiesTable.get(4).get("FIELD")).isEqualTo("created_date");
        assertThat((String) loginActivitiesTable.get(4).get("TYPE")).contains("timestamp");
        assertThat((String) loginActivitiesTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) loginActivitiesTable.get(4).get("KEY")).isEmpty();

        assertThat((String) loginActivitiesTable.get(5).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) loginActivitiesTable.get(5).get("TYPE")).contains("timestamp");
        assertThat((String) loginActivitiesTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) loginActivitiesTable.get(5).get("KEY")).isEmpty();

        assertThat((String) loginActivitiesTable.get(6).get("FIELD")).isEqualTo("date");
        assertThat((String) loginActivitiesTable.get(6).get("TYPE")).contains("timestamp");
        assertThat((String) loginActivitiesTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) loginActivitiesTable.get(6).get("KEY")).isEmpty();

        List<Map<String, Object>> loginActivitiesConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'login_activities'");

        assertThat((String) loginActivitiesConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) loginActivitiesConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) loginActivitiesConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("REFERENTIAL");
        assertThat((String) loginActivitiesConstraints.get(1).get("COLUMN_LIST")).isEqualTo("USER_ID");



        // make sure 'realizations' table has expected number of columns
        List<Map<String, Object>> realizationsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + REALIZATIONS);
        assertThat(realizationsTable.size()).isEqualTo(13);

        // make sure 'realizations' table has expected column name and type
        assertThat((String) realizationsTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) realizationsTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) realizationsTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) realizationsTable.get(1).get("FIELD")).isEqualTo("version");
        assertThat((String) realizationsTable.get(1).get("TYPE")).contains("bigint");
        assertThat((String) realizationsTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(1).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(2).get("FIELD")).isEqualTo("details");
        assertThat((String) realizationsTable.get(2).get("TYPE")).contains("clob");
        assertThat((String) realizationsTable.get(2).get("NULL")).isEqualTo("YES");
        assertThat((String) realizationsTable.get(2).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(3).get("FIELD")).isEqualTo("experiment_id");
        assertThat((String) realizationsTable.get(3).get("TYPE")).contains("bigint");
        assertThat((String) realizationsTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(3).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(4).get("FIELD")).isEqualTo("experiment_name");
        assertThat((String) realizationsTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) realizationsTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(4).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(5).get("FIELD")).isEqualTo("idle_minutes");
        assertThat((String) realizationsTable.get(5).get("TYPE")).contains("bigint");
        assertThat((String) realizationsTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(5).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(6).get("FIELD")).isEqualTo("num_nodes");
        assertThat((String) realizationsTable.get(6).get("TYPE")).contains("integer");
        assertThat((String) realizationsTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(6).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(7).get("FIELD")).isEqualTo("running_minutes");
        assertThat((String) realizationsTable.get(7).get("TYPE")).contains("bigint");
        assertThat((String) realizationsTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(7).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(8).get("FIELD")).isEqualTo("state");
        assertThat((String) realizationsTable.get(8).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) realizationsTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(8).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(9).get("FIELD")).isEqualTo("team_id");
        assertThat((String) realizationsTable.get(9).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) realizationsTable.get(9).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(9).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(10).get("FIELD")).isEqualTo("user_id");
        assertThat((String) realizationsTable.get(10).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) realizationsTable.get(10).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(10).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(11).get("FIELD")).isEqualTo("created_date");
        assertThat((String) realizationsTable.get(11).get("TYPE")).contains("timestamp");
        assertThat((String) realizationsTable.get(11).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(11).get("KEY")).isEmpty();

        assertThat((String) realizationsTable.get(12).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) realizationsTable.get(12).get("TYPE")).contains("timestamp");
        assertThat((String) realizationsTable.get(12).get("NULL")).isEqualTo("NO");
        assertThat((String) realizationsTable.get(12).get("KEY")).isEmpty();

        List<Map<String, Object>> realizationsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'realizations'");

        assertThat((String) realizationsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) realizationsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("ID");


        // make sure 'registrations' table has expected number of columns
        List<Map<String, Object>> registrationsTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + REGISTRATIONS);
        assertThat(registrationsTable.size()).isEqualTo(18);

        // make sure 'registrations' table has expected column name and type
        assertThat((String) registrationsTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) registrationsTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) registrationsTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) registrationsTable.get(1).get("FIELD")).isEqualTo("version");
        assertThat((String) registrationsTable.get(1).get("TYPE")).contains("bigint");
        assertThat((String) registrationsTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(1).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(2).get("FIELD")).isEqualTo("pid");
        assertThat((String) registrationsTable.get(2).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(2).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(3).get("FIELD")).isEqualTo("uid");
        assertThat((String) registrationsTable.get(3).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(3).get("KEY")).isEqualTo("UNI");

        assertThat((String) registrationsTable.get(4).get("FIELD")).isEqualTo("usr_addr");
        assertThat((String) registrationsTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(4).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(5).get("FIELD")).isEqualTo("usr_addr2");
        assertThat((String) registrationsTable.get(5).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(5).get("NULL")).isEqualTo("YES");
        assertThat((String) registrationsTable.get(5).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(6).get("FIELD")).isEqualTo("usr_affil");
        assertThat((String) registrationsTable.get(6).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(6).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(7).get("FIELD")).isEqualTo("usr_affil_abbrev");
        assertThat((String) registrationsTable.get(7).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(7).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(8).get("FIELD")).isEqualTo("usr_city");
        assertThat((String) registrationsTable.get(8).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(8).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(9).get("FIELD")).isEqualTo("usr_country");
        assertThat((String) registrationsTable.get(9).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(9).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(9).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(10).get("FIELD")).isEqualTo("usr_email");
        assertThat((String) registrationsTable.get(10).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(10).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(10).get("KEY")).isEqualTo("UNI");

        assertThat((String) registrationsTable.get(11).get("FIELD")).isEqualTo("usr_name");
        assertThat((String) registrationsTable.get(11).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(11).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(11).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(12).get("FIELD")).isEqualTo("usr_phone");
        assertThat((String) registrationsTable.get(12).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(12).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(12).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(13).get("FIELD")).isEqualTo("usr_state");
        assertThat((String) registrationsTable.get(13).get("TYPE")).contains("varchar(255)");
        assertThat((String) registrationsTable.get(13).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(13).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(14).get("FIELD")).isEqualTo("usr_title");
        assertThat((String) registrationsTable.get(14).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(14).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(14).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(15).get("FIELD")).isEqualTo("usr_zip");
        assertThat((String) registrationsTable.get(15).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) registrationsTable.get(15).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(15).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(16).get("FIELD")).isEqualTo("created_date");
        assertThat((String) registrationsTable.get(16).get("TYPE")).contains("timestamp");
        assertThat((String) registrationsTable.get(16).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(16).get("KEY")).isEmpty();

        assertThat((String) registrationsTable.get(17).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) registrationsTable.get(17).get("TYPE")).contains("timestamp");
        assertThat((String) registrationsTable.get(17).get("NULL")).isEqualTo("NO");
        assertThat((String) registrationsTable.get(17).get("KEY")).isEmpty();

        List<Map<String, Object>> registrationsConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'registrations'");

        assertThat((String) registrationsConstraints.get(2).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) registrationsConstraints.get(2).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) registrationsConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) registrationsConstraints.get(1).get("COLUMN_LIST")).isEqualTo("UID");
        assertThat((String) registrationsConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) registrationsConstraints.get(0).get("COLUMN_LIST")).isEqualTo("USR_EMAIL");



        // make sure 'team_members' table has expected number of columns
        List<Map<String, Object>> teamMembersTable = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + "." + TEAM_MEMBERS);
        assertThat(teamMembersTable.size()).isEqualTo(9);

        // make sure 'team_members' table has expected column name and type
        assertThat((String) teamMembersTable.get(0).get("FIELD")).isEqualTo("id");
        assertThat((String) teamMembersTable.get(0).get("TYPE")).contains("bigint");
        assertThat((String) teamMembersTable.get(0).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(0).get("KEY")).isEqualTo("PRI");

        assertThat((String) teamMembersTable.get(1).get("FIELD")).isEqualTo("version");
        assertThat((String) teamMembersTable.get(1).get("TYPE")).contains("bigint");
        assertThat((String) teamMembersTable.get(1).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(1).get("KEY")).isEmpty();

        assertThat((String) teamMembersTable.get(2).get("FIELD")).isEqualTo("status");
        assertThat((String) teamMembersTable.get(2).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamMembersTable.get(2).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(2).get("KEY")).isEmpty();

        assertThat((String) teamMembersTable.get(3).get("FIELD")).isEqualTo("member_type");
        assertThat((String) teamMembersTable.get(3).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamMembersTable.get(3).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(3).get("KEY")).isEmpty();

        assertThat((String) teamMembersTable.get(4).get("FIELD")).isEqualTo("user_id");
        assertThat((String) teamMembersTable.get(4).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamMembersTable.get(4).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(4).get("KEY")).isEqualTo("UNI");

        assertThat((String) teamMembersTable.get(5).get("FIELD")).isEqualTo("team_id");
        assertThat((String) teamMembersTable.get(5).get("TYPE")).isEqualTo("varchar(255)");
        assertThat((String) teamMembersTable.get(5).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(5).get("KEY")).isEqualTo("UNI");

        assertThat((String) teamMembersTable.get(6).get("FIELD")).isEqualTo("created_date");
        assertThat((String) teamMembersTable.get(6).get("TYPE")).contains("timestamp");
        assertThat((String) teamMembersTable.get(6).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(6).get("KEY")).isEmpty();

        assertThat((String) teamMembersTable.get(7).get("FIELD")).isEqualTo("last_modified_date");
        assertThat((String) teamMembersTable.get(7).get("TYPE")).contains("timestamp");
        assertThat((String) teamMembersTable.get(7).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(7).get("KEY")).isEmpty();

        assertThat((String) teamMembersTable.get(8).get("FIELD")).isEqualTo("joined_date");
        assertThat((String) teamMembersTable.get(8).get("TYPE")).contains("timestamp");
        assertThat((String) teamMembersTable.get(8).get("NULL")).isEqualTo("NO");
        assertThat((String) teamMembersTable.get(8).get("KEY")).isEmpty();

        List<Map<String, Object>> teamMembersConstraints = this.template.queryForList("SELECT * FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = 'team_members'");
        System.out.println(teamMembersConstraints.size());
        System.out.println(teamMembersConstraints.get(0));
        System.out.println(teamMembersConstraints.get(1));
        System.out.println(teamMembersConstraints.get(2));

        assertThat((String) teamMembersConstraints.get(2).get("CONSTRAINT_TYPE")).isEqualTo("PRIMARY KEY");
        assertThat((String) teamMembersConstraints.get(2).get("COLUMN_LIST")).isEqualTo("ID");
        assertThat((String) teamMembersConstraints.get(1).get("CONSTRAINT_TYPE")).isEqualTo("REFERENTIAL");
        assertThat((String) teamMembersConstraints.get(1).get("COLUMN_LIST")).isEqualTo("TEAM_ID");
        assertThat((String) teamMembersConstraints.get(0).get("CONSTRAINT_TYPE")).isEqualTo("UNIQUE");
        assertThat((String) teamMembersConstraints.get(0).get("COLUMN_LIST")).isEqualTo("TEAM_ID,USER_ID");


        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".addresses",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".credentials",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".credentials_roles",
                Integer.class)).isEqualTo(1);
        assertThat(this.template.queryForObject("SELECT COUNT(*) from " + SCHEMA + ".deterlab_project",
                Integer.class)).isEqualTo(0);
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

        List<Map<String, Object>> results = this.template.queryForList("SHOW COLUMNS FROM " + SCHEMA + ".addresses");
        assertThat(results.get(8).toString().toUpperCase()).contains("FIELD=CREATED_DATE, TYPE=TIMESTAMP");
        assertThat(results.get(9).toString().toUpperCase()).contains("FIELD=LAST_MODIFIED_DATE, TYPE=TIMESTAMP");

        template.execute("DROP ALL OBJECTS");
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
