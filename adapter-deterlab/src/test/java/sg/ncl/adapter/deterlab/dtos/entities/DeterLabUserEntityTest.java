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

    @Test
    public void testGetIdDedaultValue(){
        deterLabUserEntity=new DeterLabUserEntity();
        Long actual=deterLabUserEntity.getId();

        assertThat(actual).isNull();
    }
    @Test
    public void testSetId(){
        final Long expected = new Random().nextLong();

        deterLabUserEntity=new DeterLabUserEntity();
        deterLabUserEntity.setId(expected);
        Long actual=deterLabUserEntity.getId();
        
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetNclUserIdDedaultvalue(){
        deterLabUserEntity=new DeterLabUserEntity();

        String actual=deterLabUserEntity.getNclUserId();
        assertThat(actual).isNull();
    }

    @Test
    public void testGetNclUserId(){
        final String expected = RandomStringUtils.randomAlphabetic(20);

        deterLabUserEntity=new DeterLabUserEntity();
        deterLabUserEntity.setNclUserId(expected);
        String actual=deterLabUserEntity.getNclUserId();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetDeterUserIdDedaultvalue(){
        deterLabUserEntity=new DeterLabUserEntity();
        String actual=deterLabUserEntity.getDeterUserId();

        assertThat(actual).isNull();
    }

    @Test
    public void testGetDeterUserId(){
        final String expected = RandomStringUtils.randomAlphabetic(20);

        deterLabUserEntity=new DeterLabUserEntity();
        deterLabUserEntity.setDeterUserId(expected);
        String actual=deterLabUserEntity.getDeterUserId();

        assertThat(actual).isEqualTo(expected);
    }


}
