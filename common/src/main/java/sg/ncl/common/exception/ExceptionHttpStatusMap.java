package sg.ncl.common.exception;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.common.exception.base.ConflictException;
import sg.ncl.common.exception.base.NotFoundException;
import sg.ncl.common.exception.base.NotModifiedException;
import sg.ncl.common.exception.base.UnauthorizedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * A bidirectional mapping between {@link Exception} classes and {@link HttpStatus} codes.
 * An {@link Exception} is mapped to at most one {@link HttpStatus}.
 * However, a {@link HttpStatus} can be mapped to zero or more {@link Exception}s.
 *
 * @author Christopher Zhong
 * @version 1.0
 */
@Slf4j
public class ExceptionHttpStatusMap {

    static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    private final Map<Class<? extends Exception>, HttpStatus> exceptionToStatus = new HashMap<>();
    private final Map<HttpStatus, List<Class<? extends Exception>>> statusToExceptions = new HashMap<>();

    ExceptionHttpStatusMap() {
        put(BadRequestException.class, HttpStatus.BAD_REQUEST);
        put(ConflictException.class, HttpStatus.CONFLICT);
        put(NotFoundException.class, HttpStatus.NOT_FOUND);
        put(NotModifiedException.class, HttpStatus.NOT_MODIFIED);
        put(UnauthorizedException.class, HttpStatus.UNAUTHORIZED);
    }

    @Synchronized
    void put(final Class<? extends Exception> clazz, final HttpStatus status) {
        log.info("Mapping {} to status {}", clazz, status);
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
    @Synchronized
    public HttpStatus get(final Class<? extends Exception> clazz) {
        final HttpStatus status = exceptionToStatus.get(clazz);
        if (status == null) {
            log.warn("Unmapped exception: {}", clazz);
            final List<Entry<Class<? extends Exception>, HttpStatus>> list = exceptionToStatus.entrySet().stream().filter(e -> e.getKey().isAssignableFrom(clazz)).collect(Collectors.toList());
            if (list.isEmpty()) {
                log.warn("No super class match found for {}; using {}", clazz, DEFAULT_HTTP_STATUS);
                put(clazz, DEFAULT_HTTP_STATUS);
                return DEFAULT_HTTP_STATUS;
            } else {
                final Entry<Class<? extends Exception>, HttpStatus> entry = list.get(0);
                if (list.size() == 1) {
                    log.info("Found exact super class match: {}", entry);
                } else {
                    log.warn("Found {} super class matches: {}; using the first match: {}", list.size(), list, entry);
                }
                put(clazz, entry.getValue());
                return entry.getValue();
            }
        }
        return status;
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

    /**
     * Returns the number of mappings ({@link Exception} to  {@link HttpStatus}).
     *
     * @return the number of mappings.
     */
    public Integer size() {
        return exceptionToStatus.size();
    }

}
