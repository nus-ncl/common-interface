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
    public void getterAndSetterTest() {
        final Long firstRandom = new Random().nextLong();
        deterLabUserEntity.setId(firstRandom);

        final String secondRandom = RandomStringUtils.randomAlphabetic(20);
        deterLabUserEntity.setNclUserId(secondRandom);

        final String thirdRandom = RandomStringUtils.randomAlphabetic(20);
        deterLabUserEntity.setDeterUserId(thirdRandom);

        Long actual1=deterLabUserEntity.getId();
        String actual2=deterLabUserEntity.getNclUserId();
        String actual3=deterLabUserEntity.getDeterUserId();

        assertThat(actual1).isNotNull();
        assertThat(actual2).isNotNull();
        assertThat(actual3).isNotNull();

        assertEquals(firstRandom,actual1);
        assertEquals(secondRandom,actual2);
        assertEquals(thirdRandom,actual3);
    }
}
