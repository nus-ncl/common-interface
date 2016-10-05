package sg.ncl.adapter.deterlab.dtos.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Tran Ly Vu
 */
public class DeterLabUserEntityTest {

    private DeterLabUserEntity deterLabUserEntity;

    @Before
    public void setup(){
        deterLabUserEntity=new DeterLabUserEntity();
    }

    @Test
    public void testGetId(){
        final Long expected = new Random().nextLong();
        deterLabUserEntity.setId(expected);

        Long actual=deterLabUserEntity.getId();

        assertThat(actual).isNotNull();
        assertEquals(expected,actual);
    }

    @Test
    public void testGetNclUserId(){
        final String expected = RandomStringUtils.randomAlphabetic(20);
        deterLabUserEntity.setNclUserId(expected);

        String actual=deterLabUserEntity.getNclUserId();

        assertThat(actual).isNotNull();
        assertEquals(expected,actual);
    }

    @Test
    public void testGetDeterUserId(){
        final String expected = RandomStringUtils.randomAlphabetic(20);
        deterLabUserEntity.setDeterUserId(expected);

        String actual=deterLabUserEntity.getDeterUserId();

        assertThat(actual).isNotNull();
        assertEquals(expected,actual);
    }


}
