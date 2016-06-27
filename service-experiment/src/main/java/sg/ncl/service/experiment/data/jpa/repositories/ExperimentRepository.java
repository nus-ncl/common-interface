package sg.ncl.service.experiment.data.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.service.experiment.data.jpa.entities.ExperimentEntity;

/**
 * @author Christopher Zhong
 */
public interface ExperimentRepository extends JpaRepository<ExperimentEntity, Long> {
}
