package sg.ncl.adapter.deterlab.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.adapter.deterlab.dtos.entities.DeterlabProjectEntity;

/**
 * @author Yeo Te Ye
 */
public interface DeterlabProjectRepository extends JpaRepository<DeterlabProjectEntity, String> {

}
