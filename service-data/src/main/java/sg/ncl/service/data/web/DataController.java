package sg.ncl.service.data.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.common.exception.base.UnauthorizedException;
import sg.ncl.service.data.domain.*;
import sg.ncl.service.transmission.web.ResumableInfo;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import static sg.ncl.common.validation.Validator.isAdmin;

/**
 * Created by jng on 17/10/16.
 */
@RestController
@RequestMapping(path = DataController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DataController {

    static final String PATH = "/datasets";

    private final DataService dataService;
    private final DataAccessRequestService dataAccessRequestService;

    @Inject
    DataController(@NotNull final DataService dataService,
                   @NotNull final DataAccessRequestService dataAccessRequestService) {
        this.dataService = dataService;
        this.dataAccessRequestService = dataAccessRequestService;
    }

    @GetMapping(path = "/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Data> searchDatasets(@AuthenticationPrincipal Object claims,
                                     @RequestParam(value="keyword", required=false) String[] keywords) {
        if (claims == null || !(claims instanceof Claims)) {
            log.warn("Access denied for: /datasets/search GET");
            throw new UnauthorizedException();
        }
        return dataService.searchDatasets(keywords).stream().map(DataInfo::new).collect(Collectors.toList());
    }

    // Get a list of all available data sets
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Data> getDatasets(@AuthenticationPrincipal Object claims) {
        if (claims == null || !(claims instanceof Claims)) {
            log.warn("Access denied for: /datasets GET");
            throw new UnauthorizedException();
        }
        try {
            isAdmin((Claims) claims);
            return dataService.getDatasets().stream().map(DataInfo::new).collect(Collectors.toList());
        } catch (ForbiddenException e) {
            String contextUserId = ((Claims) claims).getSubject();
            return dataService.getDatasets().stream()
                    .filter(d -> !(d.getVisibility() == DataVisibility.PRIVATE && !d.getContributorId().equals(contextUserId)))
                    .map(DataInfo::new).collect(Collectors.toList());
        }
    }

    // Get a list of public data sets
    @GetMapping(params = {"visibility"})
    @ResponseStatus(HttpStatus.OK)
    public List<Data> getDatasetsByVisibility(@RequestParam("visibility") DataVisibility visibility) {
        if (visibility != DataVisibility.PUBLIC) {
            log.warn("Access denied for: /datasets?visibility=" + visibility);
            throw new UnauthorizedException();
        }
        return dataService.findByVisibility(DataVisibility.PUBLIC).stream().map(DataInfo::new).collect(Collectors.toList());
    }

    // Get details about a data set
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Data getDatasetById(@AuthenticationPrincipal Object claims, @PathVariable Long id) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        try {
            isAdmin((Claims) claims);
            return new DataInfo(dataService.getDataset(id));
        } catch (ForbiddenException e) {
            String contextUserId = ((Claims) claims).getSubject();
            Data data = dataService.getDataset(id);
            if (!(data.getVisibility() == DataVisibility.PRIVATE && !data.getContributorId().equals(contextUserId))) {
                return new DataInfo(data);
            } else {
                throw new ForbiddenException();
            }
        }
    }

    // Get details about a public data set
    @GetMapping(path = "/{id}", params = {"visibility"})
    @ResponseStatus(HttpStatus.OK)
    public Data getDatasetByIdAndVisibility(@PathVariable Long id, @RequestParam("visibility") DataVisibility visibility) {
        if (visibility != DataVisibility.PUBLIC) {
            log.warn("Access denied for: /datasets/" + id + "?visibility=" + visibility);
            throw new UnauthorizedException();
        }
        Data data = dataService.getDataset(id);
        if (data.getVisibility() != DataVisibility.PUBLIC) {
            log.warn("Retrieved dataset " + id + " is not public");
            throw new UnauthorizedException();
        }
        return new DataInfo(data);
    }

    // Create a data set
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Data add(@AuthenticationPrincipal Object claims, @RequestBody @Valid DataInfo dataInfo) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return new DataInfo(dataService.createDataset(dataInfo));
    }

    // Update a data set
    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Data update(@AuthenticationPrincipal Object claims, @PathVariable Long id, @RequestBody @Valid DataInfo dataInfo) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return new DataInfo(dataService.updateDataset(id, dataInfo, (Claims) claims));
    }

    // Delete a data set
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String remove(@AuthenticationPrincipal Object claims, @PathVariable Long id) throws UnsupportedEncodingException {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return String.valueOf(dataService.deleteDataset(id, (Claims) claims).getName());
    }

    // View resource in a data set
    @GetMapping(path = "/{did}/resources/{rid}")
    @ResponseStatus(HttpStatus.OK)
    public DataResource getResource(@AuthenticationPrincipal Object claims, @PathVariable Long did, @PathVariable Long rid) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return dataService.findResourceById(did, rid, (Claims) claims);
    }

    // Add a resource to a data set
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/resources")
    @ResponseStatus(HttpStatus.CREATED)
    public Data addResource(@AuthenticationPrincipal Object claims,
                            @PathVariable Long id,
                            @RequestBody @Valid DataResourceInfo dataResourceInfo) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return new DataInfo(dataService.createResource(id, dataResourceInfo, (Claims) claims));
    }

    // Delete a resource from a data set
    @DeleteMapping(path = "/{did}/resources/{rid}")
    @ResponseStatus(HttpStatus.OK)
    public Data removeResource(@AuthenticationPrincipal Object claims, @PathVariable Long did, @PathVariable Long rid) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return new DataInfo(dataService.deleteResource(did, rid, (Claims) claims));
    }

    // Update a resource in a data set
    // Currently for editing is_malicious status only
    @PutMapping(path = "/{did}/resources/{rid}")
    @ResponseStatus(HttpStatus.OK)
    public Data updateResource(@AuthenticationPrincipal Object claims, @PathVariable Long did, @PathVariable Long rid, @RequestBody @Valid DataResourceInfo dataResourceInfo) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        isAdmin((Claims) claims);
        return new DataInfo(dataService.updateResource(did, dataResourceInfo, (Claims) claims));
    }

    // Request access to a dataset
    @PostMapping(path = "/{did}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public DataAccessRequest addRequest(@AuthenticationPrincipal Object claims, @PathVariable Long did, @RequestBody String reason) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        final JSONObject json = new JSONObject(reason);
        return new DataAccessRequestInfo(dataAccessRequestService.createRequest(did, json.getString("reason"), (Claims) claims));
    }

    // Process request
    @PutMapping(path = "/{did}/requests/{rid}")
    @ResponseStatus(HttpStatus.OK)
    public DataAccessRequest processRequest(@AuthenticationPrincipal Object claims, @PathVariable Long did, @PathVariable Long rid) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return new DataAccessRequestInfo(dataAccessRequestService.approveRequest(did, rid, (Claims) claims));
    }

    // Get request
    @GetMapping(path = "/{did}/requests/{rid}")
    @ResponseStatus(HttpStatus.OK)
    public DataAccessRequest getRequest(@AuthenticationPrincipal Object claims, @PathVariable Long did, @PathVariable Long rid) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return new DataAccessRequestInfo(dataAccessRequestService.getRequest(did, rid, (Claims) claims));
    }

    @GetMapping(value = "/{id}/chunks/{resumableChunkNumber}/files/{resumableIdentifier}")
    @ResponseStatus(HttpStatus.OK)
    public String checkUpload(@PathVariable String resumableIdentifier,
                              @PathVariable String resumableChunkNumber,
                              @PathVariable Long id) {
        return dataService.checkChunk(resumableIdentifier, resumableChunkNumber);
    }

    @PostMapping(value = "/{id}/chunks/{resumableChunkNumber}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String fileUpload(@AuthenticationPrincipal Object claims,
                             @RequestBody @Valid ResumableInfo resumableInfo,
                             @PathVariable String resumableChunkNumber,
                             @PathVariable Long id) throws UnsupportedEncodingException {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return dataService.addChunk(resumableInfo, resumableChunkNumber, id, (Claims) claims);
    }

    @GetMapping(value = "/{did}/resources/{rid}/download")
    public void downloadResource(@AuthenticationPrincipal Object claims,
                                 @PathVariable Long did, @PathVariable Long rid,
                                 HttpServletResponse response) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        dataService.downloadResource(response, did, rid, (Claims) claims);
    }

    @GetMapping(path = "/{did}/resources/{rid}/download", params = {"visibility"})
    public void downloadPublicResource(@AuthenticationPrincipal Object claims,
                                       @PathVariable Long did, @PathVariable Long rid,
                                       @RequestParam("visibility") DataVisibility visibility,
                                       HttpServletResponse response, HttpServletRequest request) {
        if (claims == null && visibility != DataVisibility.PUBLIC) {
            log.warn("Access denied for: /datasets/" + did + "/resources/" + rid + "/download?visibility=" + visibility);
            throw new UnauthorizedException();
        }
        Enumeration values = request.getHeaders("PublicUserId");
        if (values.hasMoreElements()) {
            String puid = (String) values.nextElement();
            log.info("Public user id: {}", puid);
            dataService.downloadPublicOpenResource(response, did, rid, Long.valueOf(puid));
        } else {
            log.warn("No public user id provided");
            throw new BadRequestException();
        }
    }

    @GetMapping(value = "/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<DataCategory> getCategories(@AuthenticationPrincipal Object claims) {
        if (claims == null || !(claims instanceof Claims)) {
            log.warn("Access denied for: /categories GET");
            throw new UnauthorizedException();
        }
        return dataService.getCategories().stream().map(DataCategoryInfo::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DataCategory getCategoryById(@PathVariable Long id) {
        return new DataCategoryInfo(dataService.getCategory(id));
    }

    @GetMapping(value = "/licenses")
    @ResponseStatus(HttpStatus.OK)
    public List<DataLicense> getLicenses(@AuthenticationPrincipal Object claims) {
        if (claims == null || !(claims instanceof Claims)) {
            log.warn("Access denied for: /licenses GET");
            throw new UnauthorizedException();
        }
        return dataService.getLicenses().stream().map(DataLicenseInfo::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/licenses/{id}")
    @ResponseStatus(HttpStatus.OK)
    private DataLicense getLicenseById(@PathVariable Long id) {
        return new DataLicenseInfo(dataService.getLicense(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/public/users")
    @ResponseStatus(HttpStatus.CREATED)
    public DataPublicUser savePublicUser(@RequestBody @Valid DataPublicUserInfo dataPublicUserInfo) {
        return new DataPublicUserInfo(dataService.createPublicUser(dataPublicUserInfo));
    }

}
