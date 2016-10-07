package sg.ncl.service.experiment.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.service.experiment.Util;
import sg.ncl.service.experiment.domain.Experiment;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static sg.ncl.common.test.Checks.checkException;

/**
 * Created by Desmond
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@ContextConfiguration(classes = ExperimentRepository.class)
public class ExperimentRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private ExperimentRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository).isNotNull();
    }

    @Test
    public void testGoodSave() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();

        final long count = repository.count();
        final ExperimentEntity savedEntity = repository.saveAndFlush(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(savedEntity.getCreatedDate()).isNotNull();
        assertThat(savedEntity.getLastModifiedDate()).isNotNull();
    }

    @Test
    public void testSaveWithNullId() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();
        entity.setId(null);
        final long count = repository.count();

        final ExperimentEntity saved = repository.saveAndFlush(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void testSaveWithExistingEntityWithNullId() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();
        final ExperimentEntity saved = repository.saveAndFlush(entity);
        saved.setId(null);

        exception.expect(JpaSystemException.class);

        repository.saveAndFlush(saved);
    }

    @Test
    public void testSaveNullUserId() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();
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
        final ExperimentEntity entity = Util.getExperimentsEntity();
        entity.setTeamId(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"TEAM_ID\"");
        }
    }

    @Test
    public void testSaveNullTeamName() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();
        entity.setTeamName(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"TEAM_NAME\"");
        }
    }

    @Test
    public void testSaveNullName() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();
        entity.setName(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"NAME\"");
        }
    }

    @Test
    public void testSaveNullDescription() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();
        entity.setDescription(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"DESCRIPTION\"");
        }
    }

    @Test
    public void testSaveNullNsFile() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();
        entity.setNsFile(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"NS_FILE\"");
        }
    }

    @Test
    public void testSaveNullNsFileContent() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();
        entity.setNsFileContent(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"NS_FILE_CONTENT\"");
        }
    }

    @Test
    public void testSaveNullIdleSwap() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();
        entity.setIdleSwap(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"IDLE_SWAP\"");
        }
    }

    @Test
    public void testSaveNullMaxDuration() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();
        entity.setMaxDuration(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"MAX_DURATION\"");
        }
    }

    @Test
    public void testSaveExperimentDifferentTeamSameExpName() throws Exception {
        ExperimentEntity one = Util.getExperimentsEntity();
        ExperimentEntity two = Util.getExperimentsEntity();
        one.setName("sameExpName");
        two.setName("sameExpName");

        final long count = repository.count();
        Experiment oneSaved = repository.saveAndFlush(one);
        Experiment twoSaved = repository.saveAndFlush(two);

        assertThat(repository.count()).isEqualTo(count + 2);
        assertThat(oneSaved.getName()).isEqualTo(twoSaved.getName());
        assertThat(oneSaved.getTeamName()).isNotEqualTo(twoSaved.getTeamName());
        assertThat(oneSaved.getId()).isNotEqualTo(twoSaved.getId());
    }
}
