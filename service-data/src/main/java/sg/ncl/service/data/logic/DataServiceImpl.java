package sg.ncl.service.data.logic;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.ncl.service.data.data.jpa.DataRepository;
import sg.ncl.service.data.domain.Data;
import sg.ncl.service.data.domain.DataService;
import sg.ncl.service.data.domain.DataVisibility;

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

    /**
     * Get list of data sets as follow:
     *      - public (no need login)
     *      - all (after login)
     *
     * @param   claims  the authenticated user, if available
     * @return  the list of data sets queried
     */
    public List<Data> getDataSets(Claims claims, DataVisibility visibility) {
        if (claims == null) {
            return dataRepository.findByVisibility(visibility).stream().collect(Collectors.toList());
        } else {
            return dataRepository.findAll().stream().collect(Collectors.toList());
        }
    }

}
