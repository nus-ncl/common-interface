package sg.ncl.service.data.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.ncl.common.exception.base.UnauthorizedException;
import sg.ncl.service.data.domain.*;

import javax.inject.Inject;
import javax.validation.Valid;
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
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Data> getDatasets(@AuthenticationPrincipal Object claims) {
        if (claims == null || !(claims instanceof Claims)) {
            log.warn("Access denied for: /datasets GET");
            throw new UnauthorizedException();
        }
        return dataService.getAll().stream().map(DataInfo::new).collect(Collectors.toList());
    }

    // Get a list of public data sets
    @GetMapping(params = {"visibility"})
    @ResponseStatus(HttpStatus.OK)
    public List<Data> getDatasetsByVisibility(@AuthenticationPrincipal Object claims, @RequestParam("visibility") DataVisibility visibility) {
        if (claims == null && visibility != DataVisibility.PUBLIC) {
            log.warn("Access denied for: /datasets/?visibility=" + visibility);
            throw new UnauthorizedException();
        }
        return dataService.findByVisibility(DataVisibility.PUBLIC).stream().map(DataInfo::new).collect(Collectors.toList());
    }

    // Get details about a data set
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Data getDatasetById(@AuthenticationPrincipal Object claims, @PathVariable String id) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return new DataInfo(dataService.getOne(Long.getLong(id)));
    }

    // Create a data set
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Data add(@AuthenticationPrincipal Object claims, @RequestBody @Valid DataInfo dataInfo) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return new DataInfo(dataService.save(dataInfo));
    }

    // Update a data set
    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Data update(@AuthenticationPrincipal Object claims, @PathVariable String id, @RequestBody @Valid DataInfo dataInfo) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return new DataInfo(dataService.save(Long.getLong(id), dataInfo, (Claims) claims));
    }

    // Delete a data set
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Data remove(@AuthenticationPrincipal Object claims, @PathVariable String id) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return new DataInfo(dataService.delete(Long.getLong(id), (Claims) claims));
    }

    // View resource in a data set
    @GetMapping(path = "/{did}/resources/{rid}")
    @ResponseStatus(HttpStatus.OK)
    public DataResource getResource(@AuthenticationPrincipal Object claims, @PathVariable String did, @PathVariable String rid) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return dataService.findResourceById(Long.getLong(did), Long.getLong(rid), (Claims) claims);
    }

    // Add a resource to a data set
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/resources")
    @ResponseStatus(HttpStatus.CREATED)
    public Data addResource(@AuthenticationPrincipal Object claims,
                            @PathVariable String id,
                            @RequestBody @Valid DataResourceInfo dataResourceInfo) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return new DataInfo(dataService.saveResource(Long.getLong(id), dataResourceInfo, (Claims) claims));
    }

    // Delete a resource from a data set
    @DeleteMapping(path = "/{did}/resources/{rid}")
    @ResponseStatus(HttpStatus.OK)
    public Data removeResource(@AuthenticationPrincipal Object claims, @PathVariable String did, @PathVariable String rid) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return new DataInfo(dataService.deleteResource(Long.getLong(did), Long.getLong(rid), (Claims) claims));
    }

    // Request access to a dataset
    @PostMapping(path = "/{id}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public DataRequest addRequest(@AuthenticationPrincipal Object claims, @PathVariable String id) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        // TODO: add request to database
        return null;
    }

    // Process request
    @PutMapping(path = "{did}/requests/{rid}")
    @ResponseStatus(HttpStatus.OK)
    public DataRequest processRequest(@AuthenticationPrincipal Object claims, @PathVariable String did, @PathVariable String rid) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        // TODO: process request to add to approved users
        return null;
    }

}
