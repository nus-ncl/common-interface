package sg.ncl.service.realization.logic;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.data.jpa.RealizationRepository;
import sg.ncl.service.realization.domain.RealizationService;
import sg.ncl.service.realization.domain.RealizationState;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Created by Desmond.
 */
@Service
@Slf4j
public class RealizationServiceImpl implements RealizationService {

    private final RealizationRepository realizationRepository;
    private final AdapterDeterLab adapterDeterLab;

    @Inject
    RealizationServiceImpl(@NotNull final RealizationRepository realizationRepository, @NotNull final AdapterDeterLab adapterDeterLab) {
        this.realizationRepository = realizationRepository;
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

    // for retrieving live deterlab experiment status
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
            return null;
        }
    }

    @Transactional
    public RealizationEntity save(RealizationEntity realizationEntity) {
        log.info("Save realization");

        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        log.info("Realization saved");
        return savedRealizationEntity;
    }

    @Transactional
    public RealizationEntity startExperimentInDeter(final String teamName, final String experimentName, final String userId) {
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

        RealizationEntity realizationEntity = realizationRepository.findByExperimentName(experimentName);

        String stringFromExperiment = adapterDeterLab.startExperiment(jsonObject.toString());
        JSONObject jsonObjectFromExperiment = new JSONObject(stringFromExperiment);

        String status = jsonObjectFromExperiment.getString("status");
        String report = jsonObjectFromExperiment.getString("report");
        RealizationState realizationState;

        switch (status) {
            case "active":
                realizationState = RealizationState.RUNNING;
                break;
            case "activating":
                realizationState = RealizationState.STARTING;
                break;
            default:
                realizationState = RealizationState.NOT_RUNNING;
                break;
        }
        realizationEntity.setState(realizationState);
        realizationEntity.setDetails(report);
        return realizationRepository.save(realizationEntity);
    }

    @Transactional
    public RealizationEntity stopExperimentInDeter(final String teamName, final String experimentName, final String userId) {
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

        RealizationEntity realizationEntity = realizationRepository.findByExperimentName(experimentName);

        // should be 'swapped'
        String resultState = adapterDeterLab.stopExperiment(jsonObject.toString());
        RealizationState realizationState;
        if (resultState.equals("swapped")) {
            realizationState = RealizationState.NOT_RUNNING;
        } else {
            realizationState = RealizationState.ERROR;
        }
        realizationEntity.setState(realizationState);
        realizationEntity.setDetails("");
        return realizationRepository.save(realizationEntity);
    }

    public void setState(final Long experimentId, final RealizationState state) {
        log.info("Set realization state. {} : {}", experimentId, state);
        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);
        realizationEntity.setState(state);
        realizationRepository.save(realizationEntity);
    }

    public RealizationState getState(final Long experimentId) {
        log.info("Get realization state. {}", experimentId);
        return realizationRepository.findByExperimentId(experimentId).getState();
    }

    public void setIdleMinutes(final Long experimentId, final Long minutes) {
        log.info("Set realization idle minutes. {} : {}", experimentId, minutes);
        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);
        realizationEntity.setIdleMinutes(minutes);
        realizationRepository.saveAndFlush(realizationEntity);
    }

    public Long getIdleMinutes(final Long experimentId) {
        log.info("Get realization idle minutes. {}", experimentId);
        return realizationRepository.findByExperimentId(experimentId).getIdleMinutes();
    }

    public void setRunningMinutes(final Long experimentId, final Long minutes) {
        log.info("Set realization running minutes. {} : {}", experimentId, minutes);
        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);
        realizationEntity.setRunningMinutes(minutes);
        realizationRepository.saveAndFlush(realizationEntity);
    }

    public Long getRunningMinutes(final Long experimentId) {
        log.info("Get realization running minutes. {}", experimentId);
        return realizationRepository.findByExperimentId(experimentId).getRunningMinutes();
    }

    public void setRealizationDetails(final Long experimentId, final String details) {
        log.info("Set realization details. {} : {}", experimentId, details);
        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);
        realizationEntity.setDetails(details);
        realizationRepository.saveAndFlush(realizationEntity);
    }

    public String getRealizationDetails(final Long experimentId) {
        log.info("Get realization details. {}", experimentId);
        return realizationRepository.findByExperimentId(experimentId).getDetails();
    }

    public void deleteRealization(final Long realizationId) {
        log.info("Delete realization. {}", realizationId);
        realizationRepository.delete(realizationId);
    }
}
