package sg.ncl.service.experiment.validation;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.realization.data.jpa.RealizationEntity;

import java.util.ArrayList;

/**
 * @author Te Ye
 * @version 1.0
 */
@Slf4j
public class Validator {

    public static void addCheck(final RealizationEntity realizationEntity, final Claims claims) {
        checkClaimsRolesType(realizationEntity, claims);
        checkPermissions(realizationEntity, claims);
    }

    public static void checkClaimsRolesType(final RealizationEntity realizationEntity, final Claims claims) {
        if (!(claims.get(JwtToken.KEY) instanceof ArrayList<?>)) {
            log.warn("Bad claims type found: {}", claims);
            throw new ForbiddenException("Invalid permissions for delete experiment: expid " + realizationEntity.getExperimentId());
        }
    }

    public static void checkPermissions(final RealizationEntity realizationEntity, final Claims claims) {
        // since need to pass the checkClaimsRolesType
        // safe to say the role is of type Arraylist at this stage
        log.info("Id of requester from web: {}", claims.getSubject());
        log.info("Role of requester from web: {}", claims.get(JwtToken.KEY));
        String contextUserId = claims.getSubject();
        ArrayList<Role> roles;
        roles = (ArrayList) claims.get(JwtToken.KEY);

        log.info("Context user id: {}, Context role: {}", contextUserId, roles);

        if (!contextUserId.equals(realizationEntity.getUserId()) && !roles.contains(Role.ADMIN)) {
            log.warn("Access denied for delete experiment: /{}/ ", realizationEntity.getExperimentId());
            throw new ForbiddenException("Access denied for delete experiment: expid " + realizationEntity.getExperimentId());
        }

    }

}
