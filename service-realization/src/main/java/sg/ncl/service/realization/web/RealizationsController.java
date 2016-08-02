package sg.ncl.service.realization.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.logic.RealizationService;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/realizations", produces = MediaType.APPLICATION_JSON_VALUE)
public class RealizationsController {

    private final RealizationService realizationService;

    @Inject
    protected RealizationsController(final RealizationService realizationService) {
        this.realizationService = realizationService;
    }

    @RequestMapping(path = "/{expId}", method = RequestMethod.GET)
    public RealizationEntity get(@PathVariable String expId) {
        return realizationService.getByExperimentId(Long.parseLong(expId));
    }

    @RequestMapping(path = "/start/team/{teamName}/experiment/{expId}", method = RequestMethod.POST)
    public RealizationEntity startExperiment(@PathVariable String teamName, @PathVariable String expId) {

        RealizationEntity realizationEntityDb = realizationService.getByExperimentId(Long.parseLong(expId));
        return realizationService.startExperimentInDeter(teamName, realizationEntityDb.getExperimentName(), realizationEntityDb.getUserId());
    }

    @RequestMapping(path = "/stop/team/{teamName}/experiment/{expId}", method = RequestMethod.POST)
    public String stopExperiment(@PathVariable String teamName, @PathVariable String expId) {

        RealizationEntity realizationEntityDb = realizationService.getByExperimentId(Long.parseLong(expId));
        return realizationService.stopExperimentInDeter(teamName, realizationEntityDb.getExperimentName(), realizationEntityDb.getUserId());
    }
}
