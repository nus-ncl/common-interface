package sg.ncl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.service.team.domain.MemberStatus;
import sg.ncl.service.team.domain.MemberType;
import sg.ncl.service.team.domain.TeamPrivacy;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.user.domain.UserStatus;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;

import static java.util.UUID.randomUUID;

/**
 * @author Te Ye
 */
@Component
@Slf4j
public class FirstRun {

    // FIXME: need to include users_roles

    static final String SQL_INSERT_TEAMS = "INSERT INTO teams"
            + "(id, created_date, last_modified_date, version, application_date, description, name, organisation_type, privacy, status, visibility, website) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    static final String SQL_INSERT_ADDRESS = "INSERT INTO addresses"
            + "(created_date, last_modified_date, version, address_1, address_2, city, country, region, zip_code) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    static final String SQL_INSERT_USERS_DETAILS = "INSERT INTO user_details"
            + "(created_date, last_modified_date, version, email, first_name, institution, institution_abbreviation, institution_web, job_title, last_name, phone, address_id) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    static final String SQL_INSERT_USERS = "INSERT INTO users"
            + "(id, created_date, last_modified_date, version, application_date, is_email_verified, status, user_details_id) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?)";
    static final String SQL_INSERT_CREDENTIALS = "INSERT INTO credentials"
            + "(id, created_date, last_modified_date, version, password, status, username) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?)";
    static final String SQL_INSERT_DETERLAB_USER = "INSERT INTO deterlab_user"
            + "(created_date, last_modified_date, version, deter_user_id, ncl_user_id) VALUES"
            + "(?, ?, ?, ?, ?)";
    static final String SQL_INSERT_USERS_TEAMS = "INSERT INTO users_teams"
            + "(user_id, team_id) VALUES"
            + "(?, ?)";
    static final String SQL_INSERT_TEAM_MEMBERS = "INSERT INTO team_members"
            + "(created_date, last_modified_date, version, joined_date, member_type, user_id, team_id, status) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Inject
    FirstRun(@NotNull final JdbcTemplate jdbcTemplate, @NotNull final PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }


    private String createTeam(String teamName, String description, String organizationType, String url, final TeamPrivacy privacy, final TeamStatus status, final TeamVisibility visibility) throws SQLException {
        // insert the team
        final String id = randomUUID().toString();
        final ZonedDateTime time = ZonedDateTime.now();
        final int i = jdbcTemplate.update(SQL_INSERT_TEAMS, id, time, time, 0, time, description, teamName, organizationType, privacy.name(), status.name(), visibility.name(), url);
        log.info("Insert {} team entry", i);
        return id;
    }

    private int createAddress(final String address1, final String address2, final String city, final String country, final String region, final String zip) throws SQLException {
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

    private int createDetails(String firstName, String email, int addressId, final String institution, final String abbreviation, final String url, final String jobTitle, final String lastName, final String phone) throws SQLException {
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

    private String createUser(final String isEmailVerified, final UserStatus userStatus, final int detailsId) throws SQLException {
        // insert the users
        final String id = randomUUID().toString();
        final ZonedDateTime now = ZonedDateTime.now();
        final int i = jdbcTemplate.update(SQL_INSERT_USERS, id, now, now, 0, now, isEmailVerified, userStatus.name(), detailsId);
        log.info("Insert {} user entry", i);
        return id;
    }

    private void createCredentials(final String username, final String password, final String userId, final CredentialsStatus status) throws SQLException {
        // insert the credentials
        final ZonedDateTime now = ZonedDateTime.now();
        final int i = jdbcTemplate.update(SQL_INSERT_CREDENTIALS, userId, now, now, 0, passwordEncoder.encode(password), status.name(), username);
        log.info("Insert {} credentials entry", i);
    }


    // add user to team for both user side and team side
    private void addToTeam(String userId, String teamId, MemberType memberType, MemberStatus memberStatus) throws SQLException {
        // insert into user side
        final int i = jdbcTemplate.update(SQL_INSERT_USERS_TEAMS, userId, teamId);
        log.info("Insert {} user_team entry", i);

        // insert into team side (team members)
        final ZonedDateTime now = ZonedDateTime.now();
        final int j = jdbcTemplate.update(SQL_INSERT_TEAM_MEMBERS, now, now, 0, now, memberType.name(), userId, teamId, memberStatus.name());
        log.info("Insert {} team_member entry", j);
    }

    private void createDeterLabUser(final String name, final String userId) throws SQLException {
        // insert into deterlab_user
        final ZonedDateTime now = ZonedDateTime.now();
        final int i = jdbcTemplate.update(SQL_INSERT_DETERLAB_USER, now, now, 0, name, userId);
        log.info("Insert {} deter user entry", i);
    }

    public void wipe() throws SQLException {
        final String[] tables = {
                "credentials",
                "deterlab_project",
                "deterlab_user",
                "experiments",
                "login_activities",
                "realizations",
                "registrations",
                "team_members",
                "teams",
                "users_teams",
                "users",
                "user_details",
                "addresses"
        };
        for (String table : tables) {
            final String sql = "DELETE FROM " + table;
            int i = jdbcTemplate.update(sql);
            log.info("Delete {} entry/entries from '{}' table", i, table);
        }
    }

    public void initialize() throws SQLException {
        log.info("Initializing database on first run");
        final String teamId = createTeam("NCL", "NCL Administrative Team", "Academic", "https://www.ncl.sg", TeamPrivacy.OPEN, TeamStatus.APPROVED, TeamVisibility.PRIVATE);

        final int addressId = createAddress("Address1", "", "City", "Country", "Region", "123456");

        final int detailsId = createDetails("First Name", "admin@ncl.sg", addressId, "Institution", "NCL", "https://www.ncl.sg", "Job Title", "Last Name", "12345678");

        final String userId = createUser("Y", UserStatus.APPROVED, detailsId);

        createCredentials("admin@ncl.sg", "ncl", userId, CredentialsStatus.ACTIVE);

        addToTeam(userId, teamId, MemberType.OWNER, MemberStatus.APPROVED);

        createDeterLabUser("ncl", userId);
    }

    public void reset() throws SQLException {
        wipe();
        initialize();
    }
}
