package sg.ncl.service.experiment.validation;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.realization.data.jpa.RealizationEntity;

import java.util.List;

/**
 * @author Te Ye
 * @version 1.0
 */
@Slf4j
public class Validator {

    private Validator() {

    }

    public static void checkPermissions(final RealizationEntity realizationEntity, final Claims claims) {
        if (!(claims.get(JwtToken.KEY) instanceof List<?>)) {
            log.warn("Bad claims type found: {}", claims);
            throw new ForbiddenException();
        }

        log.info("Id of requester from web: {}", claims.getSubject());
        log.info("Role of requester from web: {}", claims.get(JwtToken.KEY));
        String contextUserId = claims.getSubject();
        List<Role> roles;
        roles = (List) claims.get(JwtToken.KEY);

        log.info("Context user id: {}, Context role: {}", contextUserId, roles);

        // FIXME too strict, we might want users on the same team to remove them also
        if (!contextUserId.equals(realizationEntity.getUserId()) && !roles.contains(Role.ADMIN)) {
            log.warn("Access denied for delete experiment: /{}/ ", realizationEntity.getExperimentId());
            throw new ForbiddenException();
        }
    }

}
