package sg.ncl.adapter.deterlab.dtos.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Te Ye
 */
public class DeterLabUserEntityTest {

    @Test
    public void testGetId() throws Exception {
        final DeterLabUserEntity deterLabUserEntity = new DeterLabUserEntity();

        assertThat(deterLabUserEntity.getId(), is(nullValue()));
    }

    @Test
    public void testSetId() throws Exception {
        final DeterLabUserEntity deterLabUserEntity = new DeterLabUserEntity();
        final String one = RandomStringUtils.randomAlphabetic(20);
        deterLabUserEntity.setId(one);

        assertThat(deterLabUserEntity.getId(), is(equalTo(one)));
    }

    @Test
    public void testGetNclId() throws Exception {
        final DeterLabUserEntity deterLabUserEntity = new DeterLabUserEntity();

        assertThat(deterLabUserEntity.getNclUserId(), is(nullValue()));
    }

    @Test
    public void testSetNclId() throws Exception {
        final DeterLabUserEntity deterLabUserEntity = new DeterLabUserEntity();
        final String one = RandomStringUtils.randomAlphabetic(20);
        deterLabUserEntity.setNclUserId(one);

        assertThat(deterLabUserEntity.getNclUserId(), is(equalTo(one)));
    }

    @Test
    public void testGetDeterUserId() throws Exception {
        final DeterLabUserEntity deterLabUserEntity = new DeterLabUserEntity();

        assertThat(deterLabUserEntity.getDeterUserId(), is(nullValue()));
    }

    @Test
    public void testSetDeterUserId() throws Exception {
        final DeterLabUserEntity deterLabUserEntity = new DeterLabUserEntity();
        final String one = RandomStringUtils.randomAlphabetic(20);
        deterLabUserEntity.setDeterUserId(one);

        assertThat(deterLabUserEntity.getDeterUserId(), is(equalTo(one)));
    }

}
