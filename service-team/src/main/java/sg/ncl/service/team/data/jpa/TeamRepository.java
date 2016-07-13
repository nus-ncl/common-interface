package sg.ncl.service.team.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.service.team.domain.TeamVisibility;

import java.util.List;

/**
 * @author Christopher Zhong
 */
public interface TeamRepository extends JpaRepository<TeamEntity, String> {

    TeamEntity findByName(String name);

    List<TeamEntity> findByVisibility(TeamVisibility visibility);

}
