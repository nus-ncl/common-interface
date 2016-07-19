package sg.ncl.service.realization.data.jpa.repositories;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.dao.DataIntegrityViolationException;
import sg.ncl.service.realization.AbstractTest;
import sg.ncl.service.realization.Util;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.data.jpa.RealizationRepository;
import sg.ncl.service.realization.domain.RealizationState;

import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static sg.ncl.common.test.Checks.checkException;

/**
 * @author Christopher Zhong
 */
public class RealizationRepositoryTest extends AbstractTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private RealizationRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository, is(not(nullValue(RealizationRepository.class))));
    }

    @Test
    public void testGoodSave() throws Exception {
        final RealizationEntity entity = Util.getRealizationEntity();

        final long count = repository.count();
        final RealizationEntity savedEntity = repository.saveAndFlush(entity);
        assertThat(repository.count(), is(equalTo(count + 1)));
        assertThat(savedEntity.getUserId(), is(not(nullValue(String.class))));
        assertThat(savedEntity.getTeamId(), is(not(nullValue(String.class))));
        assertThat(savedEntity.getExperimentId(), is(not(nullValue(Long.class))));
        assertThat(savedEntity.getNumberOfNodes(), is(not(nullValue(Integer.class))));
        assertThat(savedEntity.getState(), is(not(nullValue(RealizationState.class))));
        assertThat(savedEntity.getIdleMinutes(), is(not(nullValue(Long.class))));
        assertThat(savedEntity.getRunningMinutes(), is(not(nullValue(Long.class))));
        assertThat(savedEntity.getCreatedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getLastModifiedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getVersion(), is(equalTo(0L)));
    }

    @Test
    public void testSaveNullExperimentId() throws Exception {
        final RealizationEntity entity = Util.getRealizationEntity();
        entity.setExperimentId(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"EXPERIMENT_ID\"");
        }
    }

    @Test
    public void testSaveNullUserId() throws Exception {
        final RealizationEntity entity = Util.getRealizationEntity();
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
        final RealizationEntity entity = Util.getRealizationEntity();
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
        final RealizationEntity entity = Util.getRealizationEntity();
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
        final RealizationEntity entity = Util.getRealizationEntity();
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
        final RealizationEntity entity = Util.getRealizationEntity();
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
        final RealizationEntity entity = Util.getRealizationEntity();
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
        final RealizationEntity entity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntitiy = repository.save(entity);

        Long experimentId = savedRealizationEntitiy.getExperimentId();

        RealizationEntity realizationEntityDB = repository.findByExperimentId(experimentId);

        assertNotNull(realizationEntityDB);
        assertEquals(entity.getUserId(), realizationEntityDB.getUserId());
        assertEquals(entity.getTeamId(), realizationEntityDB.getTeamId());
        assertEquals(entity.getExperimentId(), realizationEntityDB.getExperimentId());
        assertEquals(entity.getNumberOfNodes(), realizationEntityDB.getNumberOfNodes());
        assertEquals(entity.getState(), realizationEntityDB.getState());
        assertEquals(entity.getIdleMinutes(), realizationEntityDB.getIdleMinutes());
        assertEquals(entity.getRunningMinutes(), realizationEntityDB.getRunningMinutes());
    }
}