package sg.ncl.service.realization.logic;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
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

    public RealizationEntity getById(final Long id) {
        log.info("Get realization by id");

        RealizationEntity realizationEntity = realizationRepository.findOne(id);

        if (realizationEntity != null && realizationEntity.getId() > 0) {
            return realizationEntity;
        } else {
            return null;
        }
    }

    public RealizationEntity getByExperimentId(final Long experimentId) {
        log.info("Get realization by experiment name");

        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);

        if (realizationEntity != null && realizationEntity.getId() > 0) {
            return realizationEntity;
        } else {
            return null;
        }
    }

    public RealizationEntity save(RealizationEntity realizationEntity) {
        log.info("Save realization");

        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        log.info("Realization saved");
        return savedRealizationEntity;
    }

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
        realizationEntity.setState(RealizationState.STARTING);
        realizationRepository.save(realizationEntity);

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
        realizationEntity.setState(RealizationState.STOPPING);
        realizationRepository.save(realizationEntity);

        adapterDeterLab.stopExperiment(jsonObject.toString());

        // FIXME may need to check if stopping experiments have error
        realizationEntity.setState(RealizationState.NOT_RUNNING);
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
