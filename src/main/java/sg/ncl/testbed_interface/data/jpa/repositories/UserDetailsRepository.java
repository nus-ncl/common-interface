package sg.ncl.testbed_interface.data.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.testbed_interface.data.jpa.entities.UserDetailsEntity;

/**
 * @author Christopher Zhong
 */
public interface UserDetailsRepository extends PagingAndSortingRepository<UserDetailsEntity, Long> {
}
