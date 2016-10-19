package sg.ncl.service.data.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.service.data.data.jpa.DataEntity;
import sg.ncl.service.data.data.jpa.DataRepository;
import sg.ncl.service.data.domain.Data;
import sg.ncl.service.data.domain.DataService;
import sg.ncl.service.data.domain.DataVisibility;
import sg.ncl.service.data.exceptions.DataNameInUseException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

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

    private DataEntity setUpEntity(Data data) {
        DataEntity dataEntity = new DataEntity();

        dataEntity.setName(data.getName());
        dataEntity.setDescription(data.getDescription());
        dataEntity.setContributorId(data.getContributorId());
        dataEntity.setAccessibility(data.getAccessibility());
        dataEntity.setVisibility(data.getVisibility());

        return dataEntity;
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

        DataEntity savedDataEntity = dataRepository.save(setUpEntity(data));
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

}
