package sg.ncl.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Te Ye
 */
public class Seeder {

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/test";
//    private static final String DB_DRIVER = "org.h2.Driver";
//    private static final String DB_CONNECTION = "jdbc:h2:~/test";
    private static final String DB_USER = "root"; // use username: sa, password = "" if H2 database
    private static final String DB_PASSWORD= "root";
    private static final String TEAM_OWNER = "OWNER";
    private static final String TEAM_MEMBER = "MEMBER";
    private static final String TEAM_STATUS_APPROVED = "APPROVED";
    private static final String TEAM_STATUS_PENDING = "PENDING";
    private static final String TEAM_STATUS_REJECT = "REJECT";

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        try {
//             clearTables();
            // create 3 users
//            for (int i = 1; i < 4; i++) {
//                createUsers(i);
//            }
            createTeam("cassieProj");
            createTeam("ncl");
//            createTeam("disney1234");
//            createTeam("teyecom");
            createUser(6, "ncl", "ncl@nus.edu.sg", "deterinavm", "ncl", TEAM_OWNER, TEAM_STATUS_APPROVED);
            createUser(9, "pang pang", "cassie@gmail.com", "1", "cassieProj", TEAM_OWNER, TEAM_STATUS_APPROVED);
            createUser(10, "pang very", "cassie1@gmail.com", "1", "cassieProj", TEAM_MEMBER, TEAM_STATUS_APPROVED);
//            createUser(4, "mickey", "mickey@nus.edu.sg", "deterinavm2", "disney1234", TEAM_OWNER, TEAM_STATUS_APPROVED);
//            createUser(5, "goofy", "goofy@nus.edu.sg", "deterinavm", "disney1234", TEAM_MEMBER, TEAM_STATUS_APPROVED);
//            createUser(7, "elsa", "elsa@nus.edu.sg", "deterinavm", "ncl", TEAM_MEMBER, TEAM_STATUS_APPROVED);
//            createUser(8, "teyecom", "teyecom@nus.edu.sg", "deterinavm", "teyecom", TEAM_OWNER, TEAM_STATUS_APPROVED);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void clearTables() throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = getDBConnection();
            conn.prepareStatement("delete from credentials").execute();
            conn.prepareStatement("delete from addresses").execute();
            conn.prepareStatement("delete from user_details").execute();
            conn.prepareStatement("delete from users").execute();
            conn.prepareStatement("delete from users_teams").execute();
            conn.prepareStatement("delete from teams").execute();
            conn.prepareStatement("delete from team_members").execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    private static void createTeam(String teamName) throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        String insertTeamsTableSQL = "INSERT INTO teams"
                + "(id, created_date, last_modified_date, version, application_date, description, name, organisation_type, privacy, status, visibility, website) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        UUID uuid = UUID.randomUUID();

        try {
            conn = getDBConnection();

            // insert the teams
            preparedStatement = conn.prepareStatement(insertTeamsTableSQL);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setObject(2, ZonedDateTime.now());
            preparedStatement.setObject(3, ZonedDateTime.now());
            preparedStatement.setString(4, "0");
            preparedStatement.setObject(5, ZonedDateTime.now());
            preparedStatement.setString(6, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(7, teamName);
            preparedStatement.setString(8, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(9, "OPEN");
            preparedStatement.setString(10, "PENDING");
            preparedStatement.setString(11, "PUBLIC");
            preparedStatement.setString(12, "http://" + RandomStringUtils.randomAlphanumeric(8) + ".com");

            preparedStatement.execute();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    private static void createUser(int id, String name, String email, String password, String teamName, String memberType, String memberStatus) throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        String insertAddressTableSQL = "INSERT INTO addresses"
                + "(id, created_date, last_modified_date, version, address_1, address_2, city, country, region, zip_code) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertUsersDetailsTableSQL = "INSERT INTO user_details"
                + "(id, created_date, last_modified_date, version, email, first_name, institution, institution_abbreviation, institution_web, job_title, last_name, phone, address_id) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertUsersTableSQL = "INSERT INTO users"
                + "(id, created_date, last_modified_date, version, application_date, is_email_verified, status, user_details_id) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?)";

        UUID uuid = UUID.randomUUID();

        try {
            conn = getDBConnection();

            // insert the address
            preparedStatement = conn.prepareStatement(insertAddressTableSQL);
            preparedStatement.setInt(1, id);
            preparedStatement.setObject(2, ZonedDateTime.now());
            preparedStatement.setObject(3, ZonedDateTime.now());
            preparedStatement.setString(4, "0");
            preparedStatement.setString(5, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(6, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(7, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(8, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(9, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(10, RandomStringUtils.randomNumeric(20));

            preparedStatement.execute();

            // insert the userDetails
            preparedStatement = conn.prepareStatement(insertUsersDetailsTableSQL);
            preparedStatement.setInt(1, id); // id
            preparedStatement.setObject(2, ZonedDateTime.now());
            preparedStatement.setObject(3, ZonedDateTime.now());
            preparedStatement.setString(4, "0");
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, name);
            preparedStatement.setString(7, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(8, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(9, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(10, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(11, RandomStringUtils.randomAlphanumeric(8));
            preparedStatement.setString(12, RandomStringUtils.randomNumeric(8));
            preparedStatement.setInt(13, id); // address_id

            preparedStatement.execute();

            // insert the users
            preparedStatement = conn.prepareStatement(insertUsersTableSQL);

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setObject(2, ZonedDateTime.now());
            preparedStatement.setObject(3, ZonedDateTime.now());
            preparedStatement.setString(4, "0");
            preparedStatement.setObject(5, ZonedDateTime.now());
            preparedStatement.setString(6, "N");
            preparedStatement.setString(7, "PENDING");
            preparedStatement.setInt(8, id);

            preparedStatement.execute();

            // insert the credentials
            String insertCredTableSQL = "INSERT INTO credentials"
                    + "(id, created_date, last_modified_date, version, password, status, username) VALUES"
                    + "(?, ?, ?, ?, ?, ?, ?)";

            preparedStatement = conn.prepareStatement(insertCredTableSQL);

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setObject(2, ZonedDateTime.now());
            preparedStatement.setObject(3, ZonedDateTime.now());
            preparedStatement.setString(4, "0");
            preparedStatement.setString(5, passwordEncoder.encode(password));
            preparedStatement.setString(6, "ACTIVE");
            preparedStatement.setString(7, email);

            preparedStatement.execute();

            // insert into deterlab_user
            String insertDeterlabUserTableSQL = "INSERT INTO deterlab_user"
                    + "(id, created_date, last_modified_date, version, deter_user_id, ncl_user_id) VALUES"
                    + "(?, ?, ?, ?, ?, ?)";

            preparedStatement = conn.prepareStatement(insertDeterlabUserTableSQL);
            preparedStatement.setInt(1, id); // id
            preparedStatement.setObject(2, ZonedDateTime.now());
            preparedStatement.setObject(3, ZonedDateTime.now());
            preparedStatement.setString(4, "0");
            preparedStatement.setString(5, name);
            preparedStatement.setString(6, uuid.toString());

            preparedStatement.execute();

            joinTeam(uuid.toString(), teamName, memberType, memberStatus);

            // add to NCL
//            joinTeam(uuid.toString(), "NCL", TEAM_MEMBER, memberStatus);

            /**
             * Add mickey to team disney1234 as leader
             * Need to do for both user and team side
             */

            // select the team id
            /*
            String selectTeamsSQL = "SELECT id FROM teams WHERE name = ?";
            preparedStatement = conn.prepareStatement(selectTeamsSQL);
            preparedStatement.setString(1, teamName);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String teamId = rs.getString("id");

                // insert into user side
                String insertUsersTeamsTableSQL = "INSERT INTO users_teams"
                        + "(user_id, team_id) VALUES"
                        + "(?, ?)";
                preparedStatement = conn.prepareStatement(insertUsersTeamsTableSQL);
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setObject(2, teamId);
                preparedStatement.execute();

                // insert into team side (team members)
                String insertTeamMembersTableSQL = "INSERT INTO team_members"
                        + "(id, created_date, last_modified_date, version, joined_date, member_type, user_id, team_id) VALUES"
                        + "(?, ?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = conn.prepareStatement(insertTeamMembersTableSQL);
                preparedStatement.setLong(1, UUID.randomUUID().getMostSignificantBits());
                preparedStatement.setObject(2, ZonedDateTime.now());
                preparedStatement.setObject(3, ZonedDateTime.now());
                preparedStatement.setString(4, "0");
                preparedStatement.setObject(5, ZonedDateTime.now());
                preparedStatement.setString(6, memberType);
                preparedStatement.setString(7, uuid.toString());
                preparedStatement.setString(8, teamId);
                preparedStatement.execute();


            }
            */


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    private static void createUsers(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        String insertAddressTableSQL = "INSERT INTO addresses"
                + "(id, created_date, last_modified_date, version, address_1, address_2, city, country, region, zip_code) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertUsersDetailsTableSQL = "INSERT INTO user_details"
                + "(id, created_date, last_modified_date, version, email, first_name, institution, institution_abbreviation, institution_web, job_title, last_name, phone, address_id) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertUsersTableSQL = "INSERT INTO users"
                + "(id, created_date, last_modified_date, version, application_date, is_email_verified, status, user_details_id) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?)";

        UUID uuid = UUID.randomUUID();
        String email =  RandomStringUtils.randomAlphabetic(8).toLowerCase() + "@nus.edu.sg";

        try {
            conn = getDBConnection();

            // insert the address
            preparedStatement = conn.prepareStatement(insertAddressTableSQL);
            preparedStatement.setInt(1, id);
            preparedStatement.setObject(2, ZonedDateTime.now());
            preparedStatement.setObject(3, ZonedDateTime.now());
            preparedStatement.setString(4, "0");
            preparedStatement.setString(5, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(6, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(7, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(8, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(9, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(10, RandomStringUtils.randomNumeric(20));

            preparedStatement.execute();

            // insert the userDetails
            preparedStatement = conn.prepareStatement(insertUsersDetailsTableSQL);
            preparedStatement.setInt(1, id); // id
            preparedStatement.setObject(2, ZonedDateTime.now());
            preparedStatement.setObject(3, ZonedDateTime.now());
            preparedStatement.setString(4, "0");
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(7, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(8, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(9, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(10, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(11, RandomStringUtils.randomAlphanumeric(20));
            preparedStatement.setString(12, RandomStringUtils.randomNumeric(8));
            preparedStatement.setInt(13, id); // address_id

            preparedStatement.execute();

            // insert the users
            preparedStatement = conn.prepareStatement(insertUsersTableSQL);

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setObject(2, ZonedDateTime.now());
            preparedStatement.setObject(3, ZonedDateTime.now());
            preparedStatement.setString(4, "0");
            preparedStatement.setObject(5, ZonedDateTime.now());
            preparedStatement.setString(6, "N");
            preparedStatement.setString(7, "PENDING");
            preparedStatement.setInt(8, id);

            preparedStatement.execute();

            // insert the credentials
            String insertCredTableSQL = "INSERT INTO credentials"
                    + "(id, created_date, last_modified_date, version, password, status, username) VALUES"
                    + "(?, ?, ?, ?, ?, ?, ?)";

            preparedStatement = conn.prepareStatement(insertCredTableSQL);

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setObject(2, ZonedDateTime.now());
            preparedStatement.setObject(3, ZonedDateTime.now());
            preparedStatement.setString(4, "0");
            preparedStatement.setString(5, passwordEncoder.encode("a"));
            preparedStatement.setString(6, "ACTIVE");
            preparedStatement.setString(7, email);

            preparedStatement.execute();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static Connection getDBConnection() {
        Connection conn = null;

        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return conn;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return conn;
    }

    // add user to team for both user side and team side
    private static void joinTeam(String userId, String teamName, String memberType, String memberStatus) throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = getDBConnection();

            String selectTeamsSQL = "SELECT id FROM teams WHERE name = ?";
            preparedStatement = conn.prepareStatement(selectTeamsSQL);
            preparedStatement.setString(1, teamName);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String teamId = rs.getString("id");

                // insert into user side
                String insertUsersTeamsTableSQL = "INSERT INTO users_teams"
                        + "(user_id, team_id) VALUES"
                        + "(?, ?)";
                preparedStatement = conn.prepareStatement(insertUsersTeamsTableSQL);
                preparedStatement.setString(1, userId);
                preparedStatement.setObject(2, teamId);
                preparedStatement.execute();

                // insert into team side (team members)
                String insertTeamMembersTableSQL = "INSERT INTO team_members"
                        + "(id, created_date, last_modified_date, version, joined_date, member_type, user_id, team_id, status) VALUES"
                        + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = conn.prepareStatement(insertTeamMembersTableSQL);
                preparedStatement.setLong(1, UUID.randomUUID().getMostSignificantBits());
                preparedStatement.setObject(2, ZonedDateTime.now());
                preparedStatement.setObject(3, ZonedDateTime.now());
                preparedStatement.setString(4, "0");
                preparedStatement.setObject(5, ZonedDateTime.now());
                preparedStatement.setString(6, memberType);
                preparedStatement.setString(7, userId);
                preparedStatement.setString(8, teamId);
                preparedStatement.setString(9, memberStatus);
                preparedStatement.execute();

            }

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

}
