package sg.ncl.service.analytic.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author: Tran Ly Vu
 */
public class DatasetDownloadEntityTest {

    @Test
    public void getIdTest() {
        final DatasetDownloadsEntity entity = new DatasetDownloadsEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void getDataIdTest() {
        final DatasetDownloadsEntity entity = new DatasetDownloadsEntity();
        assertThat(entity. getDataId()).isNull();
    }

    @Test
    public void getResourceIdTest() {
        final DatasetDownloadsEntity entity = new DatasetDownloadsEntity();
        assertThat(entity. getResourceId()).isNull();
    }

    @Test
    public void getDownloadDateTest() {
        final DatasetDownloadsEntity entity = new DatasetDownloadsEntity();
        assertThat(entity.getDownloadDate()).isNull();
    }

    @Test
    public void gethashedUserIdTest() {
        final DatasetDownloadsEntity entity = new DatasetDownloadsEntity();
        assertThat(entity.getHashedUserId()).isNull();
    }

    @Test
    public void setIdTest() {
        final DatasetDownloadsEntity entity = new DatasetDownloadsEntity();
        final Long randomId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(randomId);

        Long actual = entity.getId();
        Long expected = randomId;

        assertEquals(expected, actual);
    }

    @Test
    public void setDataIdTest() {
        final DatasetDownloadsEntity entity = new DatasetDownloadsEntity();
        final Long randomDataId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setDataId(randomDataId);

        Long actual = entity.getDataId();
        Long expected = randomDataId;

        assertEquals(expected, actual);
    }

    @Test
    public void setResourceIdTest() {
        final DatasetDownloadsEntity entity = new DatasetDownloadsEntity();
        final Long randomResourceId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setResourceId(randomResourceId);

        Long actual = entity.getResourceId();
        Long expected = randomResourceId;

        assertEquals(expected, actual);
    }

    @Test
    public void setDownloadDateTest() {
        final DatasetDownloadsEntity entity = new DatasetDownloadsEntity();
        final ZonedDateTime now = ZonedDateTime.now();
        entity.setDownloadDate(now);

        ZonedDateTime actual = entity.getDownloadDate();
        ZonedDateTime expected = now;

        assertEquals(expected, actual);
    }

    @Test
    public void setHashedUserId () {
        final DatasetDownloadsEntity entity = new DatasetDownloadsEntity();
        final  String randomString = RandomStringUtils.randomAlphanumeric(20);
        entity.setHashedUserId(randomString);

        String actual = entity.getHashedUserId();
        String expected = randomString;

        assertEquals(expected, actual);
    }

    @Test
    public void toStringTest() {
        final DatasetDownloadsEntity entity = new DatasetDownloadsEntity();
        final Long randomId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        final Long randomDataId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        final Long randomResourceId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        final ZonedDateTime now = ZonedDateTime.now();
        final  String randomString = RandomStringUtils.randomAlphanumeric(20);
        entity.setId(randomId);
        entity.setDataId(randomDataId);
        entity.setResourceId(randomResourceId);
        entity.setDownloadDate(now);
        entity.setHashedUserId(randomString);

        String actual = entity.toString();
        String expected = "DataEntity{" +
                "id='" + randomId + '\'' +
                ", data_id=" + randomDataId +
                ", resource_id=" + randomResourceId +
                ", date=" + now +
                ", hashed_user_id=" + randomString +
                "} " ;

        assertThat(actual).contains(expected);
    }

}
