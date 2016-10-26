package sg.ncl.service.data.validations;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.data.domain.Data;
import sg.ncl.service.data.domain.DataAccessibility;

import java.util.List;

/**
 * Created by jng on 18/10/16.
 */
@Slf4j
public class Validator {

    private Validator() {}

    public static void checkPermissions(final Data data, final Claims claims) {
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

        if (!contextUserId.equals(data.getContributorId()) && !roles.contains(Role.ADMIN)) {
            log.warn("Must be either admin or owner to add data");
            throw new ForbiddenException();
        }
    }

    public static void checkAccessibility(final Data data, final Claims claims) {
        if (data.getAccessibility().equals(DataAccessibility.RESTRICTED)) {
            if (claims == null) {
                log.warn("Restricted resources for public data set");
                throw new ForbiddenException();
            }

            if (!(claims.get(JwtToken.KEY) instanceof List<?>)) {
                log.warn("Bad claims type found: {}", claims);
                throw new ForbiddenException();
            }

            log.info("Id of requester from web: {}", claims.getSubject());
            log.info("Role of requester from web: {}", claims.get(JwtToken.KEY));
            String contextUserId = claims.getSubject();

            if (!isContributor(data, contextUserId) && !isApprovedUser(data, contextUserId)) {
                throw new ForbiddenException();
            }
        }
    }

    private static boolean isContributor(Data data, String userId) {
        return userId.equals(data.getContributorId());
    }

    private static boolean isApprovedUser(Data data, String userId) {
        List<String> approvedUsers = data.getApprovedUsers();
        return approvedUsers.stream().anyMatch(o -> o.equals(userId));
    }

}
