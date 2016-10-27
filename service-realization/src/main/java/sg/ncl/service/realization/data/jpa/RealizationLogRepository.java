package sg.ncl.service.realization.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by dcszwang on 10/27/2016.
 */
public interface RealizationLogRepository extends JpaRepository<RealizationLogEntity, Long> {
    List<RealizationLogEntity> findByTeamIdAndExpId(String teamId, Long expId);
}
