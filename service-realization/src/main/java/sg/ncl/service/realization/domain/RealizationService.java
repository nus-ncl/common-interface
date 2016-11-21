package sg.ncl.service.realization.domain;

import io.jsonwebtoken.Claims;
import sg.ncl.service.realization.data.jpa.RealizationEntity;

/**
 * Created by Desmond.
 */
public interface RealizationService {

    RealizationEntity getById(Long id);

    RealizationEntity getByExperimentId(Long experimentId);

    RealizationEntity getByExperimentId(String teamName, Long experimentId);

    RealizationEntity save(RealizationEntity realizationEntity);

    RealizationEntity startExperimentInDeter(String teamName, String expId, Claims claims);

    RealizationEntity stopExperimentInDeter(String teamName, String expId);

    void deleteRealization(Long realizationId);

    String getUsageStatistics(String id);
}
