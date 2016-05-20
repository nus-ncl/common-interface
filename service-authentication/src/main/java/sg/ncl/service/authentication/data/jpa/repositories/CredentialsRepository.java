package sg.ncl.service.authentication.data.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.service.authentication.data.jpa.entities.CredentialsEntity;

/**
 * @author Christopher Zhong
 */
public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Long> {

    CredentialsEntity findByUsername(String username);

    CredentialsEntity findByUserId(String userId);

}
