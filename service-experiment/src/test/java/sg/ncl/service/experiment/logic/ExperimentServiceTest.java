package sg.ncl.service.experiment.logic;

import mockit.Expectations;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import sg.ncl.service.experiment.AbstractTest;
import sg.ncl.service.experiment.ConnectionProperties;
import sg.ncl.service.experiment.Util;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static sg.ncl.service.experiment.Util.createNsFileContents;

/**
 * Created by Desmond.
 */
public class ExperimentServiceTest extends AbstractTest {

    @Inject
    private ExperimentRepository experimentRepository;

    @Inject
    private ExperimentService experimentService;

    @Test
    public void testSaveExperiment() throws Exception {

        ExperimentEntity createdExperimentSave = Util.getExperimentsEntity();

        ExperimentService localExperimentService = new ExperimentService(experimentRepository) {
            @Override
            public String createExperimentInDeter(ExperimentEntity experimentEntity) {
                return "done";
            }
        };

        ExperimentEntity savedExperiment = localExperimentService.save(createdExperimentSave);

        Assert.assertNotNull(savedExperiment);
        Assert.assertEquals(createdExperimentSave.getUserId(), savedExperiment.getUserId());
        Assert.assertEquals(createdExperimentSave.getTeamId(), savedExperiment.getTeamId());
        Assert.assertEquals(createdExperimentSave.getTeamName(), savedExperiment.getTeamName());
        Assert.assertEquals(createdExperimentSave.getName(), savedExperiment.getName());
        Assert.assertEquals(createdExperimentSave.getDescription(), savedExperiment.getDescription());
        Assert.assertEquals(createdExperimentSave.getIdleSwap(), savedExperiment.getIdleSwap());
        Assert.assertEquals(createdExperimentSave.getMaxDuration(), savedExperiment.getMaxDuration());

        // check new nsFile name
        String craftDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String filename = createdExperimentSave.getUserId() + "_" + createdExperimentSave.getTeamId() + "_" + craftDate + "_" + createdExperimentSave.getNsFile() + ".ns";
        Assert.assertEquals(filename, savedExperiment.getNsFile());

        // delete the created ns file
        File file = new File(filename);
        Files.deleteIfExists(file.toPath());
    }

    @Test
    public void testGetExperimentIfNoExperimentInDb() throws Exception {

        List<ExperimentEntity> experimentEntityList = experimentService.get();

        Assert.assertEquals(experimentEntityList.size(), 0);
    }

    @Test
    public void testGetExperiment() throws Exception {
        List<ExperimentEntity> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            list.add(experimentRepository.save(Util.getExperimentsEntity()));
        }

        List<ExperimentEntity> listFromDb = experimentService.get();
        Assert.assertTrue(Util.isListEqual(list, listFromDb));
    }

    @Test
    public void testGetExperimentsByUser() throws Exception {

        int numEntries = 6;

        final String userId = RandomStringUtils.randomAlphanumeric(20);
        Util.addExperiments(numEntries, userId, experimentRepository);

        List<ExperimentEntity> list = experimentService.findByUser(userId);
        Assert.assertEquals(list.size(), numEntries / 2);

        for (ExperimentEntity forEntity : list) {
            Assert.assertEquals(forEntity.getUserId(), userId);
        }

        List<ExperimentEntity> allExperiments = experimentService.get();
        Assert.assertEquals(allExperiments.size(), numEntries);
    }

    @Test
    public void testGetExperimentsUserHasNoExperiments() throws Exception {
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        for (int i = 0; i < 3; i++) {
            experimentRepository.save(Util.getExperimentsEntity());
        }

        List<ExperimentEntity> list = experimentService.findByUser(userId);
        Assert.assertEquals(list.size(), 0);
    }

    @Test
    public void testCreateExperimentInDeter() throws Exception {
        ExperimentEntity experimentEntity = Util.getExperimentsEntity();
        experimentEntity.setNsFile("nsfile.ns");

        createNsFileContents();

        ExperimentService experimentService = new ExperimentService(experimentRepository);
        new Expectations(ExperimentService.class) {{
            experimentService.createExperimentInDeter(experimentEntity); result = "done";
        }};

        String response = experimentService.createExperimentInDeter(experimentEntity);
        System.out.println(response);
    }
}
