package sg.ncl.common.validation;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.common.jwt.JwtToken;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Christopher Zhong
 * @version 1.0
 */
@Slf4j
public class Validator {

    private Validator() {

    }

    public static void checkClaimsType(final Object claims) {
        if ( !(claims instanceof Claims) ) {
            // throw forbidden
            log.warn("Invalid authentication principal: {}", claims);
            throw new ForbiddenException();
        }
    }


    public static void isAdmin(final Claims claims) {
        if (!(claims.get(JwtToken.KEY) instanceof List<?>)) {
            log.warn("Bad claims type found: {}", claims);
            throw new ForbiddenException();
        }

        String contextUserId = claims.getSubject();
        List<String> roles;
        roles = (ArrayList<String>) claims.get(JwtToken.KEY);

        log.info("Context user id: {}, Context roles: {}", contextUserId, roles);

        if (!roles.contains(Role.ADMIN.toString())) {
            log.warn("Must be an Admin");
            throw new ForbiddenException();
        }
    }

    public static boolean checkAdmin(final Claims claims) {
        if (!(claims.get(JwtToken.KEY) instanceof List<?>)) {
            log.warn("Bad claims type found: {}", claims);
            throw new ForbiddenException();
        }

        String contextUserId = claims.getSubject();
        List<String> roles;
        roles = (ArrayList<String>) claims.get(JwtToken.KEY);

        log.info("Context user id: {}, Context roles: {}", contextUserId, roles);

        return roles.contains(Role.ADMIN.toString());
    }
}
