package sg.ncl.service.realization.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.web.RealizationInfo;

/**
 * @author Christopher Zhong
 */
public interface RealizationRepository extends JpaRepository<RealizationEntity, Long> {

    RealizationEntity findByExperimentId(Long experimentId);
}
