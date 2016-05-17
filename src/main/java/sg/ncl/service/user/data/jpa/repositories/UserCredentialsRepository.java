package sg.ncl.service.user.data.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.service.user.data.jpa.entities.UserCredentialsEntity;

/**
 * @author Christopher Zhong
 */
public interface UserCredentialsRepository extends PagingAndSortingRepository<UserCredentialsEntity, Long> {

    UserCredentialsEntity findByUsername(String username);

}
