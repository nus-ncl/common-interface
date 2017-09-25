package sg.ncl.service.experiment.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcsyeoty on 25-Sep-17.
 */
public class ExperimentInfoTest {

    private final Long id = Long.parseLong(RandomStringUtils.randomNumeric(2));
    private final String userId = RandomStringUtils.randomAlphanumeric(20);
    private final String teamId = RandomStringUtils.randomAlphanumeric(20);
    private final String teamName = RandomStringUtils.randomAlphanumeric(20);
    private final String name = RandomStringUtils.randomAlphanumeric(20);
    private final String description = RandomStringUtils.randomAlphanumeric(20);
    private final String nsFile = RandomStringUtils.randomAlphanumeric(20);
    private final String nsFileContent = RandomStringUtils.randomAlphanumeric(20);
    private final Integer idleSwap =  Integer.parseInt(RandomStringUtils.randomNumeric(2));
    private final Integer maxDuration = Integer.parseInt(RandomStringUtils.randomNumeric(2));
    private final ZonedDateTime createdDate = ZonedDateTime.now();
    private final ZonedDateTime lastModifiedDate = ZonedDateTime.now();

    private final ExperimentInfo experimentInfo = new ExperimentInfo(id, userId, teamId, teamName, name, description, nsFile, nsFileContent, idleSwap, maxDuration, createdDate, lastModifiedDate);

    @Test
    public void testGetId() throws Exception {
        assertThat(experimentInfo.getId()).isEqualTo(id);
    }

    @Test
    public void testGetUserId() throws Exception {
        assertThat(experimentInfo.getUserId()).isEqualTo(userId);
    }

    @Test
    public void testGetTeamId() throws Exception {
        assertThat(experimentInfo.getTeamId()).isEqualTo(teamId);
    }

    @Test
    public void testGetTeamName() throws Exception {
        assertThat(experimentInfo.getTeamName()).isEqualTo(teamName);
    }

    @Test
    public void testGetName() throws Exception {
        assertThat(experimentInfo.getName()).isEqualTo(name);
    }

    @Test
    public void testGetDescription() throws Exception {
        assertThat(experimentInfo.getDescription()).isEqualTo(description);
    }

    @Test
    public void testGetNSFile() throws Exception {
        assertThat(experimentInfo.getNsFile()).isEqualTo(nsFile);
    }

    @Test
    public void testGetNSFileContent() throws Exception {
        assertThat(experimentInfo.getNsFileContent()).isEqualTo(nsFileContent);
    }

    @Test
    public void testGetIdleSwap() throws Exception {
        assertThat(experimentInfo.getIdleSwap()).isEqualTo(idleSwap);
    }

    @Test
    public void testGetMaxDuration() throws Exception {
        assertThat(experimentInfo.getMaxDuration()).isEqualTo(maxDuration);
    }

    @Test
    public void testGetCreatedDate() throws Exception {
        assertThat(experimentInfo.getCreatedDate()).isEqualTo(createdDate);
    }

    @Test
    public void testGetLastModifiedDate() throws Exception {
        assertThat(experimentInfo.getLastModifiedDate()).isEqualTo(lastModifiedDate);
    }
}
