package sg.ncl.adapter.deterlab.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.adapter.deterlab.DeterLabAutoConfiguration;
import sg.ncl.adapter.deterlab.Util;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabProjectEntity;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabUserEntity;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Te Ye
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@ContextConfiguration(classes = {DeterLabProjectRepository.class, DeterLabAutoConfiguration.class})
@TestPropertySource(properties = "flyway.enabled=false")
public class DeterLabProjectRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private DeterLabProjectRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository).isNotNull();
    }

    @Test
    public void testGoodSave() throws Exception {
        final DeterLabProjectEntity entity = Util.getDeterLabProjectEntity();

        final long count = repository.count();
        final DeterLabProjectEntity savedEntity = repository.saveAndFlush(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(entity.getNclTeamId()).isEqualTo(savedEntity.getNclTeamId());
        assertThat(entity.getDeterProjectId()).isEqualTo(savedEntity.getDeterProjectId());
    }

    @Test
    public void testSaveWithNullId() throws Exception {
        final DeterLabProjectEntity entity = Util.getDeterLabProjectEntity();
        entity.setId(null);
        final long count = repository.count();

        final DeterLabProjectEntity saved = repository.saveAndFlush(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void testSaveWithExistingEntityWithNullId() throws Exception {
        final DeterLabProjectEntity entity = Util.getDeterLabProjectEntity();
        final DeterLabProjectEntity saved = repository.saveAndFlush(entity);
        saved.setId(null);

        exception.expect(JpaSystemException.class);

        repository.saveAndFlush(saved);
    }
}
