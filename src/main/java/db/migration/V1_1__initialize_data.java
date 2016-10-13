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
import java.time.ZonedDateTime;

import static java.util.UUID.randomUUID;

/**
 * Created by dcszwang on 10/12/2016.
 */
@Slf4j
public class V1_1__initialize_data implements SpringJdbcMigration {

    static private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
    static final String SQL_INSERT_CREDENTIALS_ROLES = "INSERT INTO credentials_roles"
            + "(credentials_id, role) VALUES"
            + "(?, ?)";
    static final String SQL_INSERT_DETERLAB_USER = "INSERT INTO deterlab_user"
            + "(created_date, last_modified_date, version, deter_user_id, ncl_user_id) VALUES"
            + "(?, ?, ?, ?, ?)";
    static final String SQL_INSERT_USERS_TEAMS = "INSERT INTO users_teams"
            + "(user_id, team_id) VALUES"
            + "(?, ?)";
    static final String SQL_INSERT_TEAM_MEMBERS = "INSERT INTO team_members"
            + "(created_date, last_modified_date, version, joined_date, member_type, user_id, team_id, status) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?)";


    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {

        // insert the team
        final String teamId = randomUUID().toString();
        final ZonedDateTime time = ZonedDateTime.now();
        int i = jdbcTemplate.update(SQL_INSERT_TEAMS, teamId, time, time, 0, time, "NCL Administrative Team", "ncl", "Academic", TeamPrivacy.OPEN.name(), TeamStatus.APPROVED.name(), TeamVisibility.PRIVATE.name(), "https://www.ncl.sg");
        log.info("Insert {} team entry", i);

        // insert the address
        KeyHolder keyHolder = new GeneratedKeyHolder();
        i = jdbcTemplate.update(c -> {
            final ZonedDateTime now = ZonedDateTime.now();
            final PreparedStatement statement = c.prepareStatement(SQL_INSERT_ADDRESS, new String[]{"id"});
            statement.setObject(1, now);
            statement.setObject(2, now);
            statement.setInt(3, 0);
            statement.setString(4, "address1");
            statement.setString(5, "address2");
            statement.setString(6, "Singapore");
            statement.setString(7, "Singapore");
            statement.setString(8, "Singapore");
            statement.setString(9, "117417");
            return statement;
        }, keyHolder);
        final int addressId = keyHolder.getKey().intValue();
        log.info("Insert {} address entry with id = {}", i, addressId);

        // insert the user details
        keyHolder = new GeneratedKeyHolder();
        i = jdbcTemplate.update(c -> {
            final ZonedDateTime now = ZonedDateTime.now();
            final PreparedStatement statement = c.prepareStatement(SQL_INSERT_USERS_DETAILS, new String[]{"id"});
            statement.setObject(1, now);
            statement.setObject(2, now);
            statement.setInt(3, 0);
            statement.setString(4, "admin@ncl.sg");
            statement.setString(5, "Admin");
            statement.setString(6, "National Cybersecurity R&D Lab");
            statement.setString(7, "NCL");
            statement.setString(8, "https://ncl.sg");
            statement.setString(9, "Research Lab");
            statement.setString(10, "NCL");
            statement.setString(11, "88888888");
            statement.setInt(12, addressId);
            return statement;
        }, keyHolder);
        final int userDetailsId = keyHolder.getKey().intValue();
        log.info("Insert {} details entry with id = {}", i, userDetailsId);

        // insert the users
        final String userId = randomUUID().toString();
        ZonedDateTime now = ZonedDateTime.now();
        i = jdbcTemplate.update(SQL_INSERT_USERS, userId, now, now, 0, now, "Y", UserStatus.APPROVED.name(), userDetailsId);
        log.info("Insert {} user entry", i);

        // insert the credentials
        now = ZonedDateTime.now();
        i = jdbcTemplate.update(SQL_INSERT_CREDENTIALS, userId, now, now, 0, passwordEncoder.encode("ncl"), CredentialsStatus.ACTIVE.name(), "admin@ncl.sg");
        log.info("Insert {} credentials entry", i);

        // insert roles
        jdbcTemplate.update(SQL_INSERT_CREDENTIALS_ROLES, userId, Role.ADMIN.name());

        // add user to team (user side)
        i = jdbcTemplate.update(SQL_INSERT_USERS_TEAMS, userId, teamId);
        log.info("Insert {} user_team entry", i);

        // add user to team (team side)
        now = ZonedDateTime.now();
        int j = jdbcTemplate.update(SQL_INSERT_TEAM_MEMBERS, now, now, 0, now, MemberType.OWNER.name(), userId, teamId, MemberStatus.APPROVED.name());
        log.info("Insert {} team_member entry", j);

        // insert into deterlab_user
        now = ZonedDateTime.now();
        i = jdbcTemplate.update(SQL_INSERT_DETERLAB_USER, now, now, 0, "ncl", userId);
        log.info("Insert {} deter user entry", i);
    }


}
