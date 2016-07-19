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
    private RealizationConnectionProperties realizationConnectionProperties;

    @Inject
    protected RealizationService(final RealizationRepository realizationRepository) {
        this.realizationRepository = realizationRepository;
    }

    public RealizationEntity save(RealizationEntity realizationEntity) {
        logger.info("Save realization");

        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        logger.info("Realization saved");
        return savedRealizationEntity;
    }

    public void startExperimentInDeter(final String teamId, final String experimentId) {
        StringBuilder httpCommand = new StringBuilder();
        httpCommand.append("http://");
        httpCommand.append(realizationConnectionProperties.getBossurl());
        httpCommand.append("/swapexp.php?");
        httpCommand.append("inout=in");
        httpCommand.append("&");
        httpCommand.append("pid=" + teamId);
        httpCommand.append("&");
        httpCommand.append("eid=" + experimentId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("httpCommand", httpCommand.toString());

        adapterDeterlab.startExperiment(jsonObject.toString());
    }

    public void stopExperimentInDeter(final String teamId, final String experimentId) {

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
        realizationRepository.save(realizationEntity);
    }

    public Long getIdleMinutes(final Long experimentId) {
        logger.info("Get realization idle minutes. {}", experimentId);
        return realizationRepository.findByExperimentId(experimentId).getIdleMinutes();
    }

    public void setRunningMinutes(final Long experimentId, final Long minutes) {
        logger.info("Set realization running minutes. {} : {}", experimentId, minutes);
        RealizationEntity realizationEntity = realizationRepository.findByExperimentId(experimentId);
        realizationEntity.setRunningMinutes(minutes);
        realizationRepository.save(realizationEntity);
    }

    public Long getRunningMinutes(final Long experimentId) {
        logger.info("Get realization running minutes. {}", experimentId);
        return realizationRepository.findByExperimentId(experimentId).getRunningMinutes();
    }
}
