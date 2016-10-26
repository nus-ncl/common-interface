package sg.ncl.service.data.domain;

import io.jsonwebtoken.Claims;

import java.util.List;

/**
 * Created by jng on 17/10/16.
 */
public interface DataService {

    Data createDataset(Data data);

    Data updateDataset(Long id, Data data, Claims claims);

    Data deleteDataset(Long id, Claims claims);

    Data getDataset(Long id);

    List<Data> getDatasets();

    List<Data> findByVisibility(DataVisibility visibility);

    DataResource findResourceById(Long did, Long rid, Claims claims);

    Data createResource(Long id, DataResource dataResource, Claims claims);

    Data deleteResource(Long did, Long rid, Claims claims);

}
