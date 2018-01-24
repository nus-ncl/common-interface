package sg.ncl.service.experiment.logic;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.domain.ExperimentService;
import sg.ncl.service.experiment.exceptions.ExperimentNameAlreadyExistsException;
import sg.ncl.service.experiment.exceptions.ExperimentNotFoundException;
import sg.ncl.service.experiment.exceptions.TeamIdNullOrEmptyException;
import sg.ncl.service.experiment.exceptions.UserIdNullOrEmptyException;
import sg.ncl.service.experiment.web.StatefulExperiment;
import sg.ncl.service.mail.domain.MailService;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.domain.RealizationService;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static sg.ncl.service.experiment.validation.Validator.checkPermissions;
import static sg.ncl.common.validation.Validator.isAdmin;

/**
 * @Authors: Desmond, Tran Ly Vu
 */
@Service
@Slf4j
public class ExperimentServiceImpl implements ExperimentService {

    private final ExperimentRepository experimentRepository;
    private final AdapterDeterLab adapterDeterLab;
    private final RealizationService realizationService;
    private final ConnectionProperties adapterConnectionProperties;
    private final TeamService teamService;
    private final UserService userService;
    private final MailService mailService;
    private final Template internetRequestTemplate;

    @Inject
    ExperimentServiceImpl(@NotNull final ExperimentRepository experimentRepository,
                          @NotNull final AdapterDeterLab adapterDeterLab,
                          @NotNull final RealizationService realizationService,
                          @NotNull final ConnectionProperties connectionProperties,
                          @NotNull final TeamService teamService,
                          @NotNull final UserService userService,
                          @NotNull final MailService mailService,
                          @NotNull @Named("internetRequestTemplate") final Template internetRequestTemplate) {
        this.experimentRepository = experimentRepository;
        this.adapterDeterLab = adapterDeterLab;
        this.realizationService = realizationService;
        this.adapterConnectionProperties = connectionProperties;
        // FIXME Do not expose the internal workings of the DeterLab adapter to the experiment service; i.e., should not need to inject ConnectionProperties
        this.teamService = teamService;
        this.userService = userService;
        this.mailService = mailService;
        this.internetRequestTemplate =  internetRequestTemplate;
    }

    @Override
    public Experiment get(Long id) {
        if (id == null) {
            return null;
        }
        return experimentRepository.findOne(id);
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

        // put the check inside here because we do not want to add the realization service and team service on the controller
        checkPermissions(realizationEntity, teamService.isOwner(teamId, claims.getSubject()), claims);

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
     * Get experiment details from deterlab for experiment profile
     *
     * @param teamId    the team to get the exp for
     * @param expId     the experiment to get the experiment details
     * @return  a json dump of the experiment details from deterlab in the format
     *   {
     *       'ns_file' :
     *       {
     *           'msg' : 'success/fail',
     *           'ns_file' : 'ns_file_contents'
     *       },
     *       'realization_details' :
     *       {
     *           'msg' : 'success/fail',
     *           'realization_details' : 'realization_details_contents'
     *       },
     *       'activity_log'	:
     *       {
     *           'msg' : 'success/fail',
     *           'activity_log' : 'activity_log_contents'
     *       }
     *   }
     * Otherwise, returns "{}"
     */
    @Override
    public String getExperimentDetails(String teamId, Long expId) {
        Experiment experimentEntity = experimentRepository.getOne(expId);
        String teamName = experimentEntity.getTeamName();
        String experimentName = experimentEntity.getName();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pid", teamName);
        jsonObject.put("eid", experimentName);

        return adapterDeterLab.getExperimentDetails(jsonObject.toString());
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

    /**
     * Send email to ncl support to notify internet access request
     *
     * @param expId  the experiment id (DB UUID), i.e. not the experiment name
     * @param teamId the team id
     * @param reason the reason for internet access request
     *
     * @return experiment with requested internet access
     */
    @Override
    public Experiment requestInternet(String teamId, Long expId, String reason, final Claims claims) {

        log.info("Requesting internet access for Experiment {} from Team {}", expId, teamId);

        if(!isAdmin(claims) && !teamService.isMember(teamId, claims.getSubject())) {
            log.warn("Permission denied for internet access request!");
            throw new ForbiddenException();
        }

        Experiment experimentEntity = experimentRepository.getOne(expId);
        String teamName = experimentEntity.getTeamName();
        String experimentName = experimentEntity.getName();
        String userID = experimentEntity.getUserId();

        User requester = userService.getUser(userID);

        String requesterName = requester.getUserDetails().getFirstName() + " " + requester.getUserDetails().getLastName();

        final Map<String, String> map = new HashMap<>();
        map.put("requester", requesterName);
        map.put("projectName", teamName);
        map.put("expName", experimentName);
        map.put("reason", reason);

        try {
            String from = "NCL Testbed Ops <testbed-ops@ncl.sg>";

            String[] to = new String[1];
            to[0] = "support@ncl.sg";

            String subject = "Internet access request";

            String msgText = FreeMarkerTemplateUtils.processTemplateIntoString(internetRequestTemplate, map);

            String[] cc = new String[1];
            cc[0] = requester.getUserDetails().getEmail();

            mailService.send(from , to , subject, msgText, false, cc , null);

            log.info("Email sent for internet request for Experiment {} from Team {}", expId, teamId);
        } catch (IOException | TemplateException e) {
            log.warn("Error sending email for internet access request: {}", e);
        }

        return experimentEntity;
    }

    @Override
    @Transactional
    public Experiment updateExperiment(Long expId, String teamId, Experiment experiment, Claims claims) {
        log.info("Updating Experiment: {} for Team {}", expId, teamId);
        ExperimentEntity experimentEntity = experimentRepository.getOne(expId);

        RealizationEntity realizationEntity = realizationService.getByExperimentId(expId);

        // put the check inside here because we do not want to add the realization service and team service on the controller
        checkPermissions(realizationEntity, teamService.isOwner(teamId, claims.getSubject()), claims);

        final String uid = adapterDeterLab.getDeterUserIdByNclUserId(claims.getSubject());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pid", experiment.getTeamName());
        jsonObject.put("eid", experiment.getName());
        jsonObject.put("uid", uid);
        jsonObject.put("nsfile", experiment.getNsFileContent());
        jsonObject.put("maxDuration", experiment.getMaxDuration());

        // prevent un-necessary call if no changes are to be made
        if (!experimentEntity.getNsFileContent().equalsIgnoreCase(experiment.getNsFileContent())) {
            adapterDeterLab.modifyNSFile(jsonObject.toString());
            experimentEntity.setNsFileContent(experiment.getNsFileContent());
        }

        if (!experimentEntity.getMaxDuration().equals(experiment.getMaxDuration())) {
            adapterDeterLab.modifyExperimentSettings(jsonObject.toString());
            experimentEntity.setMaxDuration(experiment.getMaxDuration());
        }

        return experimentRepository.save(experimentEntity);
    }

    @Override
    public List<StatefulExperiment> getStatefulExperimentsByTeam(String teamId, String userId) {

        if (!teamService.isMember(teamId, userId)) {
            log.warn("User {} is not a member of team {}", userId, teamId);
            return new ArrayList<>();
        }

        final String result = adapterDeterLab.getExperimentsByTeam(teamId);
        log.debug("List of experiments in team {}: {}", teamId, result);

        if (result.isEmpty() || "{}".equals(result)) {
            return new ArrayList<>();
        }

        List<StatefulExperiment> stateExpList = new ArrayList<>();

        JSONObject object = new JSONObject(result);
        for (Object key : object.keySet()) {
            final String expName = (String) key;
            ExperimentEntity expEntity = experimentRepository.findOneByTeamIdAndName(teamId, expName);
            /**
             * the SIO database may have more or less experiments than Deter database;
             * we only return the experiments that exist in both SIO and Deter databases
             */
            if (expEntity != null) {
                JSONObject expDetails = object.getJSONObject(expName);
                StatefulExperiment stateExp = new StatefulExperiment();
                stateExp.setTeamId(expEntity.getTeamId());
                stateExp.setTeamName(expEntity.getTeamName());
                stateExp.setId(expEntity.getId());
                stateExp.setName(expEntity.getName());
                stateExp.setUserId(expEntity.getUserId());
                stateExp.setDescription(expEntity.getDescription());
                stateExp.setCreatedDate(expEntity.getCreatedDate());
                stateExp.setLastModifiedDate(expEntity.getLastModifiedDate());
                stateExp.setState(myExperimentState(expDetails.getString("state")));
                stateExp.setNodes(expDetails.getInt("nodes"));
                stateExp.setMaxDuration(expEntity.getMaxDuration());
                stateExp.setMinNodes(expDetails.getInt("min_nodes"));
                stateExp.setIdleHours(expDetails.getLong("idle_hours"));
                stateExp.setDetails(expDetails.getString("details"));

                stateExpList.add(stateExp);
            }
        }

        return stateExpList;
    }

    @Override
    public StatefulExperiment getStatefulExperiment(Long expId, String userId) {

        final ExperimentEntity expEntity = experimentRepository.findOne(expId);
        if (expEntity == null) {
            log.warn("Experiment {} not found in database!", expId);
            throw new ExperimentNotFoundException("Experiment " + expId + " not found.");
        }

        final String teamId = expEntity.getTeamId();

        if (!teamService.isMember(teamId, userId)) {
            log.warn("User {} is not a member of team {}", userId, teamId);
            throw new ForbiddenException();
        }

        final String expName = expEntity.getName();
        final String result = adapterDeterLab.getExperiment(teamId, expName);
        log.debug("Status for experiment {}: {}", expId, result);

        if (result.isEmpty() || "{}".equals(result)) {
            log.warn("Experiment {} not found in Deter!", expId);
            throw new ExperimentNotFoundException("Experiment " + expId + " not found.");
        }

        JSONObject object = new JSONObject(result);
        JSONObject expDetails = object.getJSONObject(expName);
        StatefulExperiment stateExp = new StatefulExperiment();

        stateExp.setTeamId(expEntity.getTeamId());
        stateExp.setTeamName(expEntity.getTeamName());
        stateExp.setId(expEntity.getId());
        stateExp.setName(expEntity.getName());
        stateExp.setUserId(expEntity.getUserId());
        stateExp.setDescription(expEntity.getDescription());
        stateExp.setCreatedDate(expEntity.getCreatedDate());
        stateExp.setLastModifiedDate(expEntity.getLastModifiedDate());
        stateExp.setState(myExperimentState(expDetails.getString("state")));
        stateExp.setNodes(expDetails.getInt("nodes"));
        stateExp.setMaxDuration(expEntity.getMaxDuration());
        stateExp.setMinNodes(expDetails.getInt("min_nodes"));
        stateExp.setIdleHours(expDetails.getLong("idle_hours"));
        stateExp.setDetails(expDetails.getString("details"));

        return stateExp;
    }

    private String myExperimentState(String deterState) {

/*
        # Experiment states on Deter
        $TB_EXPTSTATE_NEW		    = "new";
        $TB_EXPTSTATE_PRERUN		= "prerunning";
        $TB_EXPTSTATE_SWAPPING		= "swapping";
        $TB_EXPTSTATE_SWAPPED		= "swapped";
        $TB_EXPTSTATE_ACTIVATING	= "activating";
        $TB_EXPTSTATE_ACTIVE		= "active";
        $TB_EXPTSTATE_PANICED		= "paniced";
        $TB_EXPTSTATE_QUEUED		= "queued";
        $TB_EXPTSTATE_MODIFY_RESWAP	= "modify_reswap";
*/

        if ("activating".equals(deterState)){
            return "STARTING";
        } else if ("active".equals(deterState)){
            return "RUNNING";
        } else if ("swapping".equals(deterState)){
            return "STOPPING";
        } else if ("swapped".equals(deterState)){
            return "STOPPED";
        } else if ("error".equals(deterState)){
            return "ERROR";
        } else {
            return "STOPPED"; // this includes "new", "prerunning", "paniced", "queued" and "modify_reswap"
        }
    }
}
