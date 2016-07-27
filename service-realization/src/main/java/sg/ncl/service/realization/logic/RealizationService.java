package sg.ncl.service.realization.logic;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.service.realization.RealizationConnectionProperties;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.data.jpa.RealizationRepository;
import sg.ncl.service.realization.domain.RealizationState;

import javax.inject.Inject;

/**
 * Created by Desmond.
 */
@Service
public class RealizationService {

    private static final Logger logger = LoggerFactory.getLogger(RealizationService.class);
    private final RealizationRepository realizationRepository;

    @Inject
    private AdapterDeterlab adapterDeterlab;

    @Inject
    protected RealizationService(final RealizationRepository realizationRepository) {
        this.realizationRepository = realizationRepository;
    }

    public RealizationEntity getById(final Long id) {
        logger.info("Get realization by id");

        RealizationEntity realizationEntity = realizationRepository.findOne(id);

        if (realizationEntity != null && realizationEntity.getId() > 0) {
            return realizationEntity;
        }

        else {
            return null;
        }
    }

    public RealizationEntity getByExperimentId(final Long experimentId) {
        logger.info("Get realization by experiment name");

        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);

        if (realizationEntity != null && realizationEntity.getId() > 0) {
            return realizationEntity;
        }

        else {
            return null;
        }
    }

    public RealizationEntity save(RealizationEntity realizationEntity) {
        logger.info("Save realization");

        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        logger.info("Realization saved");
        return savedRealizationEntity;
    }

    public String startExperimentInDeter(final String teamName, final String experimentName) {
        StringBuilder httpCommand = new StringBuilder();
        httpCommand.append("?inout=in");
        httpCommand.append("&");
        httpCommand.append("pid=" + teamName);
        httpCommand.append("&");
        httpCommand.append("eid=" + experimentName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("httpCommand", httpCommand.toString());

        adapterDeterlab.startExperiment(jsonObject.toString());

        return httpCommand.toString();
    }

    public String stopExperimentInDeter(final String teamName, final String experimentName) {
        StringBuilder httpCommand = new StringBuilder();
        httpCommand.append("?inout=out");
        httpCommand.append("&");
        httpCommand.append("pid=" + teamName);
        httpCommand.append("&");
        httpCommand.append("eid=" + experimentName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("httpCommand", httpCommand.toString());

        adapterDeterlab.stopExperiment(jsonObject.toString());

        return httpCommand.toString();
    }

    public void setState(final Long experimentId, final RealizationState state) {
        logger.info("Set realization state. {} : {}", experimentId, state);
        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);
        realizationEntity.setState(state);
        realizationRepository.save(realizationEntity);
    }

    public RealizationState getState(final Long experimentId) {
        logger.info("Get realization state. {}", experimentId);
        return realizationRepository.findByExperimentId(experimentId).getState();
    }

    public void setIdleMinutes(final Long experimentId, final Long minutes) {
        logger.info("Set realization idle minutes. {} : {}", experimentId, minutes);
        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);
        realizationEntity.setIdleMinutes(minutes);
        realizationRepository.saveAndFlush(realizationEntity);
    }

    public Long getIdleMinutes(final Long experimentId) {
        logger.info("Get realization idle minutes. {}", experimentId);
        return realizationRepository.findByExperimentId(experimentId).getIdleMinutes();
    }

    public void setRunningMinutes(final Long experimentId, final Long minutes) {
        logger.info("Set realization running minutes. {} : {}", experimentId, minutes);
        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);
        realizationEntity.setRunningMinutes(minutes);
        realizationRepository.saveAndFlush(realizationEntity);
    }

    public Long getRunningMinutes(final Long experimentId) {
        logger.info("Get realization running minutes. {}", experimentId);
        return realizationRepository.findByExperimentId(experimentId).getRunningMinutes();
    }

    public void deleteRealization(final Long realizationId) {
        logger.info("Delete realization. {}", realizationId);
        realizationRepository.delete(realizationId);
    }

    // TODO: DELETE team from user
}
