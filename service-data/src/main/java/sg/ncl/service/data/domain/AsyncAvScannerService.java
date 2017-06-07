package sg.ncl.service.data.domain;

import sg.ncl.service.data.data.jpa.DataEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by dcsyeoty on 05-Jun-17.
 */
public interface AsyncAvScannerService {

    Data scanResource(DataEntity dataEntity, DataResource dataResource, String dataDir, String encodingScheme) throws UnsupportedEncodingException;

    String getScheduleCronExpression();
}
