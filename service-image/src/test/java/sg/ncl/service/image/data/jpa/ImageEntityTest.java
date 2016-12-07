package sg.ncl.service.image.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.image.domain.ImageVisibility;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcsyeoty on 29-Oct-16.
 */
public class ImageEntityTest {

    @Test
    public void testGetId() throws Exception {
        final ImageEntity entity = new ImageEntity();

        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() throws Exception {
        final ImageEntity entity = new ImageEntity();
        final Long id = Long.parseLong(RandomStringUtils.randomNumeric(15));
        entity.setId(id);

        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetTeamId() throws Exception {
        final ImageEntity entity = new ImageEntity();

        assertThat(entity.getTeamId()).isNull();
    }

    @Test
    public void testSetTeamId() throws Exception {
        final ImageEntity entity = new ImageEntity();
        final String id = RandomStringUtils.randomAlphanumeric(15);
        entity.setTeamId(id);

        assertThat(entity.getTeamId()).isEqualTo(id);
    }

    @Test
    public void testGetNodeId() throws Exception {
        final ImageEntity entity = new ImageEntity();

        assertThat(entity.getNodeId()).isNull();
    }

    @Test
    public void testSetNodeId() throws Exception {
        final ImageEntity entity = new ImageEntity();
        final String id = RandomStringUtils.randomAlphanumeric(15);
        entity.setNodeId(id);

        assertThat(entity.getNodeId()).isEqualTo(id);
    }

    @Test
    public void testGetImageName() throws Exception {
        final ImageEntity entity = new ImageEntity();

        assertThat(entity.getImageName()).isNull();
    }

    @Test
    public void testSetImageName() throws Exception {
        final ImageEntity entity = new ImageEntity();
        final String name = RandomStringUtils.randomAlphanumeric(15);
        entity.setImageName(name);

        assertThat(entity.getImageName()).isEqualTo(name);
    }

    @Test
    public void testGetDescription() throws Exception {
        final ImageEntity entity = new ImageEntity();

        assertThat(entity.getDescription()).isNull();
    }

    @Test
    public void testSetDescription() throws Exception {
        final ImageEntity entity = new ImageEntity();
        final String one = RandomStringUtils.randomAlphanumeric(15);
        entity.setDescription(one);

        assertThat(entity.getDescription()).isEqualTo(one);
    }

    @Test
    public void testGetVisibility() throws Exception {
        final ImageEntity entity = new ImageEntity();

        assertThat(entity.getVisibility()).isEqualTo(ImageVisibility.PRIVATE);
    }

    @Test
    public void testSetVisibility() throws Exception {
        final ImageEntity entity = new ImageEntity();
        entity.setVisibility(ImageVisibility.PUBLIC);

        assertThat(entity.getVisibility()).isEqualTo(ImageVisibility.PUBLIC);
    }

    @Test
    public void testGetCurrentOS() throws Exception {
        final ImageEntity entity = new ImageEntity();

        assertThat(entity.getCurrentOS()).isNull();
    }

    @Test
    public void testSetCurrentOS() throws Exception {
        final ImageEntity entity = new ImageEntity();
        final String one = RandomStringUtils.randomAlphanumeric(15);
        entity.setCurrentOS(one);

        assertThat(entity.getCurrentOS()).isEqualTo(one);
    }
}
