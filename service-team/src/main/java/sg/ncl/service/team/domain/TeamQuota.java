package sg.ncl.service.team.domain;

import java.math.BigDecimal;

/**
 * @author Tran Ly Vu
 */

public interface TeamQuota {

    Long getId();

    String getTeamId();

    BigDecimal getQuota();

    String toString();
}
