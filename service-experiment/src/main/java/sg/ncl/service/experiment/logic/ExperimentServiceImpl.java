package sg.ncl.service.experiment.logic;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.experiment.ExperimentConnectionProperties;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.domain.ExperimentService;
import sg.ncl.service.experiment.exceptions.UserIdNotFoundException;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.domain.RealizationService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Desmond.
 */
@Service
@Slf4j
public class ExperimentServiceImpl implements ExperimentService {

    private final ExperimentRepository experimentRepository;
    private final AdapterDeterLab adapterDeterLab;
    private final ExperimentConnectionProperties experimentConnectionProperties;
    private final RealizationService realizationService;

    @Inject
    ExperimentServiceImpl(@NotNull final ExperimentRepository experimentRepository, @NotNull final AdapterDeterLab adapterDeterLab, @NotNull final RealizationService realizationService, @NotNull final ExperimentConnectionProperties experimentConnectionProperties) {
        this.experimentRepository = experimentRepository;
        this.adapterDeterLab = adapterDeterLab;
        this.realizationService = realizationService;
        this.experimentConnectionProperties = experimentConnectionProperties;
    }

    @Transactional
    public Experiment save(Experiment experiment) {
        log.info("Save experiment");
        String fileName = craftFileName(experiment);

        // check experiment name is unique
        long countName = experimentRepository.countByName(experiment.getName());
        if (countName > 0) {
            log.warn("Experiment name is in use.");
            return null;
        }

//        createNsFile(fileName, experiment.getNsFileContent());

        ExperimentEntity savedExperimentEntity = experimentRepository.save(setupEntity(experiment, fileName));
        createExperimentInDeter(savedExperimentEntity);
        log.info("Experiment saved.");

        RealizationEntity realizationEntity = new RealizationEntity();
        realizationEntity.setExperimentId(savedExperimentEntity.getId());
        realizationEntity.setExperimentName(savedExperimentEntity.getName());
        realizationEntity.setUserId(savedExperimentEntity.getUserId());
        realizationEntity.setTeamId(savedExperimentEntity.getTeamId());
        realizationEntity.setNumberOfNodes(0);
        realizationEntity.setIdleMinutes(0L);
        realizationEntity.setRunningMinutes(0L);

        realizationService.save(realizationEntity);
        log.info("Realization saved.");

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

    @Transactional
    public List<Experiment> getAll() {
        log.info("Get all experiments");
        return experimentRepository.findAll().stream().collect(Collectors.toList());
    }

    @Transactional
    public List<Experiment> findByUser(String userId) {
        log.info("Find user by user id");

        if (userId == null || userId.isEmpty()) {
            // FIXME: this is the wrong exception to throw; it should be a BadRequestException type
            throw new UserIdNotFoundException();
        }

        return experimentRepository.findByUserId(userId).stream().collect(Collectors.toList());
    }

    @Transactional
    public List<Experiment> findByTeam(String teamId) {
        log.info("Find teams by team id");

        if (teamId == null || teamId.isEmpty()) {
            // FIXME: this is the wrong exception to throw; it should be a BadRequestException type
            throw new UserIdNotFoundException();
        }

        return experimentRepository.findByTeamId(teamId).stream().collect(Collectors.toList());
    }

    public String createNsFile(String filename, String contents) {
        log.info("Create NS file");

        File file;
        FileOutputStream fileOutputStream = null;

        try {
            file = new File(filename);
            fileOutputStream = new FileOutputStream(file);

            // if file doesn't exist, create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // getAll contents in bytes
            byte[] contentInBytes = contents.getBytes();

            fileOutputStream.write(contentInBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            log.error("File cannot be created.\n" + e.getMessage());
            filename = "error";
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                log.error("File cannot be created.\n" + e.getMessage());
                filename = "error";
            }
        }

        return filename;
    }

    public void createExperimentInDeter(Experiment experiment) {
        log.info("Start createExperimentInDeter");

        JSONObject userObject = new JSONObject();
        userObject.put("id", experiment.getId().toString());
        userObject.put("userId", experiment.getUserId());
        userObject.put("teamId", experiment.getTeamId());
        userObject.put("teamName", experiment.getTeamName());
        userObject.put("name", experiment.getName());
        userObject.put("description", experiment.getDescription());
        userObject.put("nsFile", experiment.getNsFile());
        userObject.put("nsFileContent", experiment.getNsFileContent());
        userObject.put("idleSwap", experiment.getIdleSwap().toString());
        userObject.put("maxDuration", experiment.getMaxDuration().toString());
        userObject.put("deterLogin", adapterDeterLab.getDeterUserIdByNclUserId(experiment.getUserId()));
        userObject.put("userServerUri", experimentConnectionProperties.getUserurl());

        adapterDeterLab.createExperiment(userObject.toString());

        log.info("End createExperimentInDeter");
    }


    // returns the deleted entity
    @Transactional
    public Experiment deleteExperiment(final Long id, final String teamName) {
        log.info("Deleting Experiment: {} from Team: {}", id, teamName);
        Experiment experimentEntity = null;

        RealizationEntity realizationEntity = realizationService.getByExperimentId(id);
        Long realizationId = realizationEntity.getId();

        if (realizationId != null && realizationId > 0) {
            realizationService.deleteRealization(realizationId);
            log.info("Realization deleted");

            experimentEntity = experimentRepository.getOne(id);
            // TODO: use other deleteExperimentInDeter(teamName, experimentName) if using script_wrapper.py
//            deleteExperimentInDeter(experimentEntity.getName(), realizationEntity.getUserId());
            deleteExperimentInDeter(experimentEntity.getName(), teamName, realizationEntity.getUserId());
            log.info("Experiment deleted in deter: {} from Team: {}", experimentEntity.getName(), teamName);

            experimentRepository.delete(id);
            log.info("Experiment deleted from experiment repository: {} from Team: {}", experimentEntity.getName(), teamName);
        } else {
            log.warn("Experiment not deleted");
        }

        log.info("End deleteExperiment");
        return experimentEntity;
    }

    private void deleteExperimentInDeter(final String experimentName, final String teamName, final String nclUserId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("teamName", teamName);
        jsonObject.put("experimentName", experimentName);
        jsonObject.put("deterLogin", adapterDeterLab.getDeterUserIdByNclUserId(nclUserId));

        adapterDeterLab.deleteExperiment(jsonObject.toString());
    }
}
