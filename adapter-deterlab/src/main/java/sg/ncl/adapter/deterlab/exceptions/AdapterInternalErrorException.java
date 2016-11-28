package sg.ncl.adapter.deterlab.exceptions;

/**
 * Created by dcszwang on 11/28/2016.
 */
public class AdapterInternalErrorException extends RuntimeException {
    public AdapterInternalErrorException() {
        super("Adapter DeterLab internal server error");
    }
}
