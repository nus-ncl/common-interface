package sg.ncl.service.realization.domain;

import sg.ncl.service.realization.data.jpa.RealizationEntity;

/**
 * Created by Desmond.
 */
public interface RealizationService {

    RealizationEntity getById(Long id);

    RealizationEntity getByExperimentId(Long experimentId);

    RealizationEntity save(RealizationEntity realizationEntity);

    RealizationEntity startExperimentInDeter(String teamName, String experimentName, String userId);

    RealizationEntity stopExperimentInDeter(String teamName, String experimentName, String userId);

    void setState(Long experimentId, RealizationState state);

    RealizationState getState(Long experimentId);

    void setIdleMinutes(Long experimentId, Long minutes);

    Long getIdleMinutes(Long experimentId);

    void setRunningMinutes(Long experimentId, Long minutes);

    Long getRunningMinutes(Long experimentId);

    void setRealizationDetails(Long experimentId, String details);

    String getRealizationDetails(Long experimentId);

    void deleteRealization(Long realizationId);
}