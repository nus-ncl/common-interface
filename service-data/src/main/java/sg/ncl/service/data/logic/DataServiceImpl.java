package sg.ncl.service.data.logic;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;
import sg.ncl.common.exception.base.NotFoundException;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.data.data.jpa.DataEntity;
import sg.ncl.service.data.data.jpa.DataRepository;
import sg.ncl.service.data.data.jpa.DataResourceEntity;
import sg.ncl.service.data.domain.*;
import sg.ncl.service.data.exceptions.*;
import sg.ncl.service.data.web.DataResourceInfo;
import sg.ncl.service.transmission.domain.DownloadService;
import sg.ncl.service.transmission.domain.UploadService;
import sg.ncl.service.transmission.web.ResumableInfo;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static sg.ncl.service.data.validations.Validator.checkAccessibility;
import static sg.ncl.service.data.validations.Validator.checkPermissions;

/**
 * Created by jng on 17/10/16.
 */
@Service
@Slf4j
public class DataServiceImpl implements DataService {

    private static final String INFO_TEXT = "Data saved: {}";
    private static final String DATA_DIR_KEY = "dataDir";
    private static final String UTF_ENCODING = "UTF-8";

    private final DataRepository dataRepository;
    private final UploadService uploadService;
    private final DownloadService downloadService;
    private final AnalyticsService analyticsService;
    private final AsyncAvScannerService asyncAvScannerService;

    @Inject
    DataServiceImpl(@NotNull final DataRepository dataRepository,
                    @NotNull final UploadService uploadService,
                    @NotNull final DownloadService downloadService,
                    @NotNull final AnalyticsService analyticsService,
                    @NotNull final AsyncAvScannerService asyncAvScannerService) {
        this.dataRepository = dataRepository;
        this.uploadService = uploadService;
        this.downloadService = downloadService;
        this.analyticsService = analyticsService;
        this.asyncAvScannerService = asyncAvScannerService;
    }

    private DataEntity setUpDataEntity(Data data, DataEntity... dataEntities) {
        DataEntity dataEntity;

        if (dataEntities.length > 0) {
            dataEntity = dataEntities[0];
        } else {
            dataEntity = new DataEntity();
            dataEntity.setReleasedDate(data.getReleasedDate());
        }

        dataEntity.setName(data.getName());
        dataEntity.setDescription(data.getDescription());
        dataEntity.setContributorId(data.getContributorId());
        dataEntity.setAccessibility(data.getAccessibility());
        dataEntity.setVisibility(data.getVisibility());

        return dataEntity;
    }

    private DataResourceEntity setUpResourceEntity(DataResource dataResource) {
        DataResourceEntity dataResourceEntity = new DataResourceEntity();

        dataResourceEntity.setUri(dataResource.getUri());

        return dataResourceEntity;
    }

    /**
     * Save details about a new data set
     *
     * @param   data    the data object passed from the web service
     * @return  a data set
     */
    @Transactional
    @Override
    public Data createDataset(Data data) {
        log.info("Save data set");

        // check if data name already exists
        List<DataEntity> dataEntities = dataRepository.findByName(data.getName());

        if (dataEntities != null) {
            for (DataEntity dataEntity : dataEntities) {
                if (dataEntity.getName().equals(data.getName())) {
                    log.warn("Data name is in use: {}", data.getName());
                    throw new DataNameAlreadyExistsException("Data name is in use: " + data.getName());
                }
            }
        }

        DataEntity savedDataEntity = dataRepository.save(setUpDataEntity(data));
        log.info(INFO_TEXT, savedDataEntity);
        return savedDataEntity;
    }

    /**
     * Save details about an existing data set
     *
     * @param   id      data set id
     * @param   data    the data object passed from the web service
     * @param   claims  authenticated credentials
     * @return  a data set
     */
    @Transactional
    @Override
    public Data updateDataset(Long id, Data data, Claims claims) {
        DataEntity dataEntity = (DataEntity) getDataset(id);
        checkPermissions(dataEntity, claims);

        DataEntity savedDataEntity = dataRepository.save(setUpDataEntity(data, dataEntity));
        log.info("Data updated by {}: {}", claims.getSubject(), savedDataEntity);
        return savedDataEntity;
    }

    /**
     * Delete a data set
     *
     * @param   id      data set id
     * @param   claims  authenticated credentials
     * @return  a data set
     */
    @Transactional
    @Override
    public Data deleteDataset(Long id, Claims claims) throws UnsupportedEncodingException {
        DataEntity dataEntity = (DataEntity) getDataset(id);
        checkPermissions(dataEntity, claims);

        dataRepository.delete(id);
        uploadService.deleteDirectory(DATA_DIR_KEY, UriUtils.encode(dataEntity.getName(), UTF_ENCODING));
        log.info("Data deleted by {}: {}", claims.getSubject(), dataEntity.getName());

        return dataEntity;
    }

    /**
     * Get details about a data set
     *
     * @return  data set
     */
    @Override
    public Data getDataset(Long id) {
        Data data = dataRepository.getOne(id);
        if (data == null) {
            throw new DataNotFoundException("Data not found.");
        }
        return data;
    }

    /**
     * Get list of all data sets.
     *
     * @return  the list of data sets
     */
    @Override
    public List<Data> getDatasets() {
        return dataRepository.findAll().stream().collect(Collectors.toList());
    }

    /**
     * Get list of data sets based on data visibility.
     *
     * @param   visibility  PRIVATE|PROTECTED|PUBLIC
     * @return  the list of data sets queried
     */
    @Override
    public List<Data> findByVisibility(DataVisibility visibility) {
        return dataRepository.findByVisibility(visibility).stream().collect(Collectors.toList());
    }

    /**
     * Get a data set resource.
     *
     * @param   did             data set id
     * @param   rid             resource id
     * @param   claims          authenticated credentials
     * @return  data resource
     */
    @Override
    public DataResource findResourceById(Long did, Long rid, Claims claims) {
        DataEntity dataEntity = (DataEntity) getDataset(did);
        checkAccessibility(dataEntity, claims);
        List<DataResource> dataResourceEntities = dataEntity.getResources();
        DataResource dataResource = dataResourceEntities.stream().filter(o -> o.getId().equals(rid)).findFirst().orElse(null);
        if (dataResource == null) {
            log.warn("Data resource not found.");
            throw new DataResourceNotFoundException("Data resource not found.");
        }
        return dataResource;
    }

    /**
     * Save resource to a data set.
     *
     * @param   id              data set id
     * @param   dataResource    data resource to save
     * @param   claims          authenticated credentials
     * @return  data entity
     */
    @Transactional
    @Override
    public Data createResource(Long id, DataResource dataResource, Claims claims) {
        DataEntity dataEntity = (DataEntity) getDataset(id);
        checkPermissions(dataEntity, claims);

        List<DataResource> dataResourceEntities = dataEntity.getResources();
        if (dataResourceEntities != null) {
            for (DataResource dataResourceEntity : dataResourceEntities) {
                if (dataResourceEntity.getUri().equals(dataResource.getUri())) {
                    log.warn("Data resource already in use: {}", dataResource.getUri());
                    throw new DataResourceAlreadyExistsException("Data resource already in use: " + dataResource.getUri());
                }
            }
        }

        dataEntity.addResource(setUpResourceEntity(dataResource));
        DataEntity savedDataEntity = dataRepository.save(dataEntity);
        log.info(INFO_TEXT, savedDataEntity);
        return savedDataEntity;
    }

    /**
     * Delete a resource of a data set.
     *
     * @param   did             data set id
     * @param   rid             resource id
     * @param   claims          authenticated credentials
     * @return  data entity
     */
    @Transactional
    @Override
    public Data deleteResource(Long did, Long rid, Claims claims) {
        DataEntity dataEntity = (DataEntity) getDataset(did);
        checkPermissions(dataEntity, claims);

        DataResource dataResource = findResourceById(did, rid, claims);
        if (dataResource != null) {
            dataEntity.removeResource((DataResourceEntity) dataResource);
            try {
                uploadService.deleteUpload(DATA_DIR_KEY, UriUtils.encode(dataEntity.getName(), UTF_ENCODING), dataResource.getUri());
            } catch (Exception e) {
                log.error("Unable to delete {}: {}", dataResource.getUri(), e);
                throw new DataResourceDeleteException("Unable to delete " + dataResource.getUri());
            }
            log.info("Data resource deleted by {}: {}", claims.getSubject(), dataResource);
        }

        DataEntity savedDataEntity = dataRepository.save(dataEntity);
        log.info(INFO_TEXT, savedDataEntity);
        return savedDataEntity;
    }

    @Override
    public String checkChunk(String resumableIdentifier, String resumableChunkNumber) {
        switch (uploadService.checkChunk(resumableIdentifier, Integer.parseInt(resumableChunkNumber))) {
            case UPLOADED:
                return "Uploaded.";
            case NOT_FOUND:
                return "Not found";
            default:
                return "";
        }
    }

    @Override
    public String addChunk(ResumableInfo resumableInfo, String resumableChunkNumber, Long id, Claims claims) throws UnsupportedEncodingException {
        DataEntity dataEntity = (DataEntity) getDataset(id);
        switch (uploadService.addChunk(resumableInfo, Integer.parseInt(resumableChunkNumber), DATA_DIR_KEY, UriUtils.encode(dataEntity.getName(), UTF_ENCODING))) {
            case FINISHED:
                DataResourceInfo dataResourceInfo = new DataResourceInfo(null, resumableInfo.getResumableFilename(), false);
                createResource(id, dataResourceInfo, claims);
                asyncAvScannerService.scanResource(dataEntity, dataResourceInfo, DATA_DIR_KEY, UTF_ENCODING);
                log.info("Resource upload finished and saved: {}", dataResourceInfo);
                return "All finished.";
            case UPLOAD:
                return "Upload";
            default:
                return "";
        }
    }

    @Override
    public void downloadResource(HttpServletResponse response, Long did, Long rid, Claims claims) {
        DataEntity dataEntity = (DataEntity) getDataset(did);
        checkAccessibility(dataEntity, claims);

        DataResource dataResource = findResourceById(did, rid, claims);
        try {
            downloadService.getChunks(response, DATA_DIR_KEY, UriUtils.encode(dataEntity.getName(), UTF_ENCODING), dataResource.getUri());
            analyticsService.addDataDownloadRecord(did, rid, ZonedDateTime.now(), claims.getSubject());
        } catch (IOException e) {
            log.error("Unable to download resource: {}", e);
            throw new NotFoundException();
        }
    }

    /**
     * Scheduled av scan method to periodically check for non-malicious data resources and rescan them
     * cron expression is retrieve from AsyncAvScanner -> AvScanner -> AvScannerProperties -> application.yml
     * cron expression: second, minute, hour, day-of-month, month, day-of-week
     *
     * Transactional annotation is required to maintain session for the object as DataEntity::getResource() is a lazy loading method, we need
     * the transaction session to last for entire section
     *
     * https://stackoverflow.com/questions/3519059/hibernate-failed-to-lazily-initialize-a-collection-of-role-no-session-or-sessi
     */
    @Scheduled(cron = "#{asyncAvScannerImpl.scheduleCronExpression}", zone = "Asia/Singapore")
    @Transactional
    public void scheduledDataResourceScan() {
        List<Data> dataList = getDatasets();
        dataList.forEach(o -> o.getResources().forEach(dataResource -> {
            DataEntity currentDataEntity = dataRepository.findOne(o.getId());
            if (!dataResource.isMalicious()) {
                try {
                    log.info("Data resource scanning is in progress :: " + new Date());
                    asyncAvScannerService.scanResource(currentDataEntity, dataResource, DATA_DIR_KEY, UTF_ENCODING);
                } catch (UnsupportedEncodingException e) {
                    log.error("Unable to scan data resource: {} for data: {}", e, currentDataEntity);
                }
            }
        }));
        log.info("Dataset scanning is completed.");
    }

}
