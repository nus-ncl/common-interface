package sg.ncl.service.data.domain;

import io.jsonwebtoken.Claims;

import java.util.List;

/**
 * Created by jng on 17/10/16.
 */
public interface DataService {

    List<Data> getDataSets(Claims claims, DataVisibility visibility);

}
