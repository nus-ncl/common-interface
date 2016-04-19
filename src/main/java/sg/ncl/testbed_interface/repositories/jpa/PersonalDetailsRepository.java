package sg.ncl.testbed_interface.repositories.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Christopher Zhong
 */
public interface PersonalDetailsRepository extends PagingAndSortingRepository<PersonalDetailsEntity,Long> {
}
