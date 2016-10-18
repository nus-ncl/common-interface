package sg.ncl.service.data.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.common.exception.base.ForbiddenException;
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
@RestController
@RequestMapping(path = DataController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DataController {

    static final String PATH = "/datasets";

    private final DataService dataService;

    @Inject
    DataController(@NotNull final DataService dataService) {
        this.dataService = dataService;
    }

    // Get a list of all available datasets
    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public List<Data> get(@AuthenticationPrincipal Object claims) {
        if (claims != null && !(claims instanceof Claims)) {
            throw new ForbiddenException("Unknown authenticating object.");
        }
        return dataService.getDataSets((Claims) claims, null).stream().map(DataInfo::new).collect(Collectors.toList());
    }

    // Get a list of public datasets
    @GetMapping(path = "/public")
    @ResponseStatus(HttpStatus.OK)
    public List<Data> get() {
        return dataService.getDataSets(null, DataVisibility.PUBLIC).stream().map(DataInfo::new).collect(Collectors.toList());
    }

}
