package sg.ncl.service.team;

import org.springframework.web.util.NestedServletException;
import sg.ncl.service.team.exceptions.TeamNotFoundException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Te Ye
 */
public class Checks {

    public static void checkException(final Exception e, final String message) {
        assertThat(e, is(instanceOf(NestedServletException.class)));
        assertThat(e.getCause(), is(instanceOf(TeamNotFoundException.class)));
        assertThat(e.getCause().getMessage(), containsString(message));
    }

}
