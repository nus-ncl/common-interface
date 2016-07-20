package sg.ncl.service.realization.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import sg.ncl.service.realization.AbstractTest;
import sg.ncl.service.realization.Util;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.data.jpa.RealizationRepository;
import sg.ncl.service.realization.domain.RealizationState;

import javax.inject.Inject;

/**
 * Created by Desmond.
 */
public class RealizationServiceTest extends AbstractTest {

    @Inject
    private RealizationService realizationService;

    @Inject
    private RealizationRepository realizationRepository;

    @Test
    public void getRealizationWithWrongIdTest() {

        realizationRepository.save((Util.getRealizationEntity()));

        Long id = Long.parseLong(RandomStringUtils.randomNumeric(5));
        RealizationEntity realizationEntityDb = realizationService.getById(id);

        Assert.assertNull(realizationEntityDb);
    }

    @Test
    public void getRealizationWithIdTest() {

        RealizationEntity savedRealizationEntity = realizationRepository.save((Util.getRealizationEntity()));

        Long id = savedRealizationEntity.getId();
        RealizationEntity realizationEntityDb = realizationService.getById(id);

        Assert.assertNotNull(realizationEntityDb);
    }

    @Test
    public void getRealizationWithWrongExperimentIdTest() {

        realizationRepository.save((Util.getRealizationEntity()));

        Long experimentId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        RealizationEntity realizationEntityDb = realizationService.getByExperimentId(experimentId);

        Assert.assertNull(realizationEntityDb);
    }

    @Test
    public void getRealizationWithExperimentIdTest() {

        RealizationEntity realizationEntity = Util.getRealizationEntity();
        Long experimentId = realizationEntity.getExperimentId();

        realizationRepository.save((realizationEntity));
        RealizationEntity realizationEntityDb = realizationService.getByExperimentId(experimentId);

        Assert.assertNotNull(realizationEntityDb);
    }

    @Test
    public void saveRealizationTest() {

        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntiy = realizationService.save(realizationEntity);

        Assert.assertEquals(realizationEntity.getUserId(), savedRealizationEntiy.getUserId());
        Assert.assertEquals(realizationEntity.getTeamId(), savedRealizationEntiy.getTeamId());
        Assert.assertEquals(realizationEntity.getExperimentId(), savedRealizationEntiy.getExperimentId());
        Assert.assertEquals(realizationEntity.getNumberOfNodes(), savedRealizationEntiy.getNumberOfNodes());
        Assert.assertEquals(realizationEntity.getState(), savedRealizationEntiy.getState());
        Assert.assertEquals(realizationEntity.getIdleMinutes(), savedRealizationEntiy.getIdleMinutes());
        Assert.assertEquals(realizationEntity.getRunningMinutes(), savedRealizationEntiy.getRunningMinutes());
    }

    @Test
    public void getStateTest() {
        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Assert.assertEquals(savedRealizationEntity.getState(), RealizationState.STOP);
    }

    @Test
    public void setStateTest() {

        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Long experimentId = savedRealizationEntity.getExperimentId();

        realizationService.setState(experimentId, RealizationState.WARNING);

        RealizationEntity realizationEntityDB = realizationRepository.findByExperimentId(experimentId);

        Assert.assertEquals(realizationEntityDB.getState(), RealizationState.WARNING);
    }

    @Test
    public void getIdleMinutesTest() {
        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Assert.assertEquals(savedRealizationEntity.getIdleMinutes(), realizationEntity.getIdleMinutes());
    }

    @Test
    public void setIdleMinutesTest() {

        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Long experimentId = savedRealizationEntity.getExperimentId();
        Long minutes = Long.parseLong(RandomStringUtils.randomNumeric(5));

        realizationService.setIdleMinutes(experimentId, minutes);

        RealizationEntity realizationEntityDB = realizationRepository.findByExperimentId(experimentId);

        Assert.assertEquals(realizationEntityDB.getIdleMinutes(), minutes);
    }

    @Test
    public void getRunningMinutesTest() {
        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Assert.assertEquals(savedRealizationEntity.getRunningMinutes(), realizationEntity.getRunningMinutes());
    }

    @Test
    public void setRunningMinutesTest() {

        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Long experimentId = savedRealizationEntity.getExperimentId();
        Long minutes = Long.parseLong(RandomStringUtils.randomNumeric(5));

        realizationService.setRunningMinutes(experimentId, minutes);

        RealizationEntity realizationEntityDB = realizationRepository.findByExperimentId(experimentId);

        Assert.assertEquals(realizationEntityDB.getRunningMinutes(), minutes);
    }
}
