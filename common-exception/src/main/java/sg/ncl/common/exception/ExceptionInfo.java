package sg.ncl.common.exception;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
public class ExceptionInfo {

    private final String exceptionName;

    public ExceptionInfo(final Exception exception) {
        exceptionName = exception.getClass().getName();
    }

    public String getExceptionName() {
        return exceptionName;
    }

}
