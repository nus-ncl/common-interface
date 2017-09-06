package sg.ncl.service.analytics.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class DataPublicDownloadEntityTest {

    @Test
    public void getIdTest() {
        final DataPublicDownloadEntity entity = new DataPublicDownloadEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void getDataIdTest() {
        final DataPublicDownloadEntity entity = new DataPublicDownloadEntity();
        assertThat(entity. getDataId()).isNull();
    }

    @Test
    public void getResourceIdTest() {
        final DataPublicDownloadEntity entity = new DataPublicDownloadEntity();
        assertThat(entity. getResourceId()).isNull();
    }

    @Test
    public void getDownloadDateTest() {
        final DataPublicDownloadEntity entity = new DataPublicDownloadEntity();
        assertThat(entity.getDownloadDate()).isNull();
    }

    @Test
    public void getPublicUserIdTest() {
        final DataPublicDownloadEntity entity = new DataPublicDownloadEntity();
        assertThat(entity.getPublicUserId()).isNull();
    }

    @Test
    public void setIdTest() {
        final DataPublicDownloadEntity entity = new DataPublicDownloadEntity();
        final Long randomId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(randomId);

        Long actual = entity.getId();
        Long expected = randomId;

        assertEquals(expected, actual);
    }

    @Test
    public void setDataIdTest() {
        final DataPublicDownloadEntity entity = new DataPublicDownloadEntity();
        final Long randomDataId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setDataId(randomDataId);

        Long actual = entity.getDataId();
        Long expected = randomDataId;

        assertEquals(expected, actual);
    }

    @Test
    public void setResourceIdTest() {
        final DataPublicDownloadEntity entity = new DataPublicDownloadEntity();
        final Long randomResourceId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setResourceId(randomResourceId);

        Long actual = entity.getResourceId();
        Long expected = randomResourceId;

        assertEquals(expected, actual);
    }

    @Test
    public void setDownloadDateTest() {
        final DataPublicDownloadEntity entity = new DataPublicDownloadEntity();
        final ZonedDateTime now = ZonedDateTime.now();
        entity.setDownloadDate(now);

        ZonedDateTime actual = entity.getDownloadDate();
        ZonedDateTime expected = now;

        assertEquals(expected, actual);
    }

    @Test
    public void setHashedUserId () {
        final DataPublicDownloadEntity entity = new DataPublicDownloadEntity();
        final Long randomPublicUserId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setPublicUserId(randomPublicUserId);

        Long actual = entity.getPublicUserId();
        Long expected = randomPublicUserId;

        assertEquals(expected, actual);
    }

    @Test
    public void toStringTest() {
        final DataPublicDownloadEntity entity = new DataPublicDownloadEntity();
        final Long randomId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        final Long randomDataId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        final Long randomResourceId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        final ZonedDateTime now = ZonedDateTime.now();
        final Long randomPublicUserId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(randomId);
        entity.setDataId(randomDataId);
        entity.setResourceId(randomResourceId);
        entity.setDownloadDate(now);
        entity.setPublicUserId(randomPublicUserId);

        String actual = entity.toString();
        String expected = "DataPublicDownloadEntity{" +
                "id='" + randomId + '\'' +
                ", data_id=" + randomDataId +
                ", resource_id=" + randomResourceId +
                ", download_date=" + now +
                ", public_user_id=" + randomPublicUserId +
                "} " ;

        assertThat(actual).contains(expected);
    }

}
