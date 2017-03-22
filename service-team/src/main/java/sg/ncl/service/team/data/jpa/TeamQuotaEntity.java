package sg.ncl.service.team.data.jpa;

import lombok.Getter;
import lombok.Setter;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.team.domain.TeamQuota;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @Author Tran Ly Vu
 */
@Getter
@Setter
@Entity
@Table(name = "team_quotas", schema = "prod")
public class TeamQuotaEntity extends AbstractEntity implements TeamQuota {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "team_id", nullable = false, unique = true)
    private String teamId;

    @Column(name = "quota", nullable = true)
    private BigDecimal quota;
}
