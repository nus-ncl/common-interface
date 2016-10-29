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

    private DeterLabProjectEntity deterLabProjectEntity;


    @Test
    public void testGetIdDefaultValue(){
        deterLabProjectEntity=new DeterLabProjectEntity();
        Long actual=deterLabProjectEntity.getId();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetId(){
        final Long expected = Long.parseLong(RandomStringUtils.randomNumeric(15));

        deterLabProjectEntity=new DeterLabProjectEntity();
        deterLabProjectEntity.setId(expected);
        Long actual=deterLabProjectEntity.getId();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetNclTeamIdDefaultValue(){
        deterLabProjectEntity=new DeterLabProjectEntity();
        String actual=deterLabProjectEntity.getNclTeamId();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetNclTeamId(){
        final String expected = RandomStringUtils.randomAlphabetic(20);

        deterLabProjectEntity=new DeterLabProjectEntity();
        deterLabProjectEntity.setNclTeamId(expected);
        String actual=deterLabProjectEntity.getNclTeamId();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGeteterProjectIdDefaultValue(){
        deterLabProjectEntity=new DeterLabProjectEntity();
        String actual=deterLabProjectEntity.getDeterProjectId();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetDeterProjectId(){
        final String expected = RandomStringUtils.randomAlphabetic(20);

        deterLabProjectEntity=new DeterLabProjectEntity();
        deterLabProjectEntity.setDeterProjectId(expected);
        String actual=deterLabProjectEntity.getDeterProjectId();

        assertThat(actual).isEqualTo(expected);
    }
}
