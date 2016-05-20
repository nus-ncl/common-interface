package sg.ncl.service.realization.data.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.service.realization.data.jpa.entities.RealizationEntity;

/**
 * @author Christopher Zhong
 */
public interface RealizationRepository extends PagingAndSortingRepository<RealizationEntity, Long> {
}
