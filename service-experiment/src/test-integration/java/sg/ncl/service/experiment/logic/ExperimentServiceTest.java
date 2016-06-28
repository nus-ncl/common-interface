package sg.ncl.service.experiment.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import sg.ncl.service.experiment.AbstractTest;
import sg.ncl.service.experiment.Util;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desmond.
 */
public class ExperimentServiceTest extends AbstractTest {

    @Inject
    private ExperimentRepository experimentRepository;

    @Test
    public void testSaveTeam() throws Exception {
        ExperimentService experimentService = new ExperimentService(experimentRepository);
        ExperimentEntity createdExperiment = Util.getExperimentsEntity();
        ExperimentEntity saveExperiment = experimentService.save(createdExperiment);

        Assert.assertEquals(createdExperiment.getUserId(), saveExperiment.getUserId());
        Assert.assertEquals(createdExperiment.getTeamId(), saveExperiment.getTeamId());
        Assert.assertEquals(createdExperiment.getName(), saveExperiment.getName());
        Assert.assertEquals(createdExperiment.getDescription(), saveExperiment.getDescription());
        Assert.assertEquals(createdExperiment.getNsFile(), saveExperiment.getNsFile());
        Assert.assertEquals(createdExperiment.getIdleSwap(), saveExperiment.getIdleSwap());
        Assert.assertEquals(createdExperiment.getMaxDuration(), saveExperiment.getMaxDuration());
    }

    @Test
    public void testGetExperimentIfNotExperimentInDb() throws Exception {
        ExperimentService experimentService = new ExperimentService(experimentRepository);
        List<ExperimentEntity> experimentEntityList = experimentService.get();

        Assert.assertEquals(experimentEntityList.size(), 0);
    }

    @Test
    public void testGetExperiment() throws Exception {
        ExperimentService experimentService = new ExperimentService(experimentRepository);

        List<ExperimentEntity> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            list.add(experimentService.save(Util.getExperimentsEntity()));
        }

        List<ExperimentEntity> listFromDb = experimentService.get();
        Assert.assertTrue(Util.isListEqual(list, listFromDb));
    }

//    private boolean isListEqual(List<ExperimentEntity> one, List<ExperimentEntity> two) {
//        ArrayList<ExperimentEntity> cp = new ArrayList<>(one);
//        for (ExperimentEntity twoIterator : two) {
//            if (!cp.remove(twoIterator)) {
//                return false;
//            }
//        }
//        return cp.isEmpty();
//    }

    @Test
    public void testGetExperimentsByUser() throws Exception {
        ExperimentService experimentService = new ExperimentService(experimentRepository);

        final String userId = RandomStringUtils.randomAlphanumeric(20);
        for (int i = 0; i < 3; i++) {
            ExperimentEntity experimentEntity = Util.getExperimentsEntity();

            if (i > 0) {
                experimentEntity.setUserId(userId);
            }

            experimentService.save(experimentEntity);
        }

        List<ExperimentEntity> list = experimentService.findByUser(userId);
        Assert.assertEquals(list.size(), 2);
    }

    @Test
    public void testGetExperimentsUserHasNoExperiments() throws Exception {
        ExperimentService experimentService = new ExperimentService(experimentRepository);

        final String userId = RandomStringUtils.randomAlphanumeric(20);
        for (int i = 0; i < 3; i++) {
            experimentService.save(Util.getExperimentsEntity());
        }

        List<ExperimentEntity> list = experimentService.findByUser(userId);
        Assert.assertEquals(list.size(), 0);
    }
}
