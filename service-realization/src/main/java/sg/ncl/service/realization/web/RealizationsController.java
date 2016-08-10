package sg.ncl.service.realization.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.domain.Realization;
import sg.ncl.service.realization.domain.RealizationService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/realizations", produces = MediaType.APPLICATION_JSON_VALUE)
public class RealizationsController {

    private final RealizationService realizationService;

    @Inject
    RealizationsController(@NotNull final RealizationService realizationService) {
        this.realizationService = realizationService;
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Realization get(@PathVariable String id) {
        return realizationService.getByExperimentId(Long.parseLong(id));
    }

    @PostMapping(path = "/start/team/{teamName}/experiment/{expId}")
    // FIXME: path should be blank
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Realization startExperiment(@PathVariable String teamName, @PathVariable String expId) {
        RealizationEntity realizationEntityDb = realizationService.getByExperimentId(Long.parseLong(expId));
        return realizationService.startExperimentInDeter(teamName, realizationEntityDb.getExperimentName(), realizationEntityDb.getUserId());
    }

    @PostMapping(path = "/stop/team/{teamName}/experiment/{expId}")
    // FIXME: path should be "/{id}"
    @RequestMapping(path = "/stop/team/{teamName}/experiment/{expId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Realization stopExperiment(@PathVariable String teamName, @PathVariable String expId) {
        RealizationEntity realizationEntityDb = realizationService.getByExperimentId(Long.parseLong(expId));
        return realizationService.stopExperimentInDeter(teamName, realizationEntityDb.getExperimentName(), realizationEntityDb.getUserId());
    }
}
