package sg.ncl.service.team.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.team.domain.TeamQuota;
import java.math.BigDecimal;


/**
 * @author Tran Ly Vu
 */

@Getter
public class TeamQuotaInfo implements TeamQuota {
    private final Long id;
    private final String teamId;
    private final BigDecimal quota;
    private final String usage;

    @JsonCreator
    public TeamQuotaInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("teamId") final String teamId,
            @JsonProperty("quota") final BigDecimal quota,
            @JsonProperty("usage") final String usage) {
        this.id = id;
        this.teamId = teamId;
        this.quota = quota;
        this.usage = usage;
    }

    public TeamQuotaInfo(final TeamQuota teamQuota, String usage) {
        this(teamQuota.getId(),
                teamQuota.getTeamId(),
                teamQuota.getQuota(),
                usage);
    }

}

