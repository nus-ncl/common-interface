package sg.ncl.service.experiment.data.jpa.repositories;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import sg.ncl.service.experiment.AbstractTest;
import sg.ncl.service.experiment.Util;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;

import javax.inject.Inject;

import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static sg.ncl.common.test.Checks.checkException;

/**
 * Created by Desmond
 */
public class ExperimentRepositoryTest extends AbstractTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private ExperimentRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository, is(not(nullValue(ExperimentRepository.class))));
    }

    @Test
    public void testGoodSave() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();

        final long count = repository.count();
        final ExperimentEntity savedEntity = repository.saveAndFlush(entity);
        assertThat(repository.count(), is(equalTo(count + 1)));
        assertThat(entity.getId(), is(equalTo(savedEntity.getId())));
        assertThat(savedEntity.getCreatedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getLastModifiedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getVersion(), is(equalTo(0L)));
    }

    @Test
    public void testSaveNullId() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();
        entity.setId(null);

        exception.expect(JpaSystemException.class);
        exception.expectMessage(ExperimentEntity.class.getName());

        repository.saveAndFlush(entity);
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
        entity.setName(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"NS_FILE\"");
        }
    }

    @Test
    public void testSaveNullIdleSwap() throws Exception {
        final ExperimentEntity entity = Util.getExperimentsEntity();
        entity.setName(null);

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
        entity.setName(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"MAX_DURATION\"");
        }
    }
}