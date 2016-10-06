package sg.ncl.service.data.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.data.domain.DatasetResourceType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcszwang on 10/6/2016.
 */
public class DatasetResourceEntityTest {

    @Test
    public void testGetId() {
        DatasetResourceEntity entity = new DatasetResourceEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() {
        DatasetResourceEntity entity = new DatasetResourceEntity();
        String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setId(id);
        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetLink() {
        DatasetResourceEntity entity = new DatasetResourceEntity();
        assertThat(entity.getLink()).isNull();
    }

    @Test
    public void testSetLink() {
        DatasetResourceEntity entity = new DatasetResourceEntity();
        String link = RandomStringUtils.randomAlphanumeric(20);
        entity.setLink(link);
        assertThat(entity.getLink()).isEqualTo(link);
    }

    @Test
    public void testGetType() {
        DatasetResourceEntity entity = new DatasetResourceEntity();
        assertThat(entity.getType()).isEqualTo(DatasetResourceType.INTERNAL);
    }

    @Test
    public void testSetType() {
        DatasetResourceEntity entity = new DatasetResourceEntity();
        entity.setType(DatasetResourceType.EXTERNAL);
        assertThat(entity.getType()).isEqualTo(DatasetResourceType.EXTERNAL);
    }

    @Test
    public void testToString() {
        DatasetResourceEntity entity = new DatasetResourceEntity();
        entity.setId(RandomStringUtils.randomAlphanumeric(20));
        entity.setType(DatasetResourceType.EXTERNAL);
        entity.setLink(RandomStringUtils.randomAlphanumeric(20));

        final String toString = entity.toString();

        assertThat(toString).contains(entity.getId());
        assertThat(toString).contains(entity.getLink());
        assertThat(toString).contains(entity.getType().toString());
    }
}
