package sg.ncl.common.exception;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.common.exception.base.ConflictException;
import sg.ncl.common.exception.base.NotFoundException;
import sg.ncl.common.exception.base.NotModifiedException;
import sg.ncl.common.exception.base.UnauthorizedException;

import java.util.IllegalFormatException;

import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Chris on 8/7/2016.
 */
public class ExceptionHttpStatusMapTest {

    private ExceptionHttpStatusMap map;

    @Before
    public void before() {
        map = new ExceptionHttpStatusMap();
    }

    @Test
    public void testPut() throws Exception {
        map.put(IllegalArgumentException.class, HttpStatus.OK);

        assertThat(map.get(IllegalArgumentException.class), is(equalTo(HttpStatus.OK)));

        map.put(IllegalArgumentException.class, HttpStatus.CHECKPOINT);

        assertThat(map.get(IllegalArgumentException.class), is(equalTo(HttpStatus.CHECKPOINT)));
    }

    @Test
    public void testGet() throws Exception {
        assertThat(map.get(new BadRequestException()), is(equalTo(HttpStatus.BAD_REQUEST)));
        assertThat(map.get(BadRequestException.class), is(equalTo(HttpStatus.BAD_REQUEST)));
        assertThat(map.get(ConflictException.class), is(equalTo(HttpStatus.CONFLICT)));
        assertThat(map.get(NotFoundException.class), is(equalTo(HttpStatus.NOT_FOUND)));
        assertThat(map.get(NotModifiedException.class), is(equalTo(HttpStatus.NOT_MODIFIED)));
        assertThat(map.get(UnauthorizedException.class), is(equalTo(HttpStatus.UNAUTHORIZED)));

        assertThat(map.get(Exception.class), is(equalTo(HttpStatus.INTERNAL_SERVER_ERROR)));
        assertThat(map.get(IllegalArgumentException.class), is(equalTo(HttpStatus.INTERNAL_SERVER_ERROR)));

        map.put(IllegalArgumentException.class, HttpStatus.ACCEPTED);

        assertThat(map.get(IllegalFormatException.class), is(either(equalTo(HttpStatus.INTERNAL_SERVER_ERROR)).or(equalTo(HttpStatus.ACCEPTED))));
    }

    @Test
    public void testSize() throws Exception {
        assertThat(map.size(), is(equalTo(5)));

        map.put(IllegalArgumentException.class, HttpStatus.OK);

        assertThat(map.size(), is(equalTo(6)));

        map.put(IllegalArgumentException.class, HttpStatus.OK);

        assertThat(map.size(), is(equalTo(6)));
    }

}
