package sg.ncl.service.authentication.data.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.service.authentication.data.jpa.entities.CredentialsEntity;

/**
 * @author Christopher Zhong
 */
public interface CredentialsRepository extends PagingAndSortingRepository<CredentialsEntity, Long> {

    CredentialsEntity findByUsername(String username);

    CredentialsEntity findByUserId(String userId);

}
