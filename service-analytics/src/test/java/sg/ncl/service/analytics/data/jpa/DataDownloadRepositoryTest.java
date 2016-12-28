package sg.ncl.service.analytics.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcszwang on 12/28/2016.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@ContextConfiguration(classes = DataDownloadRepository.class)
@TestPropertySource(properties = "flyway.enabled=false")
public class DataDownloadRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private DataDownloadRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository).isNotNull().isInstanceOf(DataDownloadRepository.class);

    }

    @Test
    public void testGoodSave() throws Exception {
        DataDownloadEntity entity = new DataDownloadEntity();
        entity.setDataId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setResourceId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setDownloadDate(ZonedDateTime.now());
        entity.setHashedUserId(RandomStringUtils.randomAlphanumeric(20));

        final long count = repository.count();
        final DataDownloadEntity savedEntity = repository.saveAndFlush(entity);
        assertThat(repository.count()).isEqualTo(count+1);
        assertThat(savedEntity.getId()).isNotNull();
        assertThat(savedEntity.getId()).isInstanceOf(Long.class);
        assertThat(savedEntity.getCreatedDate()).isNotNull();
        assertThat(savedEntity.getLastModifiedDate()).isNotNull();
        assertThat(savedEntity.getVersion()).isEqualTo(0L);
    }
}
