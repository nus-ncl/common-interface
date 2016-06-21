package sg.ncl.adapter.deterlab.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.adapter.deterlab.domain.DeterlabUser;
import sg.ncl.adapter.deterlab.dtos.entities.DeterlabUserEntity;

/**
 * @author Yeo Te Ye
 */
public interface DeterlabUserRepository extends JpaRepository<DeterlabUserEntity, String> {

    DeterlabUserEntity findByDeterUserId(String deterUserId);

}
