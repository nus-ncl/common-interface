package sg.ncl.service.realization.domain;

import io.jsonwebtoken.Claims;
import sg.ncl.service.realization.data.jpa.RealizationEntity;

import java.util.List;

/**
 * Created by Desmond.
 */
public interface RealizationService {

    List<Realization> getAll();

    RealizationEntity getById(Long id);

    RealizationEntity getByExperimentId(Long experimentId);

    RealizationEntity getByExperimentId(String teamName, Long experimentId);

    RealizationEntity save(RealizationEntity realizationEntity);

    RealizationEntity startExperimentInDeter(String teamName, String expId, Claims claims);

    RealizationEntity stopExperimentInDeter(String teamName, String expId, Claims claims);

    void deleteRealization(Long realizationId);

  //  String getUsageStatistics(String id);
}
