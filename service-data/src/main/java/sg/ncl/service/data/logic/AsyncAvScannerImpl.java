package sg.ncl.service.data.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;
import sg.ncl.service.data.data.jpa.DataEntity;
import sg.ncl.service.data.data.jpa.DataRepository;
import sg.ncl.service.data.domain.AsyncAvScannerService;
import sg.ncl.service.data.domain.AvScannerService;
import sg.ncl.service.data.domain.Data;
import sg.ncl.service.data.domain.DataResource;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

/**
 * The asynchronous version of the anti-virus scanning service
 * Keep the synchronous version as a separate service to enhance reusability
 * Created by teye
 */
@Service
@Slf4j
public class AsyncAvScannerImpl implements AsyncAvScannerService {

    private final AvScannerService avScannerService;
    private final DataRepository dataRepository;

    @Inject
    AsyncAvScannerImpl(@NotNull final AvScannerService avScannerService, @NotNull final DataRepository dataRepository) {
        this.avScannerService = avScannerService;
        this.dataRepository = dataRepository;
    }

    /**
     * Scans a resource to check if it is malicious after uploading and updates the resource status
     * @param dataEntity the data entity that contains the data resource
     * @param dataResource the data resource to be scanned
     * @param dataDir the data location define in application.yml
     * @param encodingScheme the encoding scheme of the file name
     * @return the data entity containing the edited data resource entity
     * @throws UnsupportedEncodingException
     */
   @Async
   @Transactional
   public Data scanResource(DataEntity dataEntity, DataResource dataResource, String dataDir, String encodingScheme) throws UnsupportedEncodingException {
       boolean isMalicious = avScannerService.scan(dataDir, UriUtils.encode(dataEntity.getName(), encodingScheme), dataResource.getUri());

       dataEntity.editResourceMalicious(dataResource, isMalicious);

       return dataRepository.save(dataEntity);
   }
}
