package sg.ncl.service.adapter.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.service.adapter.dtos.entities.DeterlabProjectEntity;

/**
 * @author Yeo Te Ye
 */
public interface DeterlabProjectRepository extends JpaRepository<DeterlabProjectEntity, String> {

}
