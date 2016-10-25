package sg.ncl.service.data.domain;

import io.jsonwebtoken.Claims;

import java.util.List;

/**
 * Created by jng on 17/10/16.
 */
public interface DataService {

    Data save(Data data);

    Data save(Long id, Data data, Claims claims);

    Data delete(Long id, Claims claims);

    Data getOne(Long id);

    List<Data> getAll();

    List<Data> findByVisibility(DataVisibility visibility);

    DataResource findResourceById(Long did, Long rid, Claims claims);

    Data saveResource(Long id, DataResource dataResource, Claims claims);

    Data deleteResource(Long did, Long rid, Claims claims);

}