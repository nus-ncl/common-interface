package sg.ncl.common.exception;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
public class ExceptionToHttpStatus {

    private final Map<Class<? extends Exception>, HttpStatus> exceptionToStatus = new HashMap<>();
    private final Map<HttpStatus, List<Class<? extends Exception>>> statusToExceptions = new HashMap<>();

    void put(Class<? extends Exception> clazz, HttpStatus status) {
        exceptionToStatus.put(clazz, status);
        statusToExceptions.computeIfAbsent(status, k -> new ArrayList<>()).add(clazz);
    }

}
