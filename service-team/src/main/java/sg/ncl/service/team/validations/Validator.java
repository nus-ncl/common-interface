package sg.ncl.service.team.validations;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.common.jwt.JwtToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jng on 18/10/16.
 */
@Slf4j
public class Validator {

    private Validator() {}

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
}
