package sg.ncl.service.data.domain;

import java.util.List;

/**
 * Created by jng on 17/10/16.
 */
public interface DataService {

    Data getOne(Long id);

    List<Data> getAll();

    List<Data> findByVisibility(DataVisibility visibility);

}
