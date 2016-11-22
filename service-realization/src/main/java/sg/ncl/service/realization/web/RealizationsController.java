package sg.ncl.service.realization.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.realization.domain.Realization;
import sg.ncl.service.realization.domain.RealizationService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import static sg.ncl.common.validation.Validator.checkClaimsType;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = RealizationsController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RealizationsController {

    static final String PATH = "/realizations";

    private final RealizationService realizationService;

    @Inject
    RealizationsController(@NotNull final RealizationService realizationService) {
        this.realizationService = realizationService;
    }

    // will be replaced by the one requiring team name
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Realization get(@PathVariable String id) {
        return realizationService.getByExperimentId(Long.parseLong(id));
    }

    // Although our experiment names are unique,
    // deterlab allows different teams but identical experiment names
    // still must check just in case
    @GetMapping(path = "/team/{teamName}/experiment/{expId}")
    @ResponseStatus(HttpStatus.OK)
    public Realization getRealization(@PathVariable String teamName, @PathVariable String expId /*, @AuthenticationPrincipal String user*/) {
//        log.info("Get realization controller User Principal {}", user);
//        log.info("Get realization controller User Principal 2 {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return realizationService.getByExperimentId(teamName, Long.parseLong(expId));
    }

    @PostMapping(path = "/start/team/{teamName}/experiment/{expId}")
    // FIXME: path should be blank
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Realization startExperiment(@PathVariable String teamName, @PathVariable String expId, @AuthenticationPrincipal Object claims) {
        checkClaimsType(claims);
        return realizationService.startExperimentInDeter(teamName, expId, (Claims) claims);
    }

    @PostMapping(path = "/stop/team/{teamName}/experiment/{expId}")
    // FIXME: path should be "/{id}"
    @RequestMapping(path = "/stop/team/{teamName}/experiment/{expId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Realization stopExperiment(@PathVariable String teamName, @PathVariable String expId, @AuthenticationPrincipal Object claims) {
        checkClaimsType(claims);
        return realizationService.stopExperimentInDeter(teamName, expId, (Claims) claims);
    }

    //FIXME: This is a temporary hack; should be moved to analytics
    @GetMapping(path = "/teams/{id}/usage")
    @ResponseStatus(HttpStatus.OK)
    public String getUsageStatistics(@PathVariable final String id) {
        return realizationService.getUsageStatistics(id);
    }
}
