package sg.ncl.service.realization.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static sg.ncl.common.test.Checks.checkException;
import static sg.ncl.service.realization.util.TestUtil.getRealizationEntity;

/**
 * @author Christopher Zhong
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@ContextConfiguration(classes = RealizationRepository.class)
@TestPropertySource(properties = "flyway.enabled=false")
public class RealizationRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private RealizationRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository).isNotNull();
    }

    @Test
    public void testGoodSave() throws Exception {
        final RealizationEntity entity = getRealizationEntity();

        final long count = repository.count();
        final RealizationEntity savedEntity = repository.saveAndFlush(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(savedEntity.getCreatedDate()).isNotNull();
        assertThat(savedEntity.getLastModifiedDate()).isNotNull();
        assertThat(savedEntity.getUserId()).isNotNull();
        assertThat(savedEntity.getTeamId()).isNotNull();
        assertThat(savedEntity.getExperimentId()).isNotNull();
        assertThat(savedEntity.getNumberOfNodes()).isNotNull();
        assertThat(savedEntity.getState()).isNotNull();
        assertThat(savedEntity.getIdleMinutes()).isNotNull();
        assertThat(savedEntity.getRunningMinutes()).isNotNull();
    }

    @Test
    public void testSaveWithNullId() throws Exception {
        final RealizationEntity entity = getRealizationEntity();
        entity.setId(null);
        final long count = repository.count();

        final RealizationEntity saved = repository.saveAndFlush(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void testSaveWithExistingEntityWithNullId() throws Exception {
        final RealizationEntity entity = getRealizationEntity();
        final RealizationEntity saved = repository.saveAndFlush(entity);
        saved.setId(null);

        exception.expect(JpaSystemException.class);

        repository.saveAndFlush(saved);
    }

    @Test
    public void testSaveNullExperimentId() throws Exception {
        final RealizationEntity entity = getRealizationEntity();
        entity.setExperimentId(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"EXPERIMENT_ID\"");
        }
    }

    @Test
    public void testSaveNullExperimentName() throws Exception {
        final RealizationEntity entity = getRealizationEntity();
        entity.setExperimentName(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"EXPERIMENT_NAME\"");
        }
    }

    @Test
    public void testSaveNullUserId() throws Exception {
        final RealizationEntity entity = getRealizationEntity();
        entity.setUserId(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USER_ID\"");
        }
    }

    @Test
    public void testSaveNullTeamId() throws Exception {
        final RealizationEntity entity = getRealizationEntity();
        entity.setTeamId(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"TEAM_ID\"");
        }
    }

    @Test
    public void testSaveNullNumberOfNodes() throws Exception {
        final RealizationEntity entity = getRealizationEntity();
        entity.setNumberOfNodes(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"NUM_NODES\"");
        }
    }

    @Test
    public void testSaveNullState() throws Exception {
        final RealizationEntity entity = getRealizationEntity();
        entity.setState(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"STATE\"");
        }
    }

    @Test
    public void testSaveNullIdleMinutes() throws Exception {
        final RealizationEntity entity = getRealizationEntity();
        entity.setIdleMinutes(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"IDLE_MINUTES\"");
        }
    }

    @Test
    public void testSaveNullRunningMinutes() throws Exception {
        final RealizationEntity entity = getRealizationEntity();
        entity.setRunningMinutes(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"RUNNING_MINUTES\"");
        }
    }

    @Test
    public void testGetRealizationByExperimentId() throws Exception {
        final RealizationEntity entity = getRealizationEntity();
        RealizationEntity savedRealizationEntitiy = repository.save(entity);

        RealizationEntity result = repository.findByExperimentId(savedRealizationEntitiy.getExperimentId());

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(entity.getUserId());
        assertThat(result.getTeamId()).isEqualTo(entity.getTeamId());
        assertThat(result.getExperimentId()).isEqualTo(entity.getExperimentId());
        assertThat(result.getNumberOfNodes()).isEqualTo(entity.getNumberOfNodes());
        assertThat(result.getState()).isEqualTo(entity.getState());
        assertThat(result.getIdleMinutes()).isEqualTo(entity.getIdleMinutes());
        assertThat(result.getRunningMinutes()).isEqualTo(entity.getRunningMinutes());
    }
}
