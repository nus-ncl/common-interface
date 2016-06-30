package sg.ncl.service.experiment.data.jpa.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Create by Desmond
 */
public class ExperimentEntityTest {

    @Test
    public void testGetId() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();

        assertThat(entity.getId(), is(0L));
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
    public void testGetIdleSwap() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();

        assertThat(entity.getIdleSwap(), is(0));
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

        assertThat(entity.getMaxDuration(), is(0));
    }

    @Test
    public void testSetMaxDuration() throws Exception {
        final ExperimentEntity entity = new ExperimentEntity();
        final Integer maxDuration = RandomUtils.nextInt(100000000, 999999999);
        entity.setMaxDuration(maxDuration);

        assertThat(entity.getMaxDuration(), is(maxDuration));
    }
}