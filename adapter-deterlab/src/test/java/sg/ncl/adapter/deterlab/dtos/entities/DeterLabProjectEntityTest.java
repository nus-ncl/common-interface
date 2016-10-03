package sg.ncl.adapter.deterlab.dtos.entities;

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
    public void getterAndSetterTest(){
        deterLabUserEntity.setId("test1");
        deterLabUserEntity.setNclTeamId("test2");
        deterLabUserEntity.setDeterProjectId("test3");

        String actual1=deterLabUserEntity.getId();
        String actual2=deterLabUserEntity.getNclTeamId();
        String actual3=deterLabUserEntity.getDeterProjectId();

        assertThat(actual1).isNotNull();
        assertThat(actual2).isNotNull();
        assertThat(actual3).isNotNull();

        assertEquals("test1",actual1);
        assertEquals("test2",actual2);
        assertEquals("test3",actual3);
    }

}
