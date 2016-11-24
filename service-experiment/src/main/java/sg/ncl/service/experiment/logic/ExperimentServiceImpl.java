package sg.ncl.service.experiment.logic;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.domain.ExperimentService;
import sg.ncl.service.experiment.exceptions.ExperimentNameAlreadyExistsException;
import sg.ncl.service.experiment.exceptions.TeamIdNullOrEmptyException;
import sg.ncl.service.experiment.exceptions.UserIdNullOrEmptyException;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.domain.RealizationService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static sg.ncl.service.experiment.validation.Validator.checkPermissions;

/**
 * Created by Desmond.
 */
@Service
@Slf4j
public class ExperimentServiceImpl implements ExperimentService {

    private final ExperimentRepository experimentRepository;
    private final AdapterDeterLab adapterDeterLab;
    private final RealizationService realizationService;
    private final ConnectionProperties adapterConnectionProperties;

    @Inject
    ExperimentServiceImpl(@NotNull final ExperimentRepository experimentRepository, @NotNull final AdapterDeterLab adapterDeterLab, @NotNull final RealizationService realizationService, @NotNull final ConnectionProperties connectionProperties) {
        this.experimentRepository = experimentRepository;
        this.adapterDeterLab = adapterDeterLab;
        this.realizationService = realizationService;
        this.adapterConnectionProperties = connectionProperties;
        // FIXME Do not expose the internal workings of the DeterLab adapter to the experiment service; i.e., should not need to inject ConnectionProperties
    }

    /**
     * Creates an experiment and realization object on DB.
     * Also creates the experiment on Deterlab DB.
     *
     * @param experiment the experiment object passed from web service
     * @return the experiment entity stored on our DB
     */
    @Transactional
    public Experiment save(Experiment experiment) {
        log.info("Save experiment");
        String fileName = craftFileName(experiment);

        // check if team already has am experiment with the same name
        List<ExperimentEntity> experimentEntityList = experimentRepository.findByTeamName(experiment.getTeamName());

        if (experimentEntityList != null) {
            for (ExperimentEntity one : experimentEntityList) {
                if (one.getName().equals(experiment.getName())) {
                    log.warn("Experiment name is in use: {}", experiment.getName());
                    throw new ExperimentNameAlreadyExistsException(experiment.getName());
                }
            }
        }

        ExperimentEntity savedExperimentEntity = experimentRepository.save(setupEntity(experiment, fileName));
        log.info("Experiment saved: {}", savedExperimentEntity);

        RealizationEntity realizationEntity = new RealizationEntity();
        realizationEntity.setExperimentId(savedExperimentEntity.getId());
        realizationEntity.setExperimentName(savedExperimentEntity.getName());
        realizationEntity.setUserId(savedExperimentEntity.getUserId());
        realizationEntity.setTeamId(savedExperimentEntity.getTeamId());
        realizationEntity.setNumberOfNodes(0);
        realizationEntity.setIdleMinutes(0L);
        realizationEntity.setRunningMinutes(0L);

        realizationService.save(realizationEntity);
        log.info("Realization saved: {}", realizationEntity);

//        createNsFile(fileName, experiment.getNsFileContent());
        createExperimentInDeter(savedExperimentEntity);
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
        log.info("Find user by user id: {}", userId);

        if (userId == null || userId.isEmpty()) {
            // FIXME: this is the wrong exception to throw; it should be a BadRequestException type
            throw new UserIdNullOrEmptyException();
        }

        return experimentRepository.findByUserId(userId).stream().collect(Collectors.toList());
    }

    @Transactional
    public List<Experiment> findByTeam(String teamId) {
        log.info("Find teams by team id: {}", teamId);

        if (teamId == null || teamId.isEmpty()) {
            // FIXME: this is the wrong exception to throw; it should be a BadRequestException type
            throw new TeamIdNullOrEmptyException();
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

    /**
     * Invokes the adapter to create an experiment on Deterlab DB
     *
     * @param experiment the experiment object
     */
    private void createExperimentInDeter(Experiment experiment) {
        log.info("Begin creating experiment: {} for team: {}", experiment.getName(), experiment.getTeamName());

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
        userObject.put("userServerUri", adapterConnectionProperties.getUserUrl());

        adapterDeterLab.createExperiment(userObject.toString());

        log.info("Create experiment : {}, success", experiment);
    }

    /**
     * Deletes the experiment and realization object from our DB.
     * Also deletes the experiment on Deterlab DB.
     *
     * @param id       the experiment id (DB UUID), i.e. not the experiment name
     * @param teamId the team where the experiment is in (required by Deterlab so that we delete the correct experiment)
     * @param claims the decrypted claims from the jwt web token
     * @return the deleted experiment object
     * @implNote delete the realization object first, follow by the experiment object (the reverse process of create)
     * @throws ForbiddenException if user is not the experiment creator and user is not an admin
     */
    @Transactional
    public Experiment deleteExperiment(final Long id, final String teamId, final Claims claims) {
        log.info("Deleting Experiment: {} from Team: {}", id, teamId);
        Experiment experimentEntity = null;

        RealizationEntity realizationEntity = realizationService.getByExperimentId(id);
        Long realizationId = realizationEntity.getId();

        checkPermissions(realizationEntity, claims);

        if (realizationId != null && realizationId > 0) {
            realizationService.deleteRealization(realizationId);
            log.info("Realization deleted: {}", realizationId);

            experimentEntity = experimentRepository.getOne(id);
            // TODO: use other deleteExperimentInDeter(teamName, experimentName) if using script_wrapper.py
//            deleteExperimentInDeter(experimentEntity.getName(), realizationEntity.getUserId());

            experimentRepository.delete(id);
            log.info("Experiment deleted from experiment repository: {} from Team: {}", experimentEntity.getName(), experimentEntity.getTeamName());

            adapterDeterLab.deleteExperiment(experimentEntity.getTeamName(), experimentEntity.getName(), claims.getSubject());
            log.info("Experiment deleted in deter: {} from Team: {}", experimentEntity.getName(), experimentEntity.getTeamName());
        } else {
            log.warn("Experiment not deleted");
        }

        log.info("End deleteExperiment");
        return experimentEntity;
    }

    /**
     * Get the network topology map thumbnail from deterlab.
     *
     * @param teamId    the team to get the exp for
     * @param expId     the experiment to get the thumbnail
     * @return  Base64 image string
     */
    @Override
    public String getTopology(String teamId, Long expId) {
        Experiment experimentEntity = experimentRepository.getOne(expId);
        String teamName = experimentEntity.getTeamName();
        String experimentName = experimentEntity.getName();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pid", teamName);
        jsonObject.put("eid", experimentName);

        return adapterDeterLab.getTopologyThumbnail(jsonObject.toString());
    }
}
