package sg.ncl.service.data.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcszwang on 10/7/2016.
 */
public class DataStatisticsEntityTest {

    @Test
    public void testGetId() {
        final DataStatisticsEntity entity = new DataStatisticsEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() {
        final DataStatisticsEntity entity = new DataStatisticsEntity();
        final Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(id);

        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetUserId() {
        final DataStatisticsEntity entity = new DataStatisticsEntity();

        assertThat(entity.getUserId()).isNull();
    }

    @Test
    public void testSetUserId() {
        final DataStatisticsEntity entity = new DataStatisticsEntity();
        final String user = RandomStringUtils.randomAlphanumeric(20);
        entity.setUserId(user);

        assertThat(entity.getUserId()).isEqualTo(user);
    }

    @Test
    public void testGetDate() {
        final DataStatisticsEntity entity = new DataStatisticsEntity();

        assertThat(entity.getDate()).isNull();
    }

    @Test
    public void testSetDate() {
        final DataStatisticsEntity entity = new DataStatisticsEntity();
        ZonedDateTime date = ZonedDateTime.now();
        entity.setDate(date);

        assertThat(entity.getDate()).isEqualTo(date);
    }

    @Test
    public void testToString() {
        final DataStatisticsEntity entity = new DataStatisticsEntity();
        entity.setId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setUserId(RandomStringUtils.randomAlphanumeric(20));
        entity.setDate(ZonedDateTime.now());

        final String toString = entity.toString();

        assertThat(toString).contains(entity.getId().toString());
        assertThat(toString).contains(entity.getUserId());
        assertThat(toString).contains(entity.getDate().toString());
    }
}
