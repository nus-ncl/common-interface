package sg.ncl.common.test;

import org.h2.jdbc.JdbcSQLException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Christopher Zhong
 */
public class Checks {

    public static void checkException(final Exception e, final String message) {
        assertThat(e).isInstanceOf(DataIntegrityViolationException.class);
        assertThat(e.getCause()).isInstanceOf(ConstraintViolationException.class);
        assertThat(e.getCause().getCause()).isInstanceOf(JdbcSQLException.class);
        assertThat(e.getCause().getCause().getMessage()).contains(message);
    }

}
