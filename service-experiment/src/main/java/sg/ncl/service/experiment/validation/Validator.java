package sg.ncl.service.experiment.validation;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.domain.Realization;
import sg.ncl.service.team.domain.Team;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Te Ye
 * @version 1.0
 */
@Slf4j
public class Validator {

    private Validator() {

    }

    public static void checkPermissions(final Realization realization, final boolean isOwner, final Claims claims) {
        if (!(claims.get(JwtToken.KEY) instanceof List<?>)) {
            log.warn("Bad claims type found: {}", claims);
            throw new ForbiddenException();
        }

        log.info("Id of requester from web: {}", claims.getSubject());
        log.info("Role of requester from web: {}", claims.get(JwtToken.KEY));
        String contextUserId = claims.getSubject();
        EnumSet<Role> roles = ((List<String>) claims.get(JwtToken.KEY)).stream().filter(Role::contains).map(Role::valueOf).collect(Collectors.toCollection(() -> EnumSet.noneOf(Role.class)));

        log.info("Context user id: {}, Context role: {}", contextUserId, roles);

        // FIXME too strict, we might want users on the same team to remove them also
        if (!roles.contains(Role.ADMIN) && !isOwner  && !contextUserId.equals(realization.getUserId())) {
            log.warn("Access denied for delete experiment: /{}/ ", realization.getExperimentId());
            throw new ForbiddenException();
        }
    }
}
