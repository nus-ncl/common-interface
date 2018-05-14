package sg.ncl.service.authentication.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.service.authentication.domain.CredentialsStatus;

import java.util.List;

/**
 * @author Christopher Zhong
 */
public interface CredentialsRepository extends JpaRepository<CredentialsEntity, String> {

    CredentialsEntity findByUsername(String username);
    CredentialsEntity findById(String uid);

    List<CredentialsEntity> findByStatus(CredentialsStatus status);

}
