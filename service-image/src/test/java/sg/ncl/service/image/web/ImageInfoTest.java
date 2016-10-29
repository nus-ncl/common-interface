package sg.ncl.service.image.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.image.domain.ImageVisibility;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcsyeoty on 29-Oct-16.
 */
public class ImageInfoTest {

    private final Long id = Long.parseLong(RandomStringUtils.randomNumeric(15));
    private final String teamId = RandomStringUtils.randomAlphanumeric(20);
    private String imageName = RandomStringUtils.randomAlphanumeric(20);
    private String nodeId = RandomStringUtils.randomAlphanumeric(20);
    private String description = RandomStringUtils.randomAlphanumeric(20);
    private ImageVisibility visibility = ImageVisibility.PUBLIC;
    private final ImageInfo imageInfo = new ImageInfo(id, teamId, imageName, nodeId, description, visibility);

    @Test
    public void testGetId() throws Exception {
        assertThat(imageInfo.getId()).isEqualTo(id);
    }

    @Test
    public void testGetTeamId() throws Exception {
        assertThat(imageInfo.getTeamId()).isEqualTo(teamId);
    }

    @Test
    public void testGetImageName() throws Exception {
        assertThat(imageInfo.getImageName()).isEqualTo(imageName);
    }

    @Test
    public void testGetNodeId() throws Exception {
        assertThat(imageInfo.getNodeId()).isEqualTo(nodeId);
    }

    @Test
    public void testGetDescription() throws Exception {
        assertThat(imageInfo.getDescription()).isEqualTo(description);
    }

    @Test
    public void testGetVisibility() throws Exception {
        assertThat(imageInfo.getVisibility()).isEqualTo(visibility);
    }
}
