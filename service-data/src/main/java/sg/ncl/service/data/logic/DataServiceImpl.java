package sg.ncl.service.data.logic;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.common.exception.base.NotFoundException;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.data.data.jpa.*;
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
import java.util.*;

import static sg.ncl.service.data.validations.Validator.checkAccessibility;
import static sg.ncl.service.data.validations.Validator.checkPermissions;

/**
 * Created by jng on 17/10/16.
 */
@Service
@Slf4j
public class DataServiceImpl implements DataService {

    private static final String WARN_TEXT = "Data resource not found.";
    private static final String INFO_TEXT = "Data saved: {}";
    private static final String DATA_DIR_KEY = "dataDir";
    private static final String UTF_ENCODING = "UTF-8";

    private final DataRepository dataRepository;
    private final DataCategoryRepository dataCategoryRepository;
    private final DataLicenseRepository dataLicenseRepository;
    private final DataPublicUserRepository dataPublicUserRepository;
    private final UploadService uploadService;
    private final DownloadService downloadService;
    private final AnalyticsService analyticsService;

    @Inject
    DataServiceImpl(@NotNull final DataRepository dataRepository,
                    @NotNull final DataCategoryRepository dataCategoryRepository,
                    @NotNull final DataLicenseRepository dataLicenseRepository,
                    @NotNull final DataPublicUserRepository dataPublicUserRepository,
                    @NotNull final UploadService uploadService,
                    @NotNull final DownloadService downloadService,
                    @NotNull final AnalyticsService analyticsService) {
        this.dataRepository = dataRepository;
        this.dataCategoryRepository = dataCategoryRepository;
        this.dataLicenseRepository = dataLicenseRepository;
        this.dataPublicUserRepository = dataPublicUserRepository;
        this.uploadService = uploadService;
        this.downloadService = downloadService;
        this.analyticsService = analyticsService;
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
        dataEntity.setCategoryId(data.getCategoryId());
        dataEntity.setLicenseId(data.getLicenseId());
        dataEntity.resetKeywords(data.getKeywords());

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
        return new ArrayList<>(dataRepository.findAll());
    }

    @Override
    public Set<Data> searchDatasets(String[] keywords) {
        Set<Data> datasets = new HashSet<>();
        if (keywords != null && keywords.length > 0) {
            for (String keyword : keywords) {
                datasets.addAll(dataRepository.findDataByName(keyword));
                datasets.addAll(dataRepository.findDataByDescription(keyword));
                datasets.addAll(dataRepository.findDataByKeyword(keyword));
            }
        }
        return datasets;
    }

    /**
     * Get list of data sets based on data visibility.
     *
     * @param   visibility  PRIVATE|PROTECTED|PUBLIC
     * @return  the list of data sets queried
     */
    @Override
    public List<Data> findByVisibility(DataVisibility visibility) {
        return new ArrayList<>(dataRepository.findByVisibility(visibility));
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
            log.warn(WARN_TEXT);
            throw new DataResourceNotFoundException(WARN_TEXT);
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
    public Data updateResource(Long did, DataResource dataResource, Claims claims) {
        DataEntity dataEntity = (DataEntity) getDataset(did);

        DataResource updatedDataResource = dataEntity.updateResource(dataResource);

        if (updatedDataResource != null) {
            log.info("Data resource updated by {}: {}", claims.getSubject(), updatedDataResource);
        } else {
            log.warn("Data resource cannot tbe found.");
            throw new DataResourceNotFoundException("Data resource cannot tbe found.");
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
                DataResourceInfo dataResourceInfo = new DataResourceInfo(null, resumableInfo.getResumableFilename(), false, false);
                createResource(id, dataResourceInfo, claims);
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

        if (dataEntity.getVisibility() != DataVisibility.PRIVATE || dataEntity.getContributorId().equals(claims.getSubject())) {
            DataResource dataResource = findResourceById(did, rid, claims);
            try {
                downloadService.getChunks(response, DATA_DIR_KEY, UriUtils.encode(dataEntity.getName(), UTF_ENCODING), dataResource.getUri());
                analyticsService.addDataDownloadRecord(did, rid, ZonedDateTime.now(), claims.getSubject());
            } catch (IOException e) {
                log.error("Unable to download resource: {}", e);
                throw new NotFoundException();
            }
        } else {
            throw new ForbiddenException();
        }
    }

    @Override
    public void downloadPublicOpenResource(HttpServletResponse response, Long did, Long rid, Long puid) {
        DataPublicUserEntity userEntity = dataPublicUserRepository.getOne(puid);
        if (userEntity == null) {
            throw new DataPublicUserNotFoundException("Public user not found.");
        }

        DataEntity dataEntity = (DataEntity) getDataset(did);
        if (dataEntity.getVisibility() == DataVisibility.PUBLIC && dataEntity.getAccessibility() == DataAccessibility.OPEN) {
            List<DataResource> dataResourceEntities = dataEntity.getResources();
            DataResource dataResource = dataResourceEntities.stream().filter(o -> o.getId().equals(rid)).findFirst().orElse(null);
            if (dataResource == null) {
                log.warn(WARN_TEXT);
                throw new DataResourceNotFoundException(WARN_TEXT);
            }
            try {
                downloadService.getChunks(response, DATA_DIR_KEY, UriUtils.encode(dataEntity.getName(), UTF_ENCODING), dataResource.getUri());
                analyticsService.addDataPublicDownloadRecord(did, rid, ZonedDateTime.now(), puid);
            } catch (IOException e) {
                log.error("Unable to download resource: {}", e);
                throw new NotFoundException();
            }
        } else {
            throw new ForbiddenException();
        }
    }

    @Override
    public List<DataCategory> getCategories() {
        return new ArrayList<>(dataCategoryRepository.findAll());
    }

    @Override
    public DataCategory getCategory(Long id) {
        DataCategory dataCategory = dataCategoryRepository.getOne(id);
        if (dataCategory == null) {
            throw new DataCategoryNotFoundException("Category not found.");
        }
        return dataCategory;
    }

    @Override
    public List<DataLicense> getLicenses() {
        return new ArrayList<>(dataLicenseRepository.findAll());
    }

    @Override
    public DataLicense getLicense(Long id) {
        DataLicense dataLicense = dataLicenseRepository.getOne(id);
        if (dataLicense == null) {
            throw new DataLicenseNotFoundException("License not found.");
        }
        return dataLicense;
    }

    /**
     * Save the details of a public user
     *
     * @param   dataPublicUser  the data object passed from the web service
     * @return  a public user
     */
    @Override
    public DataPublicUser createPublicUser(DataPublicUser dataPublicUser) {
        log.info("Save public user");
        DataPublicUserEntity newPublicUserEntity = new DataPublicUserEntity();
        newPublicUserEntity.setFullName(dataPublicUser.getFullName());
        newPublicUserEntity.setEmail(dataPublicUser.getEmail());
        newPublicUserEntity.setJobTitle(dataPublicUser.getJobTitle());
        newPublicUserEntity.setInstitution(dataPublicUser.getInstitution());
        newPublicUserEntity.setCountry(dataPublicUser.getCountry());
        DataPublicUserEntity savedPublicUserEntity = dataPublicUserRepository.save(newPublicUserEntity);
        log.info("Public user saved: {}", savedPublicUserEntity);
        return savedPublicUserEntity;
    }

}
