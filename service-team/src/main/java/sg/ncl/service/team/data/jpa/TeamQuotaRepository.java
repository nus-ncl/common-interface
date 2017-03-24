package sg.ncl.service.team.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author Tran Ly Vu
 */

public interface TeamQuotaRepository extends JpaRepository<TeamQuotaEntity, Long> {
    TeamQuotaEntity findByTeamId(String teamId);
}
