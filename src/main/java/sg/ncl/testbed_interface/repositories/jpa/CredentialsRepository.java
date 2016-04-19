package sg.ncl.testbed_interface.repositories.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Christopher Zhong
 */
public interface CredentialsRepository extends PagingAndSortingRepository<CredentialsEntity, Long> {

    CredentialsEntity findByUsername(String username);

}
