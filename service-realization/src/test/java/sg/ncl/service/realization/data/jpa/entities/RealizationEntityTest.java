package sg.ncl.service.realization.data.jpa.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.domain.RealizationState;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 */
public class RealizationEntityTest {

    @Test
    public void testGetId() throws Exception {
        final RealizationEntity entity = new RealizationEntity();

        assertThat(entity.getId(), is(nullValue()));
    }

    @Test
    public void testSetId() throws Exception {
        final RealizationEntity entity = new RealizationEntity();
        final Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(id);

        assertThat(entity.getId(), is(id));
    }

    @Test
    public void testGetExperimentId() throws Exception {
        final RealizationEntity entity = new RealizationEntity();

        assertThat(entity.getExperimentId(), is(nullValue()));
    }

    @Test
    public void testSetExperimentId() throws Exception {
        final RealizationEntity entity = new RealizationEntity();
        final Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setExperimentId(id);

        assertThat(entity.getExperimentId(), is(id));
    }

    @Test
    public void testGetUserId() throws Exception {
        final RealizationEntity entity = new RealizationEntity();

        assertThat(entity.getUserId(), is(nullValue()));
    }

    @Test
    public void testSetUserId() throws Exception {
        final RealizationEntity entity = new RealizationEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setUserId(id);

        assertThat(entity.getUserId(), is(id));
    }

    @Test
    public void testGetTeamId() throws Exception {
        final RealizationEntity entity = new RealizationEntity();

        assertThat(entity.getTeamId(), is(nullValue()));
    }

    @Test
    public void testSetTeamId() throws Exception {
        final RealizationEntity entity = new RealizationEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setTeamId(id);

        assertThat(entity.getTeamId(), is(id));
    }

    @Test
    public void testGetNumberOfNodes() throws Exception {
        final RealizationEntity entity = new RealizationEntity();

        assertThat(entity.getNumberOfNodes(), is(nullValue()));
    }

    @Test
    public void testSetNumberOfNodes() throws Exception {
        final RealizationEntity entity = new RealizationEntity();
        final int number = Integer.parseInt(RandomStringUtils.randomNumeric(5));
        entity.setNumberOfNodes(number);

        assertThat(entity.getNumberOfNodes(), is(number));
    }

    @Test
    public void testGetState() throws Exception {
        final RealizationEntity entity = new RealizationEntity();

        assertThat(entity.getState(), is(RealizationState.STOP));
    }

    @Test
    public void testSetState() throws Exception {
        final RealizationEntity entity = new RealizationEntity();
        entity.setState(RealizationState.WARNING);

        assertThat(entity.getState(), is(RealizationState.WARNING));
    }

    @Test
    public void testGetIdleMinutes() throws Exception {
        final RealizationEntity entity = new RealizationEntity();

        assertThat(entity.getIdleMinutes(), is(nullValue()));
    }

    @Test
    public void testSetIdleMinutes() throws Exception {
        final RealizationEntity entity = new RealizationEntity();
        final long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setIdleMinutes(id);

        assertThat(entity.getIdleMinutes(), is(id));
    }

    @Test
    public void testGetRunningMinutes() throws Exception {
        final RealizationEntity entity = new RealizationEntity();

        assertThat(entity.getRunningMinutes(), is(nullValue()));
    }

    @Test
    public void testSetRunningMinutes() throws Exception {
        final RealizationEntity entity = new RealizationEntity();
        final long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setRunningMinutes(id);

        assertThat(entity.getRunningMinutes(), is(id));
    }
}