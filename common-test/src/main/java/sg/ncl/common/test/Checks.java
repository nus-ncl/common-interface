package sg.ncl.common.test;

import org.h2.jdbc.JdbcSQLException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 */
public class Checks {

    public static void checkException(final Exception e, final String message) {
        assertThat(e, is(instanceOf(DataIntegrityViolationException.class)));
        assertThat(e.getCause(), is(instanceOf(ConstraintViolationException.class)));
        assertThat(e.getCause().getCause(), is(instanceOf(JdbcSQLException.class)));
        assertThat(e.getCause().getCause().getMessage(), containsString(message));
    }

}
