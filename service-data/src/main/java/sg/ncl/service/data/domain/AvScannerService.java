package sg.ncl.service.data.domain;

import java.io.UnsupportedEncodingException;

/**
 * Created by dcsyeoty on 29-May-17.
 */
public interface AvScannerService {

    boolean scan(String subDirKey, String preDir, String fileName);
}
