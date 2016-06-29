package sg.ncl.service.user.data.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Christopher Zhong
 */
public interface AddressRepository extends PagingAndSortingRepository<AddressEntity, Long> {
}
