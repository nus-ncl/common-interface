package sg.ncl.service.experiment.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import sg.ncl.service.experiment.AbstractTest;
import sg.ncl.service.experiment.Util;
import sg.ncl.service.experiment.domain.Experiment;

import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
        assertThat(savedEntity.getCreatedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getLastModifiedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getVersion(), is(equalTo(0L)));
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

//    @Test
//    public void testSaveExperimentDifferentTeamSameExpName() throws Exception {
//        ExperimentEntity one = Util.getExperimentsEntity();
//        ExperimentEntity two = Util.getExperimentsEntity();
//        one.setName("sameExpName");
//        two.setName("sameExpName");
//
//        when(repository.save(any(ExperimentEntity.class))).thenAnswer(i -> {
//            ExperimentEntity e = i.getArgumentAt(0, ExperimentEntity.class);
//            e.setId(Long.parseLong(RandomStringUtils.randomNumeric(10));
//            return e;
//        });
//
//        Experiment oneSaved = experimentService.save(one);
//        Experiment twoSaved = experimentService.save(two);
//
//        verify(experimentRepository, times(2)).save(any(ExperimentEntity.class));
//        Assertions.assertThat(oneSaved.getName()).isEqualTo(twoSaved.getName());
//        Assertions.assertThat(oneSaved.getTeamName()).isNotEqualTo(twoSaved.getTeamName());
//        Assertions.assertThat(oneSaved.getId()).isNotEqualTo(twoSaved.getId());
//    }
}
