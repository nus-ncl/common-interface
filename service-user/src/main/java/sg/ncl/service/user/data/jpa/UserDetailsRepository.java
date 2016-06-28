package sg.ncl.service.user.data.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.service.user.data.jpa.entities.UserDetailsEntity;

/**
 * @author Christopher Zhong
 */
public interface UserDetailsRepository extends PagingAndSortingRepository<UserDetailsEntity, Long> {
}
