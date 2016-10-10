package sg.ncl.service.experiment.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.springframework.test.context.TestExecutionListeners;
import sg.ncl.service.experiment.util.Util;
import java.lang.Object;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Create by Desmond && Vu
 */
public class ExperimentEntityTest {

    @Test
    public void testGetId() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();

        assertThat(entity.getId(), is(nullValue()));
    }

    @Test
    public void testSetId() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();
        final Long id = RandomUtils.nextLong(100000000, 999999999);
        entity.setId(id);

        assertThat(entity.getId(), is(id));
    }

    @Test
    public void testGetUserId() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();

        assertThat(entity.getUserId(), is(nullValue()));
    }

    @Test
    public void testSetUserId() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        entity.setUserId(userId);

        assertThat(entity.getUserId(), is(userId));
    }

    @Test
    public void testGetTeamId() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();

        assertThat(entity.getTeamId(), is(nullValue()));
    }

    @Test
    public void testSetTeamId() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();
        final String teamId = RandomStringUtils.randomAlphanumeric(20);
        entity.setTeamId(teamId);

        assertThat(entity.getTeamId(), is(teamId));
    }

    @Test
    public void testGetTeamName() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();

        assertThat(entity.getTeamName(), is(nullValue()));
    }

    @Test
    public void testSetTeamName() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();
        final String name = RandomStringUtils.randomAlphanumeric(20);
        entity.setTeamName(name);

        assertThat(entity.getTeamName(), is(name));
    }

    @Test
    public void testGetName() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();

        assertThat(entity.getName(), is(nullValue()));
    }

    @Test
    public void testSetName() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();
        final String name = RandomStringUtils.randomAlphanumeric(20);
        entity.setName(name);

        assertThat(entity.getName(), is(name));
    }

    @Test
    public void testGetDescription() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();

        assertThat(entity.getDescription(), is(nullValue()));
    }

    @Test
    public void testSetDescription() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();
        final String description = RandomStringUtils.randomAlphanumeric(20);
        entity.setDescription(description);

        assertThat(entity.getDescription(), is(description));
    }

    @Test
    public void testGetNsFile() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();

        assertThat(entity.getNsFile(), is(nullValue()));
    }

    @Test
    public void testSetNsFile() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();
        final String nsFile = RandomStringUtils.randomAlphanumeric(20);
        entity.setNsFile(nsFile);

        assertThat(entity.getNsFile(), is(nsFile));
    }

    @Test
    public void testGetNsFileContent() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();

        assertThat(entity.getNsFileContent(), is(nullValue()));
    }

    @Test
    public void testSetNsFileContent() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();
        final String nsFile = RandomStringUtils.randomAlphanumeric(20);
        entity.setNsFileContent(nsFile);

        assertThat(entity.getNsFileContent(), is(nsFile));
    }

    @Test
    public void testGetIdleSwap() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();

        assertThat(entity.getIdleSwap(), is(nullValue()));
    }

    @Test
    public void testSetIdleSwap() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();
        final Integer idleSwap = RandomUtils.nextInt(100000000, 999999999);
        entity.setIdleSwap(idleSwap);

        assertThat(entity.getIdleSwap(), is(idleSwap));
    }

    @Test
    public void testGetMaxDuration() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();

        assertThat(entity.getMaxDuration(), is(nullValue()));
    }

    @Test
    public void testSetMaxDuration() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();
        final Integer maxDuration = RandomUtils.nextInt(100000000, 999999999);
        entity.setMaxDuration(maxDuration);

        assertThat(entity.getMaxDuration(), is(maxDuration));
    }

    @Test
    public void testEquals1() throws Exception {
        final ExperimentEntity entity=  new ExperimentEntity();
        final ExperimentEntity o = new ExperimentEntity();
        boolean expected= entity.equals(o);
        assertTrue(expected);
    }

    @Test
    public void testEquals2() throws Exception {
        final ExperimentEntity entity=  Util.getExperimentsEntity();
        boolean expected= entity.equals(null);
        assertFalse(expected);
    }

    @Test
    public void testEquals3() throws Exception   {
        final ExperimentEntity entity= new ExperimentEntity();
        int o = 123;
        boolean expected=entity.equals(o);
        assertFalse(expected);
    }


    @Test
    public void testEquals4() throws Exception {
        final ExperimentEntity entity= new ExperimentEntity();
        entity.setId(null);
        final ExperimentEntity o= new ExperimentEntity();
        o.setId(null);
        boolean expected=entity.equals(o);
        assertTrue(expected);
    }

    @Test
    public void testEquals5() throws Exception {
        final ExperimentEntity entity= new ExperimentEntity();
        entity.setId(12345L);
        ExperimentEntity o = Util.getExperimentsEntity();
        o.setId(123456789L);
        boolean expected=entity.equals(o);
        assertFalse(expected);
    }

    @Test
    public void testEquals6() throws Exception {
        final ExperimentEntity entity= new ExperimentEntity();
        entity.setId(12345L);
        final ExperimentEntity o = new ExperimentEntity();
        o.setId(1234L);
        boolean expected=entity.equals(o);
        assertFalse(expected);
    }

    @Test
    public void testEquals7() throws Exception {
        final ExperimentEntity entity= new ExperimentEntity();
        entity.setId(1234L);
        final ExperimentEntity o = new ExperimentEntity();
        o.setId(1234L);
        boolean expected=entity.equals(o);
        assertTrue(expected);
    }

}

