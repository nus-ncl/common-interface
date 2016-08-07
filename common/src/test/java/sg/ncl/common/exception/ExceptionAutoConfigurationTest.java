package sg.ncl.common.exception;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static sg.ncl.common.exception.ExceptionHttpStatusMap.DEFAULT_HTTP_STATUS;

/**
 * Created by Chris on 8/7/2016.
 */
public class ExceptionAutoConfigurationTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private ExceptionProperties properties;

    private ExceptionAutoConfiguration configuration;

    @Before
    public void before() {
        configuration = new ExceptionAutoConfiguration(properties);
    }

    @Test
    public void testExceptionHttpStatusMapWithStatusCode() throws Exception {
        final Class<IllegalArgumentException> clazz = IllegalArgumentException.class;
        final HttpStatus status = HttpStatus.OK;
        final Map<String, String> m = new HashMap<String, String>() {{
            put(clazz.getName(), status.toString());
        }};
        doReturn(m).when(properties).getMappings();

        final ExceptionHttpStatusMap map = configuration.exceptionHttpStatusMap();

        assertThat(map.size(), is(equalTo(6)));
        assertThat(map.get(clazz), is(equalTo(status)));
    }

    @Test
    public void testExceptionHttpStatusMapWithName() throws Exception {
        final Class<IllegalArgumentException> clazz = IllegalArgumentException.class;
        final HttpStatus status = HttpStatus.OK;
        final Map<String, String> m = new HashMap<String, String>() {{
            put(clazz.getName(), status.name());
        }};
        doReturn(m).when(properties).getMappings();

        final ExceptionHttpStatusMap map = configuration.exceptionHttpStatusMap();

        assertThat(map.size(), is(equalTo(6)));
        assertThat(map.get(clazz), is(equalTo(status)));
    }

    @Test
    public void testExceptionHttpStatusMapWithUnknownException() throws Exception {
        final String clazz = "unknown";
        final HttpStatus status = HttpStatus.OK;
        final Map<String, String> m = new HashMap<String, String>() {{
            put(clazz, status.name());
        }};
        doReturn(m).when(properties).getMappings();

        final ExceptionHttpStatusMap map = configuration.exceptionHttpStatusMap();

        assertThat(map.size(), is(equalTo(5)));
    }

    @Test
    public void testExceptionHttpStatusMapWithOneGoodNumberAndOneGoodName() throws Exception {
        final Class<IllegalArgumentException> clazz1 = IllegalArgumentException.class;
        final HttpStatus status1 = HttpStatus.OK;
        final Class<InvalidPropertiesFormatException> clazz2 = InvalidPropertiesFormatException.class;
        final HttpStatus status2 = HttpStatus.ACCEPTED;
        final Map<String, String> m = new HashMap<String, String>() {{
            put(clazz1.getName(), status1.toString());
            put(clazz2.getName(), status2.name());
        }};
        doReturn(m).when(properties).getMappings();

        final ExceptionHttpStatusMap map = configuration.exceptionHttpStatusMap();

        assertThat(map.size(), is(equalTo(7)));
        assertThat(map.get(clazz1), is(equalTo(status1)));
        assertThat(map.get(clazz2), is(equalTo(status2)));
    }

    @Test
    public void testExceptionHttpStatusMapWithBadNumber() throws Exception {
        final Class<IllegalArgumentException> clazz = IllegalArgumentException.class;
        final String status = "0";
        final Map<String, String> m = new HashMap<String, String>() {{
            put(clazz.getName(), status);
        }};
        doReturn(m).when(properties).getMappings();

        final ExceptionHttpStatusMap map = configuration.exceptionHttpStatusMap();

        assertThat(map.size(), is(equalTo(5)));
        assertThat(map.get(clazz), is(equalTo(DEFAULT_HTTP_STATUS)));
        assertThat(map.size(), is(equalTo(6)));
    }

    @Test
    public void testExceptionHttpStatusMapWithBadName() throws Exception {
        final Class<IllegalArgumentException> clazz = IllegalArgumentException.class;
        final String status = "bad";
        final Map<String, String> m = new HashMap<String, String>() {{
            put(clazz.getName(), status);
        }};
        doReturn(m).when(properties).getMappings();

        final ExceptionHttpStatusMap map = configuration.exceptionHttpStatusMap();

        assertThat(map.size(), is(equalTo(5)));
        assertThat(map.get(clazz), is(equalTo(DEFAULT_HTTP_STATUS)));
        assertThat(map.size(), is(equalTo(6)));
    }

    @Test
    public void testExceptionHttpStatusMapWithOneBadNumberAndOneGoodName() throws Exception {
        final Class<IllegalArgumentException> clazz1 = IllegalArgumentException.class;
        final String status1 = "0";
        final Class<InvalidPropertiesFormatException> clazz2 = InvalidPropertiesFormatException.class;
        final HttpStatus status2 = HttpStatus.ACCEPTED;
        final Map<String, String> m = new HashMap<String, String>() {{
            put(clazz1.getName(), status1);
            put(clazz2.getName(), status2.name());
        }};
        doReturn(m).when(properties).getMappings();

        final ExceptionHttpStatusMap map = configuration.exceptionHttpStatusMap();

        assertThat(map.size(), is(equalTo(6)));
        assertThat(map.get(clazz1), is(equalTo(DEFAULT_HTTP_STATUS)));
        assertThat(map.get(clazz2), is(equalTo(status2)));
    }

}
