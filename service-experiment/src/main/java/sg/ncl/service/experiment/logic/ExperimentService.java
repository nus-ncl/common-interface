package sg.ncl.service.experiment.logic;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.adapter.deterlab.exceptions.ExpNameAlreadyExistsException;
import sg.ncl.service.experiment.ExperimentConnectionProperties;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.exceptions.UserIdNotFoundException;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.domain.Realization;
import sg.ncl.service.realization.logic.RealizationService;

import javax.inject.Inject;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Desmond.
 */
@Service
public class ExperimentService {

    private static final Logger logger = LoggerFactory.getLogger(ExperimentService.class);
    private final ExperimentRepository experimentRepository;

    @Inject
    private AdapterDeterlab adapterDeterlab;

    @Inject
    private ExperimentConnectionProperties experimentConnectionProperties;

    private RealizationService realizationService;

    @Inject
    protected ExperimentService(final ExperimentRepository experimentRepository, final AdapterDeterlab adapterDeterlab, final RealizationService realizationService, final ExperimentConnectionProperties experimentConnectionProperties) {
        this.experimentRepository = experimentRepository;
        this.adapterDeterlab = adapterDeterlab;
        this.realizationService = realizationService;
        this.experimentConnectionProperties = experimentConnectionProperties;
    }

    @Transactional
    public ExperimentEntity save(Experiment experiment) {
        logger.info("Save experiment");
        String fileName = craftFileName(experiment);

        // check experiment name is unique
        long countName = experimentRepository.countByName(experiment.getName());
        if (countName > 0) {
            logger.warn("Experiment name is in use.");
            return null;
        }

//        createNsFile(fileName, experiment.getNsFileContent());

        ExperimentEntity savedExperimentEntity = experimentRepository.save(setupEntity(experiment, fileName));
        createExperimentInDeter(savedExperimentEntity);
        logger.info("Experiment saved.");

        RealizationEntity realizationEntity = new RealizationEntity();
        realizationEntity.setExperimentId(savedExperimentEntity.getId());
        realizationEntity.setExperimentName(savedExperimentEntity.getName());
        realizationEntity.setUserId(savedExperimentEntity.getUserId());
        realizationEntity.setTeamId(savedExperimentEntity.getTeamId());
        realizationEntity.setNumberOfNodes(0);
        realizationEntity.setIdleMinutes(0L);
        realizationEntity.setRunningMinutes(0L);

        realizationService.save(realizationEntity);
        logger.info("Realization saved.");

        return savedExperimentEntity;
    }

    private String craftDate() {
        Date date = new Date();
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    private String craftFileName(Experiment experiment) {
        String fileName = experiment.getNsFile() + ".ns";
        return experiment.getUserId() + "_" + experiment.getTeamId() + "_" + craftDate() + "_" + fileName;
    }

    private ExperimentEntity setupEntity(Experiment experiment, String fileName) {
        ExperimentEntity experimentEntity = new ExperimentEntity();

        experimentEntity.setUserId(experiment.getUserId());
        experimentEntity.setTeamId(experiment.getTeamId());
        experimentEntity.setTeamName(experiment.getTeamName());
        experimentEntity.setName(experiment.getName());
        experimentEntity.setDescription(experiment.getDescription());
        experimentEntity.setNsFile(fileName);
        experimentEntity.setNsFileContent(experiment.getNsFileContent());
        experimentEntity.setIdleSwap(experiment.getIdleSwap());
        experimentEntity.setMaxDuration(experiment.getMaxDuration());

        return experimentEntity;
    }

    public List<ExperimentEntity> get() {
        logger.info("Get all experiments");

        final List<ExperimentEntity> result = new ArrayList<>();
        result.addAll(experimentRepository.findAll());

        return result;
    }

    public List<ExperimentEntity> findByUser(String userId) {
        logger.info("Find user by user id");

        if (userId == null || userId.isEmpty()) {
            throw new UserIdNotFoundException();
        }

        List<ExperimentEntity> result = experimentRepository.findByUserId(userId);

        return result;
    }

    public List<ExperimentEntity> findByTeam(String teamId) {
        logger.info("Find teams by team id");

        if (teamId == null || teamId.isEmpty()) {
            throw new UserIdNotFoundException();
        }

        List<ExperimentEntity> result = experimentRepository.findByTeamId(teamId);

        return result;
    }

    public String createNsFile(String filename, String contents) {
        logger.info("Create NS file");

        File file;
        FileOutputStream fileOutputStream = null;

        try {
            file = new File(filename);
            fileOutputStream = new FileOutputStream(file);

            // if file doesn't exist, create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get contents in bytes
            byte[] contentInBytes = contents.getBytes();

            fileOutputStream.write(contentInBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        }

        catch (IOException e) {
            logger.error("File cannot be created.\n" + e.getMessage());
            filename = "error";
        }

        finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }

            catch (IOException e) {
                logger.error("File cannot be created.\n" + e.getMessage());
                filename = "error";
            }
        }

        return filename;
    }

    public void createExperimentInDeter(ExperimentEntity experimentEntity) {
        logger.info("Start createExperimentInDeter");

        JSONObject userObject = new JSONObject();
        userObject.put("id", experimentEntity.getId().toString());
        userObject.put("userId", experimentEntity.getUserId());
        userObject.put("teamId", experimentEntity.getTeamId());
        userObject.put("teamName", experimentEntity.getTeamName());
        userObject.put("name", experimentEntity.getName());
        userObject.put("description", experimentEntity.getDescription());
        userObject.put("nsFile", experimentEntity.getNsFile());
        userObject.put("nsFileContent", experimentEntity.getNsFileContent());
        userObject.put("idleSwap", experimentEntity.getIdleSwap().toString());
        userObject.put("maxDuration", experimentEntity.getMaxDuration().toString());
        userObject.put("deterLogin", adapterDeterlab.getDeterUserIdByNclUserId(experimentEntity.getUserId()));
        userObject.put("userServerUri", experimentConnectionProperties.getUserurl());

        adapterDeterlab.createExperiment(userObject.toString());

        logger.info("End createExperimentInDeter");
    }

    public String deleteExperiment(final Long id) {
        logger.info("Start deleteExperiment");
        String returnString = "experiment deleted";

        RealizationEntity realizationEntity = realizationService.getByExperimentId(id);
        Long realizationId = realizationEntity.getId();

        if (realizationId != null && realizationId > 0) {
            realizationService.deleteRealization(realizationId);
            logger.info("Realization deleted");

            ExperimentEntity experimentEntity = experimentRepository.getOne(id);
            // TODO: use other deleteExperimentInDeter(teamName, experimentName) if using script_wrapper.py
            deleteExperimentInDeter(experimentEntity.getName(), realizationEntity.getUserId());
            logger.info("Experiment deleted in deter");

            experimentRepository.delete(id);
            logger.info("Experiment deleted");
        }
        else {
            logger.warn("Experiment not deleted");
            returnString = "experiment not deleted";
        }

        logger.info("End deleteExperiment");

        return returnString;
    }

    private void deleteExperimentInDeter(final String experimentName, final String nclUserId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("experimentName", experimentName);
        jsonObject.put("deterLogin", adapterDeterlab.getDeterUserIdByNclUserId(nclUserId));

        adapterDeterlab.deleteExperiment(jsonObject.toString());
    }

    // TODO: Use this if using script_wrapper.py
//    private void deleteExperimentInDeter(final String teamName, final String experimentName) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("teamName", teamName);
//        jsonObject.put("experimentName", experimentName);
//
//        adapterDeterlab.deleteExperiment(jsonObject.toString());
//    }
}
