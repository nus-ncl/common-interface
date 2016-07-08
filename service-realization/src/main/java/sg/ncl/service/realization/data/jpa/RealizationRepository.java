package sg.ncl.service.realization.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.service.realization.data.jpa.RealizationEntity;

/**
 * @author Christopher Zhong
 */
public interface RealizationRepository extends JpaRepository<RealizationEntity, Long> {
}