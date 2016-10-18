package sg.ncl.service.data.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcszwang on 10/6/2016.
 */
public class DataResourceEntityTest {

    @Test
    public void testGetId() {
        DataResourceEntity entity = new DataResourceEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() {
        DataResourceEntity entity = new DataResourceEntity();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(id);
        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetLink() {
        DataResourceEntity entity = new DataResourceEntity();
        assertThat(entity.getUri()).isNull();
    }

    @Test
    public void testSetLink() {
        DataResourceEntity entity = new DataResourceEntity();
        String link = RandomStringUtils.randomAlphanumeric(20);
        entity.setUri(link);
        assertThat(entity.getUri()).isEqualTo(link);
    }

    @Test
    public void testToString() {
        DataResourceEntity entity = new DataResourceEntity();
        entity.setId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setUri(RandomStringUtils.randomAlphanumeric(20));

        final String toString = entity.toString();

        assertThat(toString).contains(entity.getId().toString());
        assertThat(toString).contains(entity.getUri());
    }
}
