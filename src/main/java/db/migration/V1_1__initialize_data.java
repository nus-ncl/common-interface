package db.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
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
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static java.util.UUID.randomUUID;

/**
 * Created by dcszwang on 10/12/2016.
 */
@Slf4j
public class V1_1__initialize_data implements SpringJdbcMigration {

    static private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String SQL_INSERT_TEAMS = "INSERT INTO teams"
            + "(id, created_date, last_modified_date, version, application_date, description, name, organisation_type, privacy, status, visibility, website) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_ADDRESS = "INSERT INTO addresses"
            + "(created_date, last_modified_date, version, address_1, address_2, city, country, region, zip_code) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_USERS_DETAILS = "INSERT INTO user_details"
            + "(created_date, last_modified_date, version, email, first_name, institution, institution_abbreviation, institution_web, job_title, last_name, phone, address_id) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_USERS = "INSERT INTO users"
            + "(id, created_date, last_modified_date, version, application_date, is_email_verified, status, user_details_id) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_CREDENTIALS = "INSERT INTO credentials"
            + "(id, created_date, last_modified_date, version, password, status, username) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_CREDENTIALS_ROLES = "INSERT INTO credentials_roles"
            + "(credentials_id, role) VALUES"
            + "(?, ?)";
    private static final String SQL_INSERT_DETERLAB_USER = "INSERT INTO deterlab_user"
            + "(created_date, last_modified_date, version, deter_user_id, ncl_user_id) VALUES"
            + "(?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_USERS_TEAMS = "INSERT INTO users_teams"
            + "(user_id, team_id) VALUES"
            + "(?, ?)";
    private static final String SQL_INSERT_TEAM_MEMBERS = "INSERT INTO team_members"
            + "(created_date, last_modified_date, version, joined_date, member_type, user_id, team_id, status) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
        log.info("Initializing database on first run");
        final String teamId = createTeam(jdbcTemplate, "NCL", "NCL Administrative Team", "Academic", "https://www.ncl.sg", TeamPrivacy.OPEN, TeamStatus.APPROVED, TeamVisibility.PRIVATE);

        final int addressId = createAddress(jdbcTemplate, "Address1", "", "City", "Country", "Region", "123456");

        final int detailsId = createDetails(jdbcTemplate, "First Name", "admin@ncl.sg", addressId, "Institution", "NCL", "https://www.ncl.sg", "Job Title", "Last Name", "12345678");

        final String userId = createUser(jdbcTemplate, "Y", UserStatus.APPROVED, detailsId);

        createCredentials(jdbcTemplate, "admin@ncl.sg", "ncl", userId, CredentialsStatus.ACTIVE);

        createRoles(jdbcTemplate, userId, Collections.singletonList(Role.ADMIN));

        addToTeam(jdbcTemplate, userId, teamId, MemberType.OWNER, MemberStatus.APPROVED);

        createDeterLabUser(jdbcTemplate, "ncl", userId);
    }

    private String createTeam(final JdbcTemplate jdbcTemplate, final String teamName, final String description, final String organizationType, final String url, final TeamPrivacy privacy, final TeamStatus status, final TeamVisibility visibility) throws SQLException {
        // insert the team
        final String id = randomUUID().toString();
        final ZonedDateTime time = ZonedDateTime.now();
        final int i = jdbcTemplate.update(SQL_INSERT_TEAMS, id, time, time, 0, time, description, teamName, organizationType, privacy.name(), status.name(), visibility.name(), url);
        log.info("Insert {} team entry", i);
        return id;
    }

    private int createAddress(final JdbcTemplate jdbcTemplate, final String address1, final String address2, final String city, final String country, final String region, final String zip) throws SQLException {
        // insert the address
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final int i = jdbcTemplate.update(c -> {
            final ZonedDateTime now = ZonedDateTime.now();
            final PreparedStatement statement = c.prepareStatement(SQL_INSERT_ADDRESS, new String[]{"id"});
            statement.setObject(1, now);
            statement.setObject(2, now);
            statement.setInt(3, 0);
            statement.setString(4, address1);
            statement.setString(5, address2);
            statement.setString(6, city);
            statement.setString(7, country);
            statement.setString(8, region);
            statement.setString(9, zip);
            return statement;
        }, keyHolder);
        final int id = keyHolder.getKey().intValue();
        log.info("Insert {} address entry with id = {}", i, id);
        return id;
    }

    private int createDetails(final JdbcTemplate jdbcTemplate, final String firstName, final String email, final int addressId, final String institution, final String abbreviation, final String url, final String jobTitle, final String lastName, final String phone) throws SQLException {
        // insert the userDetails
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final int i = jdbcTemplate.update(c -> {
            final ZonedDateTime now = ZonedDateTime.now();
            final PreparedStatement statement = c.prepareStatement(SQL_INSERT_USERS_DETAILS, new String[]{"id"});
            statement.setObject(1, now);
            statement.setObject(2, now);
            statement.setInt(3, 0);
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
        log.info("Insert {} details entry with id = {}", i, id);
        return id;
    }

    private String createUser(final JdbcTemplate jdbcTemplate, final String isEmailVerified, final UserStatus userStatus, final int detailsId) throws SQLException {
        // insert the users
        final String id = randomUUID().toString();
        final ZonedDateTime now = ZonedDateTime.now();
        final int i = jdbcTemplate.update(SQL_INSERT_USERS, id, now, now, 0, now, isEmailVerified, userStatus.name(), detailsId);
        log.info("Insert {} user entry", i);
        return id;
    }

    private void createCredentials(final JdbcTemplate jdbcTemplate, final String username, final String password, final String userId, final CredentialsStatus status) throws SQLException {
        // insert the credentials
        final ZonedDateTime now = ZonedDateTime.now();
        final int i = jdbcTemplate.update(SQL_INSERT_CREDENTIALS, userId, now, now, 0, passwordEncoder.encode(password), status.name(), username);
        log.info("Insert {} credentials entry", i);
    }

    private void createRoles(final JdbcTemplate jdbcTemplate, final String userId, final List<Role> roles) {
        roles.forEach(role -> jdbcTemplate.update(SQL_INSERT_CREDENTIALS_ROLES, userId, role.toString()));
    }

    // add user to team for both user side and team side
    private void addToTeam(final JdbcTemplate jdbcTemplate, String userId, String teamId, MemberType memberType, MemberStatus memberStatus) throws SQLException {
        // insert into user side
        final int i = jdbcTemplate.update(SQL_INSERT_USERS_TEAMS, userId, teamId);
        log.info("Insert {} user_team entry", i);

        // insert into team side (team members)
        final ZonedDateTime now = ZonedDateTime.now();
        final int j = jdbcTemplate.update(SQL_INSERT_TEAM_MEMBERS, now, now, 0, now, memberType.name(), userId, teamId, memberStatus.name());
        log.info("Insert {} team_member entry", j);
    }

    private void createDeterLabUser(final JdbcTemplate jdbcTemplate, final String name, final String userId) throws SQLException {
        // insert into deterlab_user
        final ZonedDateTime now = ZonedDateTime.now();
        final int i = jdbcTemplate.update(SQL_INSERT_DETERLAB_USER, now, now, 0, name, userId);
        log.info("Insert {} deter user entry", i);
    }

}
