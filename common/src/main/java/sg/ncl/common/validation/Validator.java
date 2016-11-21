package sg.ncl.common.validation;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.common.exception.base.ForbiddenException;


/**
 * @author Christopher Zhong
 * @version 1.0
 */
@Slf4j
public class Validator {

    public static void checkClaimsType(final Object claims) {
        if ( !(claims instanceof Claims) ) {
            // throw forbidden
            log.warn("Invalid authentication principal: {}", claims);
            throw new ForbiddenException();
        }
    }
}
