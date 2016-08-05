package sg.ncl.service.realization.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Christopher Zhong
 */
public interface RealizationRepository extends JpaRepository<RealizationEntity, Long> {

    RealizationEntity findByExperimentId(Long experimentId);

    RealizationEntity findByExperimentName(String experimentName);
}
