package sg.ncl.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.service.team.domain.MemberStatus;
import sg.ncl.service.team.domain.MemberType;
import sg.ncl.service.team.domain.TeamPrivacy;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.user.domain.UserStatus;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static sg.ncl.service.FirstRun.SQL_INSERT_CREDENTIALS;
import static sg.ncl.service.FirstRun.SQL_INSERT_DETERLAB_USER;
import static sg.ncl.service.FirstRun.SQL_INSERT_TEAMS;
import static sg.ncl.service.FirstRun.SQL_INSERT_TEAM_MEMBERS;
import static sg.ncl.service.FirstRun.SQL_INSERT_USERS;
import static sg.ncl.service.FirstRun.SQL_INSERT_USERS_TEAMS;

/**
 * Created by chris on 8/19/2016.
 */
public class FirstRunTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private PasswordEncoder encoder;

    private FirstRun firstRun;

    @Before
    public void before() throws SQLException {
        firstRun = new FirstRun(jdbcTemplate, encoder);
    }

    @Test
    public void wipe() throws Exception {
        firstRun.wipe();

        verify(jdbcTemplate).update("DELETE FROM credentials");
        verify(jdbcTemplate).update("DELETE FROM deterlab_project");
        verify(jdbcTemplate).update("DELETE FROM deterlab_user");
        verify(jdbcTemplate).update("DELETE FROM experiments");
        verify(jdbcTemplate).update("DELETE FROM login_activities");
        verify(jdbcTemplate).update("DELETE FROM realizations");
        verify(jdbcTemplate).update("DELETE FROM registrations");
        verify(jdbcTemplate).update("DELETE FROM team_members");
        verify(jdbcTemplate).update("DELETE FROM teams");
        verify(jdbcTemplate).update("DELETE FROM users_teams");
        verify(jdbcTemplate).update("DELETE FROM users");
        verify(jdbcTemplate).update("DELETE FROM user_details");
        verify(jdbcTemplate).update("DELETE FROM addresses");
    }

    @Test
    public void initialize() throws Exception {
        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class))).then(i -> {
            final GeneratedKeyHolder keyHolder = i.getArgumentAt(1, GeneratedKeyHolder.class);
            final HashMap<String, Object> map = new HashMap<>();
            map.put("id", 1);
            keyHolder.getKeyList().add(map);
            return 1;
        });

        firstRun.initialize();

        verify(jdbcTemplate).update(eq(SQL_INSERT_TEAMS), anyString(), any(ZonedDateTime.class), any(ZonedDateTime.class), eq(0), any(ZonedDateTime.class), eq("NCL Administrative Team"), eq("NCL"), eq("Academic"), eq(TeamPrivacy.OPEN.name()), eq(TeamStatus.APPROVED.name()), eq(TeamVisibility.PRIVATE.name()), eq("https://www.ncl.sg"));
        // verify create address
        // verify create details
        verify(jdbcTemplate).update(eq(SQL_INSERT_USERS), anyString(), any(ZonedDateTime.class), any(ZonedDateTime.class), eq(0), any(ZonedDateTime.class), eq("Y"), eq(UserStatus.APPROVED.name()), anyInt());
        verify(jdbcTemplate).update(eq(SQL_INSERT_CREDENTIALS), anyString(), any(ZonedDateTime.class), any(ZonedDateTime.class), eq(0), anyString(), eq(CredentialsStatus.ACTIVE.name()), eq("admin@ncl.sg"));
        verify(jdbcTemplate).update(eq(SQL_INSERT_USERS_TEAMS), anyString(), anyString());
        verify(jdbcTemplate).update(eq(SQL_INSERT_TEAM_MEMBERS), any(ZonedDateTime.class), any(ZonedDateTime.class), eq(0), any(ZonedDateTime.class), eq(MemberType.OWNER.name()), anyString(), anyString(), eq(MemberStatus.APPROVED.name()));
        verify(jdbcTemplate).update(eq(SQL_INSERT_DETERLAB_USER), any(ZonedDateTime.class), any(ZonedDateTime.class), eq(0), eq("ncl"), anyString());
    }

}
