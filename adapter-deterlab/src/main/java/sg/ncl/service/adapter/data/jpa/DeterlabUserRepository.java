package sg.ncl.service.adapter.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.service.adapter.dtos.entities.DeterlabUserEntity;

/**
 * @author Yeo Te Ye
 */
public interface DeterlabUserRepository extends JpaRepository<DeterlabUserEntity, String> {

}
