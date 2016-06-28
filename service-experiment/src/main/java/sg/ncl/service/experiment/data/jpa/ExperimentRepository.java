package sg.ncl.service.experiment.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Christopher Zhong
 */
public interface ExperimentRepository extends JpaRepository<ExperimentEntity, Long> {
}
