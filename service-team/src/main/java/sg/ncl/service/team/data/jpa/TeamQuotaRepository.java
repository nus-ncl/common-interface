package sg.ncl.service.team.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.service.team.domain.TeamQuota;

/**
 * @Author Tran Ly Vu
 */

public interface TeamQuotaRepository extends JpaRepository<TeamQuotaEntity, Long> {
    TeamQuotaEntity findByTeamId(String teamId);
}
