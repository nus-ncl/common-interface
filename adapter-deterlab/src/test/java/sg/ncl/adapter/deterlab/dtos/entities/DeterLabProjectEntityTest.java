package sg.ncl.adapter.deterlab.dtos.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Tran Ly Vu on 10/3/2016.
 */

public class DeterLabProjectEntityTest {
    private DeterLabProjectEntity deterLabUserEntity;

    @Before
    public void setup(){
        deterLabUserEntity=new DeterLabProjectEntity();
    }

    @Test
    public void testGetId(){
        final String expected = RandomStringUtils.randomAlphabetic(20);
        deterLabUserEntity.setId(expected);
        String actual=deterLabUserEntity.getId();

        assertThat(actual).isNotNull();
        assertEquals(expected,actual);
    }

    @Test
    public void testGetNclTeamId(){
        final String expected = RandomStringUtils.randomAlphabetic(20);
        deterLabUserEntity.setNclTeamId(expected);
        String actual=deterLabUserEntity.getNclTeamId();

        assertThat(actual).isNotNull();
        assertEquals(expected,actual);
    }

    @Test
    public void testGetDeterProjectId(){
        final String expected = RandomStringUtils.randomAlphabetic(20);
        deterLabUserEntity.setDeterProjectId(expected);
        String actual=deterLabUserEntity.getDeterProjectId();

        assertThat(actual).isNotNull();
        assertEquals(expected,actual);
    }


}
