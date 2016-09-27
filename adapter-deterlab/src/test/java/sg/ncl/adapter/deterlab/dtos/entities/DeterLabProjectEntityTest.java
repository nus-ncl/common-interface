package sg.ncl.adapter.deterlab.dtos.entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

/**
 * Created by Vu on 9/26/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DeterLabUserEntity.class)
public class DeterLabProjectEntityTest {
    @Test
    public void setterAndGetterTest (){
        DeterLabProjectEntity entity= new DeterLabProjectEntity();
        entity.setId("test1");
        entity.setNclTeamId("test2");
        entity.setDeterProjectId("test3");
        String actual1=  entity.getId();
        String actual2=  entity.getNclTeamId();
        String actual3=  entity.getDeterProjectId();
        assertEquals("test1", actual1);
        assertEquals("test2", actual2);
        assertEquals("test3", actual3);

    }

}
