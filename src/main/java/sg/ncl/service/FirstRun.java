package sg.ncl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

import static java.util.UUID.randomUUID;

/**
 * @author Te Ye
 */
@Component
@Slf4j
public class FirstRun {

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
    private static final String SQL_INSERT_DETERLAB_USER = "INSERT INTO deterlab_user"
            + "(created_date, last_modified_date, version, deter_user_id, ncl_user_id) VALUES"
            + "(?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_USERS_TEAMS = "INSERT INTO users_teams"
            + "(user_id, team_id) VALUES"
            + "(?, ?)";
    private static final String SQL_INSERT_TEAM_MEMBERS = "INSERT INTO team_members"
            + "(created_date, last_modified_date, version, joined_date, member_type, user_id, team_id, status) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?)";

    private final DataSourceProperties properties;
    private final PasswordEncoder passwordEncoder;

    @Inject
    FirstRun(@NotNull final DataSourceProperties properties, @NotNull final PasswordEncoder passwordEncoder) {
        this.properties = properties;
        this.passwordEncoder = passwordEncoder;
    }


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(properties.getUrl(), properties.getUsername(), properties.getPassword());
    }

    private void wipeData(final Connection connection) throws SQLException {
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
            try (final PreparedStatement statement = connection.prepareStatement(sql)) {
                log.info("{}", statement);
                statement.execute();
            }
        }
    }

    private String createTeam(String teamName, String description, String organizationType, String url, final TeamPrivacy privacy, final TeamStatus status, final TeamVisibility visibility, final Connection connection) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(SQL_INSERT_TEAMS)) {
            // insert the team
            final String id = randomUUID().toString();
            statement.setString(1, id);
            statement.setObject(2, ZonedDateTime.now());
            statement.setObject(3, ZonedDateTime.now());
            statement.setInt(4, 0);
            statement.setObject(5, ZonedDateTime.now());
            statement.setString(6, description);
            statement.setString(7, teamName);
            statement.setString(8, organizationType);
            statement.setString(9, privacy.name());
            statement.setString(10, status.name());
            statement.setString(11, visibility.name());
            statement.setString(12, url);

            log.info("{}", statement);
            statement.execute();

            return id;
        }
    }

    private static int createAddress(final String address1, final String address2, final String city, final String country, final String region, final String zip, Connection connection) throws SQLException {
        // insert the address
        try (final PreparedStatement statement = connection.prepareStatement(SQL_INSERT_ADDRESS, new String[]{"id"})) {
            statement.setObject(1, ZonedDateTime.now());
            statement.setObject(2, ZonedDateTime.now());
            statement.setInt(3, 0);
            statement.setString(4, address1);
            statement.setString(5, address2);
            statement.setString(6, city);
            statement.setString(7, country);
            statement.setString(8, region);
            statement.setString(9, zip);

            log.info("{}", statement);
            statement.execute();

            final ResultSet result = statement.getGeneratedKeys();
            result.next();
            return result.getInt(1);
        }
    }

    private int createDetails(String firstName, String email, int addressId, Connection connection, final String institution, final String abbreviation, final String url, final String jobTitle, final String lastName, final String phone) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USERS_DETAILS, new String[]{"id"})) {
            // insert the userDetails
            statement.setObject(1, ZonedDateTime.now());
            statement.setObject(2, ZonedDateTime.now());
            statement.setString(3, "0");
            statement.setString(4, email);
            statement.setString(5, firstName);
            statement.setString(6, institution);
            statement.setString(7, abbreviation);
            statement.setString(8, url);
            statement.setString(9, jobTitle);
            statement.setString(10, lastName);
            statement.setString(11, phone);
            statement.setInt(12, addressId);

            log.info("{}", statement);
            statement.execute();

            final ResultSet result = statement.getGeneratedKeys();
            result.next();
            return result.getInt(1);
        }
    }

    private String createUser(final String isEmailVerified, final UserStatus userStatus, final int detailsId, final Connection connection) throws SQLException {
        // insert the users
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USERS)) {
            final String id = randomUUID().toString();
            statement.setString(1, id);
            statement.setObject(2, ZonedDateTime.now());
            statement.setObject(3, ZonedDateTime.now());
            statement.setInt(4, 0);
            statement.setObject(5, ZonedDateTime.now());
            statement.setString(6, isEmailVerified);
            statement.setString(7, userStatus.name());
            statement.setInt(8, detailsId);

            log.info("{}", statement);
            statement.execute();

            return id;
        }
    }

    private void createCredentials(final String username, final String password, final String userId, final CredentialsStatus status, final Connection connection) throws SQLException {
        // insert the credentials
        try (final PreparedStatement statement = connection.prepareStatement(SQL_INSERT_CREDENTIALS)) {
            statement.setString(1, userId);
            statement.setObject(2, ZonedDateTime.now());
            statement.setObject(3, ZonedDateTime.now());
            statement.setInt(4, 0);
            statement.setString(5, passwordEncoder.encode(password));
            statement.setString(6, status.name());
            statement.setString(7, username);

            log.info("{}", statement);
            statement.execute();
        }
    }


    // add user to team for both user side and team side
    private void addToTeam(String userId, String teamId, MemberType memberType, MemberStatus memberStatus, final Connection connection) throws SQLException {
        // insert into user side
        try (final PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USERS_TEAMS)) {
            statement.setString(1, userId);
            statement.setObject(2, teamId);

            log.info("{}", statement);
            statement.execute();
        }

        // insert into team side (team members)
        try (final PreparedStatement statement = connection.prepareStatement(SQL_INSERT_TEAM_MEMBERS)) {
            statement.setObject(1, ZonedDateTime.now());
            statement.setObject(2, ZonedDateTime.now());
            statement.setInt(3, 0);
            statement.setObject(4, ZonedDateTime.now());
            statement.setString(5, memberType.name());
            statement.setString(6, userId);
            statement.setString(7, teamId);
            statement.setString(8, memberStatus.name());

            log.info("{}", statement);
            statement.execute();
        }
    }

    private void createDeterLabUser(final String name, final String userId, final Connection connection) throws SQLException {
        // insert into deterlab_user
        try (final PreparedStatement statement = connection.prepareStatement(SQL_INSERT_DETERLAB_USER)) {
            statement.setObject(1, ZonedDateTime.now());
            statement.setObject(2, ZonedDateTime.now());
            statement.setInt(3, 0);
            statement.setString(4, name);
            statement.setString(5, userId);

            log.info("{}", statement);
            statement.execute();
        }
    }

    public void wipe() throws SQLException {
        try (final Connection connection = getConnection()) {
            wipeData(connection);
        }
    }

    public void initialize() throws SQLException {
        log.info("Initializing first initialize");
        try (final Connection connection = getConnection()) {
            final String teamId = createTeam("NCL", "NCL Administrative Team", "Academic", "https://www.ncl.sg", TeamPrivacy.OPEN, TeamStatus.APPROVED, TeamVisibility.PUBLIC, connection);

            final int addressId = createAddress("Address1", "", "City", "Country", "Region", "123456", connection);

            final int detailsId = createDetails("First Name", "admin@ncl.sg", addressId, connection, "Institution", "NCL", "https://www.ncl.sg", "Job Title", "Last Name", "12345678");

            final String userId = createUser("Y", UserStatus.APPROVED, detailsId, connection);

            createCredentials("admin@ncl.sg", "ncl", userId, CredentialsStatus.ACTIVE, connection);

            addToTeam(userId, teamId, MemberType.OWNER, MemberStatus.APPROVED, connection);

            createDeterLabUser("ncl", userId, connection);
        }
    }
}
