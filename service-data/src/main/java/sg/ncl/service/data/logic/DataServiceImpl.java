package sg.ncl.service.data.logic;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.service.data.data.jpa.DataEntity;
import sg.ncl.service.data.data.jpa.DataRepository;
import sg.ncl.service.data.data.jpa.DataResourceEntity;
import sg.ncl.service.data.domain.Data;
import sg.ncl.service.data.domain.DataResource;
import sg.ncl.service.data.domain.DataService;
import sg.ncl.service.data.domain.DataVisibility;
import sg.ncl.service.data.exceptions.DataNameInUseException;
import sg.ncl.service.data.exceptions.DataNotFoundException;
import sg.ncl.service.data.exceptions.DataResourceNotFoundException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
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

    private final DataRepository dataRepository;

    @Inject
    DataServiceImpl(@NotNull final DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    private DataEntity setUpDataEntity(Data data, DataEntity... dataEntities) {
        DataEntity dataEntity;

        if (dataEntities.length > 0) {
            dataEntity = dataEntities[0];
        } else {
            dataEntity = new DataEntity();
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
                    throw new DataNameInUseException();
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
        log.info(INFO_TEXT, savedDataEntity);
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
    public Data deleteDataset(Long id, Claims claims) {
        DataEntity dataEntity = (DataEntity) getDataset(id);
        checkPermissions(dataEntity, claims);

        dataRepository.delete(id);
        log.info("Data deleted: {}", dataEntity.getName());

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
            throw new DataNotFoundException();
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
            throw new DataResourceNotFoundException();
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
    public Data createResource(Long id, DataResource dataResource, Claims claims) {
        DataEntity dataEntity = (DataEntity) getDataset(id);
        checkPermissions(dataEntity, claims);

        dataEntity.getResources().add(setUpResourceEntity(dataResource));
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

        List<DataResource> dataResourceEntities = dataEntity.getResources();
        DataResource dataResource = dataResourceEntities
                .stream().filter(o -> o.getId().equals(rid)).findFirst().orElse(null);
        if (dataResource != null) {
            dataResourceEntities.remove(dataResource);
        }

        DataEntity savedDataEntity = dataRepository.save(dataEntity);
        log.info(INFO_TEXT, savedDataEntity);
        return savedDataEntity;
    }

}
