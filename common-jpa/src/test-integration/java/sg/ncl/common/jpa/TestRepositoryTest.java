package sg.ncl.common.jpa;

import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;
import sg.ncl.common.test.AbstractTest;

import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@SpringApplicationConfiguration(TestApp.class)
public class TestRepositoryTest extends AbstractTest {

    @Inject
    private TestRepository testRepository;

    @Test
    public void testSave() throws Exception {
        final TestEntity entity = new TestEntity();

        final TestEntity savedEntity = testRepository.saveAndFlush(entity);

        assertThat(savedEntity.getCreatedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getLastModifiedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getVersion(), is(not(nullValue(Long.class))));
    }

}
