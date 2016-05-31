package sg.ncl.service.team.data.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;

/**
 * @author Christopher Zhong
 */
public interface TeamRepository extends PagingAndSortingRepository<TeamEntity, String> {

//    TeamEntity findByName(String name);

}
