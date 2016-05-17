package sg.ncl.service.user.data.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.service.user.data.jpa.entities.UserEntity;

/**
 * @author Christopher Zhong
 */
public interface UserRepository extends PagingAndSortingRepository<UserEntity, String> {
}
