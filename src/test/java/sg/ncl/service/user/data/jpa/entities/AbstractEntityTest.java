package sg.ncl.service.user.data.jpa.entities;

import org.junit.Test;
import sg.ncl.service.team.data.jpa.entities.AbstractEntity;

import java.time.ZonedDateTime;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 */
public class AbstractEntityTest {

    @Test
    public void testGetCreatedDate() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {
        };

        assertThat(abstractEntity.getCreatedDate(), is(nullValue()));
    }

    @Test
    public void testSetCreatedDate() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {
        };
        final ZonedDateTime now = ZonedDateTime.now();
        abstractEntity.setCreatedDate(now);

        assertThat(abstractEntity.getCreatedDate(), is(equalTo(now)));
    }

    @Test
    public void testGetLastModifiedDate() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {
        };

        assertThat(abstractEntity.getLastModifiedDate(), is(nullValue()));
    }

    @Test
    public void testSetLastModifiedDate() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {
        };
        final ZonedDateTime now = ZonedDateTime.now();
        abstractEntity.setLastModifiedDate(now);

        assertThat(abstractEntity.getLastModifiedDate(), is(equalTo(now)));
    }

    @Test
    public void testGetVersion() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {
        };

        assertThat(abstractEntity.getVersion(), is(equalTo(0L)));
    }

    @Test
    public void testSetVersion() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {
        };
        final long version = new Random().nextLong();
        abstractEntity.setVersion(version);

        assertThat(abstractEntity.getVersion(), is(equalTo(version)));
    }

    @Test
    public void testToString() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {
        };
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime createdDate = now.minusYears(1);
        abstractEntity.setCreatedDate(createdDate);
        final ZonedDateTime lastModifiedDate = now.minusMonths(1);
        abstractEntity.setLastModifiedDate(lastModifiedDate);
        final Long version = new Random().nextLong();
        abstractEntity.setVersion(version);

        final String toString = abstractEntity.toString();

        assertThat(toString, containsString(createdDate.toString()));
        assertThat(toString, containsString(lastModifiedDate.toString()));
        assertThat(toString, containsString(String.valueOf(version)));
    }

}