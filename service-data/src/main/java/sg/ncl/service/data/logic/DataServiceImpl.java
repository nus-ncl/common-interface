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

    private final DataRepository dataRepository;

    @Inject
    DataServiceImpl(@NotNull final DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    private DataEntity setUpDataEntity(Data data) {
        DataEntity dataEntity = new DataEntity();

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
     * Save details about a data set
     *
     * @param   data    the data object passed from the web service
     * @return  a data set
     */
    @Transactional
    public Data save(Data data) {
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
        log.info("Data saved: {}", savedDataEntity);
        return savedDataEntity;
    }

    /**
     * Get details about a data set
     *
     * @return  data set
     */
    public Data getOne(Long id) {
        return dataRepository.getOne(id);
    }

    /**
     * Get list of all data sets.
     *
     * @return  the list of data sets
     */
    public List<Data> getAll() {
        return dataRepository.findAll().stream().collect(Collectors.toList());
    }

    /**
     * Get list of data sets based on data visibility.
     *
     * @param   visibility  PRIVATE|PROTECTED|PUBLIC
     * @return  the list of data sets queried
     */
    public List<Data> findByVisibility(DataVisibility visibility) {
        return dataRepository.findByVisibility(visibility).stream().collect(Collectors.toList());
    }

    /**
     * Get a data set resource.
     *
     * @param   did             data set id
     * @param   rid             resource id
     * @return  data resource
     */
    public DataResource findResourceById(Long did, Long rid, Claims claims) {
        DataEntity dataEntity = (DataEntity) getOne(did);
        checkAccessibility(dataEntity, claims);
        List<DataResourceEntity> dataResourceEntities = dataEntity.getResources();
        return dataResourceEntities.stream().filter(o -> o.getId().equals(rid)).findFirst().orElse(null);
    }

    /**
     * Save resource to a data set.
     *
     * @param   id              data set id
     * @param   dataResource    data resource to save
     * @param   claims          authenticated credentials
     * @return  data resource
     */
    @Transactional
    public Data saveResource(Long id, DataResource dataResource, Claims claims) {
        DataEntity dataEntity = (DataEntity) getOne(id);
        checkPermissions(dataEntity, claims);

        dataEntity.getResources().add(setUpResourceEntity(dataResource));
        DataEntity savedDataEntity = dataRepository.save(dataEntity);
        log.info("Data saved: {}", savedDataEntity);
        return savedDataEntity;
    }

}
