package sg.ncl.service.experiment.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Christopher Zhong
 */
public interface ExperimentRepository extends JpaRepository<ExperimentEntity, Long> {

    List<ExperimentEntity> findByUserId(String userId);
    List<ExperimentEntity> findByTeamId(String teamId);

    Long countByName(String name);
}
