package sg.ncl.service.authentication;

import org.h2.jdbc.JdbcSQLException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({App.class, TestConfig.class})
@ActiveProfiles("authentication-service")
public abstract class AbstractTest extends AbstractTransactionalJUnit4SpringContextTests {

    protected void checkException(final Exception e, final String message) {
        assertThat(e, is(instanceOf(DataIntegrityViolationException.class)));
        assertThat(e.getCause(), is(instanceOf(ConstraintViolationException.class)));
        assertThat(e.getCause().getCause(), is(instanceOf(JdbcSQLException.class)));
        assertThat(e.getCause().getCause().getMessage(), containsString(message));
    }

    protected void checkException(final Exception e, final String startWithMessage, final String containsMessage) {
        assertThat(e, is(instanceOf(DataIntegrityViolationException.class)));
        assertThat(e.getCause(), is(instanceOf(ConstraintViolationException.class)));
        assertThat(e.getCause().getCause(), is(instanceOf(JdbcSQLException.class)));
        assertThat(e.getCause().getCause().getMessage(), allOf(startsWith(startWithMessage), containsString(containsMessage)));
    }

}
