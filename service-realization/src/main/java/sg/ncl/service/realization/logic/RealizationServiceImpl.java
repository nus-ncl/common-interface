package sg.ncl.service.realization.logic;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.data.jpa.RealizationLogEntity;
import sg.ncl.service.realization.data.jpa.RealizationLogRepository;
import sg.ncl.service.realization.data.jpa.RealizationRepository;
import sg.ncl.service.realization.domain.RealizationService;
import sg.ncl.service.realization.domain.RealizationState;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by Desmond.
 */
@Service
@Slf4j
public class RealizationServiceImpl implements RealizationService {

    private final RealizationRepository realizationRepository;
    private final RealizationLogRepository realizationLogRepository;
    private final AdapterDeterLab adapterDeterLab;

    @Inject
    RealizationServiceImpl(
            @NotNull final RealizationRepository realizationRepository,
            @NotNull final RealizationLogRepository realizationLogRepository,
            @NotNull final AdapterDeterLab adapterDeterLab
    ) {
        this.realizationRepository = realizationRepository;
        this.realizationLogRepository = realizationLogRepository;
        this.adapterDeterLab = adapterDeterLab;
    }

    @Transactional
    public RealizationEntity getById(final Long id) {
        log.info("Get realization by id");

        RealizationEntity realizationEntity = realizationRepository.findOne(id);

        if (realizationEntity != null && realizationEntity.getId() > 0) {
            return realizationEntity;
        } else {
            return null;
        }
    }

    @Transactional
    public RealizationEntity getByExperimentId(final Long experimentId) {
        log.info("Get realization by experiment name");

        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);

        if (realizationEntity != null && realizationEntity.getId() > 0) {
            return realizationEntity;
        } else {
            return null;
        }
    }

    /**
     * Retrieves the live status of experiments' status from Deterlab
     *
     * @param teamName     the team whose experiments that are of interest
     * @param experimentId the experiment name to get status
     * @return the realization entity object that contains the updated experiment status and report pulled from Deterlab. Defaults status to not running if there are any connection errors to Deterlab.
     * @implNote DB Sync issue cannot be avoided due to dependency of results from Deterlab before we can update our own DB
     */
    @Transactional
    @Override
    public RealizationEntity getByExperimentId(final String teamName, final Long experimentId) {
        log.info("Get realization for team: {}, expid: {}", teamName, experimentId);

        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);

        if (realizationEntity != null && realizationEntity.getId() > 0) {
            log.info("Retrieved realization entity: {}", realizationEntity);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pid", teamName);
            jsonObject.put("eid", realizationEntity.getExperimentName());
            String result = adapterDeterLab.getExperimentStatus(jsonObject.toString());

            log.info("Retrieved deterlab exp status...Exp: {} State: {}", realizationEntity.getExperimentName(), result);

            JSONObject jsonObjectFromExperiment = new JSONObject(result);
            String status = jsonObjectFromExperiment.getString("status");
            RealizationState realizationState;
            if (status == null) {
                realizationState = RealizationState.NOT_RUNNING;
            } else {
                switch (status) {
                    case "active":
                        realizationState = RealizationState.RUNNING;
                        String report = jsonObjectFromExperiment.getString("report");
                        realizationEntity.setDetails(report);
                        break;
                    case "activating":
                        realizationState = RealizationState.STARTING;
                        break;
                    case "swapping":
                        realizationState = RealizationState.STOPPING;
                        realizationEntity.setDetails("");
                        break;
                    case "error":
                        realizationState = RealizationState.ERROR;
                        break;
                    default:
                        realizationState = RealizationState.NOT_RUNNING;
                        break;
                }
            }
            if (realizationEntity.getState() != realizationState) {
                realizationEntity.setState(realizationState);
                log.info("Realization: {} updated to state: {}", realizationEntity, realizationState);
                return realizationRepository.save(realizationEntity);
            }
            return realizationEntity;
        } else {
            log.warn("Get realization fail for team: {}, experiment id: {}", teamName, experimentId);
            return new RealizationEntity();
        }
    }

    @Transactional
    public RealizationEntity save(RealizationEntity realizationEntity) {
        log.info("Save realization");

        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        log.info("Realization saved");
        return savedRealizationEntity;
    }

    /**
     * Starts an experiment in Deterlab (allocate resources and swapped-in).
     * The experiment from Deterlab will generate a report which states the Qualified Name, IP Address, Link Delay .etc
     *
     * @param teamName the team to start the exp for
     * @param expId    the experiment name to start, e.g. demo, Identical experiment names are allow for different teams
     * @return the realization entity object that contains the updated experiment status and report pulled from Deterlab
     * @implNote DB sync not an issue since experiment status and report will be re-updated when users refresh the page
     */
    @Transactional
    public RealizationEntity startExperimentInDeter(final String teamName, final String expId) {
        log.info("Starting experiment {} for team {}", expId, teamName);
        RealizationEntity realizationEntityDb = realizationRepository.findByExperimentId(Long.parseLong(expId));
        if(!realizationEntityDb.getState().equals(RealizationState.NOT_RUNNING)) {
            log.warn("Failed to start experiment {}: current status {}", realizationEntityDb.getExperimentName(), realizationEntityDb.getState());
            return null;
        }
        String experimentName = realizationEntityDb.getExperimentName();
        String userId = realizationEntityDb.getUserId();

        StringBuilder httpCommand = new StringBuilder();
        httpCommand.append("?inout=in");
        httpCommand.append("&");
        httpCommand.append("pid=" + teamName);
        httpCommand.append("&");
        httpCommand.append("eid=" + experimentName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("httpCommand", httpCommand.toString());
        jsonObject.put("deterLogin", adapterDeterLab.getDeterUserIdByNclUserId(userId));
        jsonObject.put("pid", teamName);
        jsonObject.put("eid", experimentName);

        String stringFromExperiment = adapterDeterLab.startExperiment(jsonObject.toString());
        JSONObject jsonObjectFromExperiment = new JSONObject(stringFromExperiment);

        String status = jsonObjectFromExperiment.getString("status");
        String report = jsonObjectFromExperiment.getString("report");
        RealizationState realizationState;

        switch (status) {
            case "active":
                realizationState = RealizationState.RUNNING;
                addRealizationLog(realizationEntityDb.getTeamId(), realizationEntityDb.getExperimentId(), realizationEntityDb.getNumberOfNodes());
                break;
            case "activating":
                realizationState = RealizationState.STARTING;
                break;
            default:
                realizationState = RealizationState.NOT_RUNNING;
                break;
        }
        log.info("Start Experiment: {}, Team: {} Status: {}", experimentName, teamName, realizationState);
        realizationEntityDb.setState(realizationState);
        realizationEntityDb.setDetails(report);
        return realizationRepository.save(realizationEntityDb);
    }

    /**
     * Stops an experiment in Deterlab (de-allocate resources and swapped-out).
     * The realization entity report will be clear.
     *
     * @param teamName the team to stop the exp
     * @param expId    the experiment name to stop
     * @return the realization entity object that contains the updated experiment status and with empty report
     * @implNote DB sync not an issue since experiment status and report will be re-updated when users refresh the page
     */
    @Transactional
    public RealizationEntity stopExperimentInDeter(final String teamName, final String expId) {
        log.info("Stopping experiment: {} for team: ", expId, teamName);
        RealizationEntity realizationEntityDb = realizationRepository.findByExperimentId(Long.parseLong(expId));
        String experimentName = realizationEntityDb.getExperimentName();
        String userId = realizationEntityDb.getUserId();

        StringBuilder httpCommand = new StringBuilder();
        httpCommand.append("?inout=out");
        httpCommand.append("&");
        httpCommand.append("pid=" + teamName);
        httpCommand.append("&");
        httpCommand.append("eid=" + experimentName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("httpCommand", httpCommand.toString());
        jsonObject.put("deterLogin", adapterDeterLab.getDeterUserIdByNclUserId(userId));
        jsonObject.put("pid", teamName);
        jsonObject.put("eid", experimentName);

        String resultState = adapterDeterLab.stopExperiment(jsonObject.toString());
        RealizationState realizationState;
        if (resultState.equals("swapped")) {
            realizationState = RealizationState.NOT_RUNNING;
        } else {
            realizationState = RealizationState.ERROR;
        }

        updateRealizationLog(realizationEntityDb.getTeamId(), realizationEntityDb.getExperimentId());

        log.info("Stop Experiment: {}, Team: {} Status: {}", experimentName, teamName, realizationState);
        realizationEntityDb.setState(realizationState);
        realizationEntityDb.setDetails("");
        return realizationRepository.save(realizationEntityDb);
    }

    @Transactional
    public void setState(final Long experimentId, final RealizationState state) {
        log.info("Set realization state. {} : {}", experimentId, state);
        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);
        realizationEntity.setState(state);
        realizationRepository.save(realizationEntity);
    }

    @Transactional
    public RealizationState getState(final Long experimentId) {
        log.info("Get realization state. {}", experimentId);
        return realizationRepository.findByExperimentId(experimentId).getState();
    }

    @Transactional
    public void setIdleMinutes(final Long experimentId, final Long minutes) {
        log.info("Set realization idle minutes. {} : {}", experimentId, minutes);
        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);
        realizationEntity.setIdleMinutes(minutes);
        realizationRepository.saveAndFlush(realizationEntity);
    }

    @Transactional
    public Long getIdleMinutes(final Long experimentId) {
        log.info("Get realization idle minutes. {}", experimentId);
        return realizationRepository.findByExperimentId(experimentId).getIdleMinutes();
    }

    @Transactional
    public void setRunningMinutes(final Long experimentId, final Long minutes) {
        log.info("Set realization running minutes. {} : {}", experimentId, minutes);
        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);
        realizationEntity.setRunningMinutes(minutes);
        realizationRepository.saveAndFlush(realizationEntity);
    }

    @Transactional
    public Long getRunningMinutes(final Long experimentId) {
        log.info("Get realization running minutes. {}", experimentId);
        return realizationRepository.findByExperimentId(experimentId).getRunningMinutes();
    }

    @Transactional
    public void setRealizationDetails(final Long experimentId, final String details) {
        log.info("Set realization details. {} : {}", experimentId, details);
        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);
        realizationEntity.setDetails(details);
        realizationRepository.saveAndFlush(realizationEntity);
    }

    @Transactional
    public String getRealizationDetails(final Long experimentId) {
        log.info("Get realization details. {}", experimentId);
        return realizationRepository.findByExperimentId(experimentId).getDetails();
    }

    @Transactional
    public void deleteRealization(final Long realizationId) {
        log.info("Delete realization. {}", realizationId);
        realizationRepository.delete(realizationId);
    }

    public void addRealizationLog(String teamId, Long expId, int numNodes) {
        RealizationLogEntity realizationLogEntity = new RealizationLogEntity();
        realizationLogEntity.setTeamId(teamId);
        realizationLogEntity.setExpId(expId);
        realizationLogEntity.setStartDate(ZonedDateTime.now());
        realizationLogEntity.setNumNodes(numNodes);
        realizationLogRepository.save(realizationLogEntity);
        log.info("Added realization log for exp {} in team {}", expId, teamId);
    }

    public void updateRealizationLog(String teamId, Long expId) {
        List<RealizationLogEntity> realizationLogEntities = realizationLogRepository.findByTeamIdAndExpId(teamId, expId);
        if(realizationLogEntities.size() <= 0) {
            log.warn("Cannot find realization logs for exp {} in team {}", expId, teamId);
            return;
        }
        for(RealizationLogEntity realizationLogEntity : realizationLogEntities) {
            if(null == realizationLogEntity.getEndDate()) {
                realizationLogEntity.setEndDate(ZonedDateTime.now());
                realizationLogRepository.save(realizationLogEntity);
                log.info("Updated realization log for exp {} in team {}", expId, teamId);
                return;
            }
        }
    }
}
