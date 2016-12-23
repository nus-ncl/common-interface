package sg.ncl.service.analytics.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.analytics.data.jpa.DataDownloadsEntity;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author: Tran Ly Vu
 */
public class DataDownloadEntityTest {

    @Test
    public void getIdTest() {
        final DataDownloadsEntity entity = new DataDownloadsEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void getDataIdTest() {
        final DataDownloadsEntity entity = new DataDownloadsEntity();
        assertThat(entity. getDataId()).isNull();
    }

    @Test
    public void getResourceIdTest() {
        final DataDownloadsEntity entity = new DataDownloadsEntity();
        assertThat(entity. getResourceId()).isNull();
    }

    @Test
    public void getDownloadDateTest() {
        final DataDownloadsEntity entity = new DataDownloadsEntity();
        assertThat(entity.getDownloadDate()).isNull();
    }

    @Test
    public void gethashedUserIdTest() {
        final DataDownloadsEntity entity = new DataDownloadsEntity();
        assertThat(entity.getHashedUserId()).isNull();
    }

    @Test
    public void setIdTest() {
        final DataDownloadsEntity entity = new DataDownloadsEntity();
        final Long randomId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(randomId);

        Long actual = entity.getId();
        Long expected = randomId;

        assertEquals(expected, actual);
    }

    @Test
    public void setDataIdTest() {
        final DataDownloadsEntity entity = new DataDownloadsEntity();
        final Long randomDataId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setDataId(randomDataId);

        Long actual = entity.getDataId();
        Long expected = randomDataId;

        assertEquals(expected, actual);
    }

    @Test
    public void setResourceIdTest() {
        final DataDownloadsEntity entity = new DataDownloadsEntity();
        final Long randomResourceId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setResourceId(randomResourceId);

        Long actual = entity.getResourceId();
        Long expected = randomResourceId;

        assertEquals(expected, actual);
    }

    @Test
    public void setDownloadDateTest() {
        final DataDownloadsEntity entity = new DataDownloadsEntity();
        final ZonedDateTime now = ZonedDateTime.now();
        entity.setDownloadDate(now);

        ZonedDateTime actual = entity.getDownloadDate();
        ZonedDateTime expected = now;

        assertEquals(expected, actual);
    }

    @Test
    public void setHashedUserId () {
        final DataDownloadsEntity entity = new DataDownloadsEntity();
        final  String randomString = RandomStringUtils.randomAlphanumeric(20);
        entity.setHashedUserId(randomString);

        String actual = entity.getHashedUserId();
        String expected = randomString;

        assertEquals(expected, actual);
    }

    @Test
    public void toStringTest() {
        final DataDownloadsEntity entity = new DataDownloadsEntity();
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
                ", download_date=" + now +
                ", hashed_user_id=" + randomString +
                "} " ;

        assertThat(actual).contains(expected);
    }

}
