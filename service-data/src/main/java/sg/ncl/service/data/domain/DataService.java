package sg.ncl.service.data.domain;

import io.jsonwebtoken.Claims;

import java.util.List;

/**
 * Created by jng on 17/10/16.
 */
public interface DataService {

    Data save(Data data);

    Data getOne(Long id);

    List<Data> getAll();

    List<Data> findByVisibility(DataVisibility visibility);

    Data saveResource(Long id, DataResource dataResource, Claims claims);

}
