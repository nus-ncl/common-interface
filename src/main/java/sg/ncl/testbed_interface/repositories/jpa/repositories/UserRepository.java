package sg.ncl.testbed_interface.repositories.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.testbed_interface.repositories.jpa.entities.UserEntity;

/**
 * @author Christopher Zhong
 */
public interface UserRepository extends PagingAndSortingRepository<UserEntity, String> {
}
