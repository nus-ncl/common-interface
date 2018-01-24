package sg.ncl.service.experiment.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcsyeoty.
 */
public class StatefulExperimentTest {

    @Test
    public void testGetId() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        assertThat(statefulExperiment.getId()).isNull();
    }

    @Test
    public void testSetId() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        final Long id = RandomUtils.nextLong(100000000, 999999999);
        statefulExperiment.setId(id);

        assertThat(statefulExperiment.getId()).isEqualTo(id);
    }

    @Test
    public void testGetUserId() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        assertThat(statefulExperiment.getUserId()).isNull();
    }

    @Test
    public void testSetUserId() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        String userId = RandomStringUtils.randomAlphanumeric(20);
        statefulExperiment.setUserId(userId);

        assertThat(statefulExperiment.getUserId()).isEqualTo(userId);
    }

    @Test
    public void testGetTeamId() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        assertThat(statefulExperiment.getTeamId()).isNull();
    }

    @Test
    public void testSetTeamId() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        String teamId = RandomStringUtils.randomAlphanumeric(20);
        statefulExperiment.setTeamId(teamId);

        assertThat(statefulExperiment.getTeamId()).isEqualTo(teamId);
    }

    @Test
    public void testGetTeamName() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        assertThat(statefulExperiment.getTeamName()).isNull();
    }

    @Test
    public void testSetTeamName() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        String teamName = RandomStringUtils.randomAlphanumeric(20);
        statefulExperiment.setTeamName(teamName);

        assertThat(statefulExperiment.getTeamName()).isEqualTo(teamName);
    }

    @Test
    public void testGetName() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        assertThat(statefulExperiment.getName()).isNull();
    }

    @Test
    public void testSetName() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        String name = RandomStringUtils.randomAlphanumeric(20);
        statefulExperiment.setName(name);

        assertThat(statefulExperiment.getName()).isEqualTo(name);
    }

    @Test
    public void testGetDescription() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        assertThat(statefulExperiment.getDescription()).isNull();
    }

    @Test
    public void testSetDescription() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        String description = RandomStringUtils.randomAlphanumeric(20);
        statefulExperiment.setDescription(description);

        assertThat(statefulExperiment.getDescription()).isEqualTo(description);
    }

    @Test
    public void testGetIdleHours() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        assertThat(statefulExperiment.getIdleHours()).isEqualTo(0L);
    }

    @Test
    public void testSetIdleHours() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        final Long hours = RandomUtils.nextLong(100000000, 999999999);
        statefulExperiment.setIdleHours(hours);

        assertThat(statefulExperiment.getIdleHours()).isEqualTo(hours);
    }

    @Test
    public void testGetMaxDuration() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        assertThat(statefulExperiment.getMaxDuration()).isEqualTo(0);
    }

    @Test
    public void testSetMaxDuration() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        final Integer maxDuration = RandomUtils.nextInt(100000000, 999999999);
        statefulExperiment.setMaxDuration(maxDuration);

        assertThat(statefulExperiment.getMaxDuration()).isEqualTo(maxDuration);
    }

    @Test
    public void testGetMinNodes() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        assertThat(statefulExperiment.getMinNodes()).isEqualTo(0);
    }

    @Test
    public void testSetMinNodes() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        final Integer minNodes = RandomUtils.nextInt(100000000, 999999999);
        statefulExperiment.setMinNodes(minNodes);

        assertThat(statefulExperiment.getMinNodes()).isEqualTo(minNodes);
    }

    @Test
    public void testGetNodes() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        assertThat(statefulExperiment.getNodes()).isEqualTo(0);
    }

    @Test
    public void testSetNodes() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        final Integer nodes = RandomUtils.nextInt(100000000, 999999999);
        statefulExperiment.setNodes(nodes);

        assertThat(statefulExperiment.getNodes()).isEqualTo(nodes);
    }

    @Test
    public void testGetState() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        assertThat(statefulExperiment.getState()).isNull();
    }

    @Test
    public void testSetState() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        String state = RandomStringUtils.randomAlphanumeric(20);
        statefulExperiment.setState(state);

        assertThat(statefulExperiment.getState()).isEqualTo(state);
    }

    @Test
    public void testGetDetails() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        assertThat(statefulExperiment.getDetails()).isNull();
    }

    @Test
    public void testSetDetails() throws Exception {
        final StatefulExperiment statefulExperiment = new StatefulExperiment();
        String details = RandomStringUtils.randomAlphanumeric(20);
        statefulExperiment.setDetails(details);

        assertThat(statefulExperiment.getDetails()).isEqualTo(details);
    }
}
