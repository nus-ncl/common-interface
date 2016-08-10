package sg.ncl.adapter.deterlab.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabUserEntity;

/**
 * @author Yeo Te Ye
 */
public interface DeterLabUserRepository extends JpaRepository<DeterLabUserEntity, String> {

    DeterLabUserEntity findByDeterUserId(String deterUserId);

    DeterLabUserEntity findByNclUserId(String nclUserId);
}
