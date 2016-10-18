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

    // Get a list of all available data sets
    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public List<Data> getAll(@AuthenticationPrincipal Object claims) {
        log.info("User principal: " + claims);
        if (claims == null || !(claims instanceof Claims)) {
            log.warn("Access denied for all data sets");
            throw new ForbiddenException();
        }
        return dataService.getAll().stream().map(DataInfo::new).collect(Collectors.toList());
    }

    // Get a list of public data sets
    @GetMapping(path = "/public")
    @ResponseStatus(HttpStatus.OK)
    public List<Data> getPublic() {
        return dataService.findByVisibility(DataVisibility.PUBLIC).stream().map(DataInfo::new).collect(Collectors.toList());
    }

}
