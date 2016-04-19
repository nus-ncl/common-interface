package sg.ncl.testbed_interface.repositories.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.testbed_interface.repositories.jpa.entities.CredentialsEntity;

/**
 * @author Christopher Zhong
 */
public interface CredentialsRepository extends PagingAndSortingRepository<CredentialsEntity, Long> {

    CredentialsEntity findByUsername(String username);

}
