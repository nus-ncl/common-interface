package sg.ncl.service.user.data.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.service.user.data.jpa.entities.AddressEntity;

/**
 * @author Christopher Zhong
 */
public interface AddressRepository extends PagingAndSortingRepository<AddressEntity, Long> {
}
