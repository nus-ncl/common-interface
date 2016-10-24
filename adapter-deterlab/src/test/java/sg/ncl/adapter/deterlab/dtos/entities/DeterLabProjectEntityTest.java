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


    @Test
    public void testGetIdDefaultValue(){
        deterLabUserEntity=new DeterLabProjectEntity();
        String actual=deterLabUserEntity.getId();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetId(){
        final String expected = RandomStringUtils.randomAlphabetic(20);

        deterLabUserEntity=new DeterLabProjectEntity();
        deterLabUserEntity.setId(expected);
        String actual=deterLabUserEntity.getId();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetNclTeamIdDefaultValue(){
        deterLabUserEntity=new DeterLabProjectEntity();
        String actual=deterLabUserEntity.getNclTeamId();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetNclTeamId(){
        final String expected = RandomStringUtils.randomAlphabetic(20);

        deterLabUserEntity=new DeterLabProjectEntity();
        deterLabUserEntity.setNclTeamId(expected);
        String actual=deterLabUserEntity.getNclTeamId();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGeteterProjectIdDefaultValue(){
        deterLabUserEntity=new DeterLabProjectEntity();
        String actual=deterLabUserEntity.getDeterProjectId();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetDeterProjectId(){
        final String expected = RandomStringUtils.randomAlphabetic(20);

        deterLabUserEntity=new DeterLabProjectEntity();
        deterLabUserEntity.setDeterProjectId(expected);
        String actual=deterLabUserEntity.getDeterProjectId();

        assertThat(actual).isEqualTo(expected);
    }


}
