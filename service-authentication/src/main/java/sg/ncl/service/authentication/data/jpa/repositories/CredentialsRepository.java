package sg.ncl.service.authentication.data.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.service.authentication.data.jpa.entities.CredentialsEntity;
import sg.ncl.service.authentication.domain.CredentialsStatus;

import java.util.List;

/**
 * @author Christopher Zhong
 */
public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Long> {

    CredentialsEntity findByUsername(String username);

    CredentialsEntity findByUserId(String userId);

    List<CredentialsEntity> findByStatus(CredentialsStatus status);

}
