package sg.ncl.service.data.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.data.util.TestUtil;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcsjnh on 12/23/2016.
 */
public class DataAccessRequestEntityTest {

    @Test
    public void testGetId() {
        DataAccessRequestEntity entity = new DataAccessRequestEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() {
        DataAccessRequestEntity entity = new DataAccessRequestEntity();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(id);
        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetDataId() {
        DataAccessRequestEntity entity = new DataAccessRequestEntity();
        assertThat(entity.getDataId()).isNull();
    }

    @Test
    public void testSetDataId() {
        DataAccessRequestEntity entity = new DataAccessRequestEntity();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setDataId(id);
        assertThat(entity.getDataId()).isEqualTo(id);
    }

    @Test
    public void testGetRequesterId() {
        DataAccessRequestEntity entity = new DataAccessRequestEntity();
        assertThat(entity.getRequesterId()).isNull();
    }

    @Test
    public void testSetRequesterId() {
        DataAccessRequestEntity entity = new DataAccessRequestEntity();
        String id = RandomStringUtils.randomAlphanumeric(10);
        entity.setRequesterId(id);
        assertThat(entity.getRequesterId()).isEqualTo(id);
    }

    @Test
    public void testGetReason() {
        DataAccessRequestEntity entity = new DataAccessRequestEntity();
        assertThat(entity.getReason()).isNull();
    }

    @Test
    public void testSetReason() {
        DataAccessRequestEntity entity = new DataAccessRequestEntity();
        String reason = RandomStringUtils.randomAlphanumeric(20);
        entity.setReason(reason);
        assertThat(entity.getReason()).isEqualTo(reason);
    }

    @Test
    public void testGetRequestDate() {
        DataAccessRequestEntity entity = new DataAccessRequestEntity();
        assertThat(entity.getRequestDate()).isNull();
    }

    @Test
    public void testSetRequestDate() {
        DataAccessRequestEntity entity = new DataAccessRequestEntity();
        ZonedDateTime date = ZonedDateTime.now();
        entity.setRequestDate(date);
        assertThat(entity.getRequestDate()).isEqualTo(date);
    }

    @Test
    public void testGetApprovedDate() {
        DataAccessRequestEntity entity = new DataAccessRequestEntity();
        assertThat(entity.getApprovedDate()).isNull();
    }

    @Test
    public void testSetApprovedDate() {
        DataAccessRequestEntity entity = new DataAccessRequestEntity();
        ZonedDateTime date = ZonedDateTime.now();
        entity.setApprovedDate(date);
        assertThat(entity.getApprovedDate()).isEqualTo(date);
    }

    @Test
    public void testToString() {
        DataAccessRequestEntity entity = TestUtil.getDataAccessRequestEntity();
        final String toString = entity.toString();

        assertThat(toString).contains(entity.getId().toString());
        assertThat(toString).contains(entity.getDataId().toString());
        assertThat(toString).contains(entity.getRequesterId());
        assertThat(toString).contains(entity.getReason());
        assertThat(toString).contains(entity.getRequestDate().toString());
        assertThat(toString).contains(entity.getApprovedDate().toString());
    }

}
