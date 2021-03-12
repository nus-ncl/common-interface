package db.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sg.ncl.common.authentication.Role;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.service.team.domain.MemberStatus;
import sg.ncl.service.team.domain.MemberType;
import sg.ncl.service.team.domain.TeamPrivacy;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.user.domain.UserStatus;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static java.util.UUID.randomUUID;

/**
 * Created by dcszwang on 10/12/2016.
 */
@Slf4j
public class V1_1__initial_data implements SpringJdbcMigration {

    private static final String SQL_INSERT_TEAMS = "INSERT INTO prod.teams"
            + "(id, created_date, last_modified_date, version, application_date, description, name, organisation_type, privacy, status, visibility, website) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_ADDRESS = "INSERT INTO prod.addresses"
            + "(created_date, last_modified_date, version, address_1, address_2, city, country, region, zip_code) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_USERS_DETAILS = "INSERT INTO prod.user_details"
            + "(created_date, last_modified_date, version, email, first_name, institution, institution_abbreviation, institution_web, job_title, last_name, phone, address_id) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_USERS = "INSERT INTO prod.users"
            + "(id, created_date, last_modified_date, version, application_date, is_email_verified, status, user_details_id) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_CREDENTIALS = "INSERT INTO prod.credentials"
            + "(id, created_date, last_modified_date, version, password, status, username) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_CREDENTIALS_ROLES = "INSERT INTO prod.credentials_roles"
            + "(credentials_id, role) VALUES"
            + "(?, ?)";
    private static final String SQL_INSERT_USERS_TEAMS = "INSERT INTO prod.users_teams"
            + "(user_id, team_id) VALUES"
            + "(?, ?)";
    private static final String SQL_INSERT_TEAM_MEMBERS = "INSERT INTO prod.team_members"
            + "(created_date, last_modified_date, version, joined_date, member_type, user_id, team_id, status) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_DETERLAB_USER = "INSERT INTO prod.deterlab_user"
            + "(created_date, last_modified_date, version, deter_user_id, ncl_user_id) VALUES"
            + "(?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_DETERLAB_PROJECT = "INSERT INTO prod.deterlab_project"
            + "(created_date, last_modified_date, version, deter_project_id, ncl_team_id) VALUES"
            + "(?, ?, ?, ?, ?)";

    private final PasswordEncoder passwordEncoder;

    public V1_1__initial_data() {
        // FIXME: need to find a way to get the bean
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
        log.debug("Adding initial data to database");
        final String teamId = createTeam(jdbcTemplate, "testbed-ncl", "NCL Administrative Team", "Academic", "https://ncl.sg", TeamPrivacy.OPEN, TeamStatus.APPROVED, TeamVisibility.PRIVATE);

        final int addressId = createAddress(jdbcTemplate, "13 Computing Drive", "COM1-01-16", "Singapore", "SG", "SG", "117417");

        final int detailsId = createDetails(jdbcTemplate, "Admin", "ncl-admin@ncl.sg", addressId, "National Cybersecurity R&D Lab", "NCL", "https://ncl.sg", "Research Lab", "NCL", "12345678");

        final String userId = createUser(jdbcTemplate, "Y", UserStatus.APPROVED, detailsId);

        createCredentials(jdbcTemplate, "ncl-admin@ncl.sg", "deterinavm", userId, CredentialsStatus.ACTIVE);

        createRoles(jdbcTemplate, userId, Collections.singletonList(Role.ADMIN));

        addToTeam(jdbcTemplate, userId, teamId, MemberType.OWNER, MemberStatus.APPROVED);

        createDeterLabUser(jdbcTemplate, "ncl", userId);

        createDeterLabProject(jdbcTemplate, "testbed-ncl", teamId);
    }

    private String createTeam(final JdbcTemplate jdbcTemplate, final String teamName, final String description, final String organizationType, final String url, final TeamPrivacy privacy, final TeamStatus status, final TeamVisibility visibility) throws SQLException {
        // insert the team
        final String id = randomUUID().toString();
        final Timestamp time = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());
        final int version = 0;
        final int i = jdbcTemplate.update(SQL_INSERT_TEAMS, id, time, time, version, time, description, teamName, organizationType, privacy.name(), status.name(), visibility.name(), url);
        log.info("Inserted {} team entry: id={}, created_date={}, last_modified_date={}, version={}, application_date={}, description={}, name={}, organisation_type={}, privacy={}, status={}, visibility={}, website={}", i, id, time, time, version, time, description, teamName, organizationType, privacy.name(), status.name(), visibility.name(), url);
        return id;
    }

    private int createAddress(final JdbcTemplate jdbcTemplate, final String address1, final String address2, final String city, final String country, final String region, final String zip) throws SQLException {
        // insert the address
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final Timestamp now = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());
        final int version = 0;
        final int i = jdbcTemplate.update(c -> {
            final PreparedStatement statement = c.prepareStatement(SQL_INSERT_ADDRESS, new String[]{"id"});
            statement.setObject(1, now);
            statement.setObject(2, now);
            statement.setInt(3, version);
            statement.setString(4, address1);
            statement.setString(5, address2);
            statement.setString(6, city);
            statement.setString(7, country);
            statement.setString(8, region);
            statement.setString(9, zip);
            return statement;
        }, keyHolder);
        final int id = keyHolder.getKey().intValue();
        log.info("Inserted {} address entry: id={}, created_date={}, last_modified_date={}, version={}, address_1={}, address_2={}, city={}, country={}, region={}, zip_code={}", i, id, now, now, version, address1, address2, city, country, region, zip);
        return id;
    }

    private int createDetails(final JdbcTemplate jdbcTemplate, final String firstName, final String email, final int addressId, final String institution, final String abbreviation, final String url, final String jobTitle, final String lastName, final String phone) throws SQLException {
        // insert the userDetails
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final Timestamp now = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());
        final int version = 0;
        final int i = jdbcTemplate.update(c -> {
            final PreparedStatement statement = c.prepareStatement(SQL_INSERT_USERS_DETAILS, new String[]{"id"});
            statement.setObject(1, now);
            statement.setObject(2, now);
            statement.setInt(3, version);
            statement.setString(4, email);
            statement.setString(5, firstName);
            statement.setString(6, institution);
            statement.setString(7, abbreviation);
            statement.setString(8, url);
            statement.setString(9, jobTitle);
            statement.setString(10, lastName);
            statement.setString(11, phone);
            statement.setInt(12, addressId);
            return statement;
        }, keyHolder);
        final int id = keyHolder.getKey().intValue();
        log.info("Insert {} details entry: id={}, created_date={}, last_modified_date={}, version={}, email={}, first_name={}, institution={}, institution_abbreviation={}, institution_web={}, job_title={}, last_name={}, phone={}, address_id={}", i, id, now, now, version, email, firstName, institution, abbreviation, url, jobTitle, lastName, phone, addressId);
        return id;
    }

    private String createUser(final JdbcTemplate jdbcTemplate, final String isEmailVerified, final UserStatus userStatus, final int detailsId) throws SQLException {
        // insert the users
        final String id = randomUUID().toString();
        final Timestamp now = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());
        final int version = 0;
        final int i = jdbcTemplate.update(SQL_INSERT_USERS, id, now, now, version, now, isEmailVerified, userStatus.name(), detailsId);
        log.info("Inserted {} user entry: id={}, created_date={}, last_modified_date={}, version={}, application_date={}, is_email_verified={}, status={}, user_details_id={}", i, id, now, now, version, now, isEmailVerified, userStatus, detailsId);
        return id;
    }

    private void createCredentials(final JdbcTemplate jdbcTemplate, final String username, final String password, final String userId, final CredentialsStatus status) throws SQLException {
        // insert the credentials
        final Timestamp now = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());
        final int version = 0;
        final int i = jdbcTemplate.update(SQL_INSERT_CREDENTIALS, userId, now, now, version, passwordEncoder.encode(password), status.name(), username);
        log.info("Inserted {} credentials entry: id={}, created_date={}, last_modified_date={}, version={}, status={}, username={}", i, userId, now, now, version, status, username);
    }

    private void createRoles(final JdbcTemplate jdbcTemplate, final String userId, final List<Role> roles) {
        roles.forEach(role -> {
            final int i = jdbcTemplate.update(SQL_INSERT_CREDENTIALS_ROLES, userId, role.toString());
            log.info("Inserted {} roles entry: credentials_id={}, role={}", i, userId, role);
        });
    }

    // add user to team for both user side and team side
    private void addToTeam(final JdbcTemplate jdbcTemplate, String userId, String teamId, MemberType memberType, MemberStatus memberStatus) throws SQLException {
        // insert into user side
        final int i = jdbcTemplate.update(SQL_INSERT_USERS_TEAMS, userId, teamId);
        log.info("Inserted {} user_team entry: user_id={}, team_id={}", i, userId, teamId);

        // insert into team side (team members)
        final Timestamp now = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());
        final int j = jdbcTemplate.update(SQL_INSERT_TEAM_MEMBERS, now, now, 0, now, memberType.name(), userId, teamId, memberStatus.name());
        log.info("Inserted {} team_member entry: created_date={}, last_modified_date={}, version={}, joined_date={}, member_type={}, user_id={}, team_id={}, status={}", j, now, now, 0, now, memberType, userId, teamId, memberStatus);
    }

    private void createDeterLabUser(final JdbcTemplate jdbcTemplate, final String name, final String userId) throws SQLException {
        // insert into deterlab_user
        final Timestamp now = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());
        final int version = 0;
        final int i = jdbcTemplate.update(SQL_INSERT_DETERLAB_USER, now, now, version, name, userId);
        log.info("Inserted {} deter user entry: created_date={}, last_modified_date={}, version={}, deter_user_id={}, ncl_user_id={}", i, now, now, version, name, userId);
    }

    private void createDeterLabProject(final JdbcTemplate jdbcTemplate, final String name, final String teamId) throws SQLException {
        // insert into deterlab_user
        final Timestamp now = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());
        final int version = 0;
        final int i = jdbcTemplate.update(SQL_INSERT_DETERLAB_PROJECT, now, now, version, name, teamId);
        log.info("Inserted {} deter project entry: created_date={}, last_modified_date={}, version={}, deter_project_id={}, ncl_team_id={}", i, now, now, version, name, teamId);
    }
    public static void main(String[] args) throws Exception {
//        System.out.println("hello");
        V1_1__initial_data obj = new V1_1__initial_data();
        ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
        JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
        obj.migrate(jdbcTemplate);
    }
}
