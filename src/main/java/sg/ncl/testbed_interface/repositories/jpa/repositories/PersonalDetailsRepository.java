package sg.ncl.testbed_interface.repositories.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.testbed_interface.repositories.jpa.entities.PersonalDetailsEntity;

/**
 * @author Christopher Zhong
 */
public interface PersonalDetailsRepository extends PagingAndSortingRepository<PersonalDetailsEntity,Long> {
}
