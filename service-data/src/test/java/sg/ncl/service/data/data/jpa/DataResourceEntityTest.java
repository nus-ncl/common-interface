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

    @Test
    public void testEqualsNot() {
        DataResourceEntity entity = new DataResourceEntity();
        assertThat(entity.equals(null)).isFalse();
    }

    @Test
    public void testEqualsUri() {
        String link = RandomStringUtils.randomAlphanumeric(20);
        DataResourceEntity entity1 = new DataResourceEntity();
        DataResourceEntity entity2 = new DataResourceEntity();
        entity1.setUri(link);
        entity2.setUri(link);
        assertThat(entity1.equals(entity2)).isTrue();
    }

    @Test
    public void testHashCode() {
        String link = RandomStringUtils.randomAlphanumeric(20);
        DataResourceEntity entity = new DataResourceEntity();
        entity.setUri(link);
        assertThat(entity.hashCode()).isEqualTo(link.hashCode());
    }
}
