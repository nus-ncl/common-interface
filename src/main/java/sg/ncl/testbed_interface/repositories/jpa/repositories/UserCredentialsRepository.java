package sg.ncl.testbed_interface.repositories.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.testbed_interface.repositories.jpa.entities.UserCredentialsEntity;

/**
 * @author Christopher Zhong
 */
public interface UserCredentialsRepository extends PagingAndSortingRepository<UserCredentialsEntity, Long> {

    UserCredentialsEntity findByUsername(String username);

}
