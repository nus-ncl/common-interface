package sg.ncl.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;

import static org.mockito.Mockito.verify;

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
        firstRun.wipe();
    }

    @Test
    public void initialize() throws Exception {
        firstRun.initialize();
    }

}
