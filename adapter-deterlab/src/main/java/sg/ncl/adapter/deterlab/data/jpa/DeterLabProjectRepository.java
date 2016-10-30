package sg.ncl.adapter.deterlab.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabProjectEntity;

/**
 * @author Yeo Te Ye
 */
public interface DeterLabProjectRepository extends JpaRepository<DeterLabProjectEntity, Long> {

}
