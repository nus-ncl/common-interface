package sg.ncl.common.exception;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A bidirectional mapping between {@link Exception} classes and {@link HttpStatus} codes.
 * An {@link Exception} is mapped to at most one {@link HttpStatus}.
 * However, a {@link HttpStatus} can be mapped to zero or more {@link Exception}s.
 *
 * @author Christopher Zhong
 * @version 1.0
 */
public class ExceptionHttpStatusMap {

    private final Map<Class<? extends Exception>, HttpStatus> exceptionToStatus = new HashMap<>();
    private final Map<HttpStatus, List<Class<? extends Exception>>> statusToExceptions = new HashMap<>();

    ExceptionHttpStatusMap() {
        put(BadRequestException.class, HttpStatus.BAD_REQUEST);
        put(ConflictException.class, HttpStatus.CONFLICT);
        put(NotFoundException.class, HttpStatus.NOT_FOUND);
        put(NotModifiedException.class, HttpStatus.NOT_MODIFIED);
        put(UnauthorizedException.class, HttpStatus.UNAUTHORIZED);
    }

    void put(final Class<? extends Exception> clazz, final HttpStatus status) {
        exceptionToStatus.put(clazz, status);
        statusToExceptions.computeIfAbsent(status, k -> new ArrayList<>()).add(clazz);
    }

    /**
     * Returns the {@link HttpStatus} that is mapped to the given {@link Exception}.
     * If a mapping for the given {@link Exception} does not exists, the {@link HttpStatus#INTERNAL_SERVER_ERROR} is returned.
     *
     * @param clazz the {@link Class} of the {@link Exception}.
     * @return the {@link HttpStatus} that is mapped to the given {@link Exception}, otherwise {@link HttpStatus#INTERNAL_SERVER_ERROR}.
     */
    public HttpStatus get(final Class<? extends Exception> clazz) {
        return exceptionToStatus.getOrDefault(clazz, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Returns the {@link HttpStatus} that is mapped to the given {@link Exception}.
     * If a mapping for the given {@link Exception} does not exists, the {@link HttpStatus#INTERNAL_SERVER_ERROR} is returned.
     *
     * @param exception the {@link Exception}.
     * @return the {@link HttpStatus} that is mapped to the given {@link Exception}, otherwise {@link HttpStatus#INTERNAL_SERVER_ERROR}.
     */
    public HttpStatus get(final Exception exception) {
        return get(exception.getClass());
    }

}
