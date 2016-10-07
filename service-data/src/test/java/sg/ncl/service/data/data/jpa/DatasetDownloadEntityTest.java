package sg.ncl.service.data.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcszwang on 10/7/2016.
 */
public class DatasetDownloadEntityTest {

    @Test
    public void testGetId() {
        final DatasetDownloadEntity entity = new DatasetDownloadEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() {
        final DatasetDownloadEntity entity = new DatasetDownloadEntity();
        final Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(id);

        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetUserId() {
        final DatasetDownloadEntity entity = new DatasetDownloadEntity();

        assertThat(entity.getUserId()).isNull();
    }

    @Test
    public void testSetUserId() {
        final DatasetDownloadEntity entity = new DatasetDownloadEntity();
        final String user = RandomStringUtils.randomAlphanumeric(20);
        entity.setUserId(user);

        assertThat(entity.getUserId()).isEqualTo(user);
    }

    @Test
    public void testGetDate() {
        final DatasetDownloadEntity entity = new DatasetDownloadEntity();

        assertThat(entity.getDate()).isNull();
    }

    @Test
    public void testSetDate() {
        final DatasetDownloadEntity entity = new DatasetDownloadEntity();
        ZonedDateTime date = ZonedDateTime.now();
        entity.setDate(date);

        assertThat(entity.getDate()).isEqualTo(date);
    }

    @Test
    public void testGetSuccess() {
        final DatasetDownloadEntity entity = new DatasetDownloadEntity();
        assertThat(entity.isSuccess()).isEqualTo(false);
    }

    @Test
    public void testSetSuccess() {
        final DatasetDownloadEntity entity = new DatasetDownloadEntity();
        entity.setSuccess(true);

        assertThat(entity.isSuccess()).isEqualTo(true);
    }

    @Test
    public void testToString() {
        final DatasetDownloadEntity entity = new DatasetDownloadEntity();
        entity.setId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setUserId(RandomStringUtils.randomAlphanumeric(20));
        entity.setDate(ZonedDateTime.now());
        entity.setSuccess(true);

        final String toString = entity.toString();

        assertThat(toString).contains(entity.getId().toString());
        assertThat(toString).contains(entity.getUserId());
        assertThat(toString).contains(entity.getDate().toString());
        assertThat(toString).contains(String.valueOf(entity.isSuccess()));
    }
}
