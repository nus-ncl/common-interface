package sg.ncl.service.user;

import org.h2.jdbc.JdbcSQLException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author Christopher Zhong
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {App.class, TestConfig.class})
@WebAppConfiguration
@ActiveProfiles("user-service")
public abstract class AbstractTest extends AbstractTransactionalJUnit4SpringContextTests {
    protected void checkException(final Exception e, final String message) {
        assertThat(e, is(instanceOf(DataIntegrityViolationException.class)));
        assertThat(e.getCause(), is(instanceOf(ConstraintViolationException.class)));
        assertThat(e.getCause().getCause(), is(instanceOf(JdbcSQLException.class)));
        assertThat(e.getCause().getCause().getMessage(), containsString(message));
    }
}
