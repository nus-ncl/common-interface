package sg.ncl.common.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Random;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = "flyway.enabled=false")
public class AbstractEntityTest {

    @Inject
    private TestRepository testRepository;

    @Test
    public void testGetCreatedDate() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {};

        assertThat(abstractEntity.getCreatedDate(), is(nullValue()));
    }

    @Test
    public void testSetCreatedDate() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {};
        final ZonedDateTime now = ZonedDateTime.now();
        abstractEntity.setCreatedDate(now);

        assertThat(abstractEntity.getCreatedDate(), is(equalTo(now)));
    }

    @Test
    public void testGetLastModifiedDate() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {};

        assertThat(abstractEntity.getLastModifiedDate(), is(nullValue()));
    }

    @Test
    public void testSetLastModifiedDate() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {};
        final ZonedDateTime now = ZonedDateTime.now();
        abstractEntity.setLastModifiedDate(now);

        assertThat(abstractEntity.getLastModifiedDate(), is(equalTo(now)));
    }

    @Test
    public void testGetVersion() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {};

        assertThat(abstractEntity.getVersion(), is(equalTo(0L)));
    }

    @Test
    public void testSetVersion() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {};
        final long version = new Random().nextLong();
        abstractEntity.setVersion(version);

        assertThat(abstractEntity.getVersion(), is(equalTo(version)));
    }

    @Test
    public void testToString() throws Exception {
        final AbstractEntity abstractEntity = new AbstractEntity() {};
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

    @Test
    public void testSave() throws Exception {
        final TestEntity entity = new TestEntity();

        final TestEntity savedEntity = testRepository.saveAndFlush(entity);

        assertThat(savedEntity.getCreatedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getLastModifiedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getVersion(), is(not(nullValue(Long.class))));
    }

}
