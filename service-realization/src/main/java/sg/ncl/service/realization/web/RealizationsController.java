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

    @RequestMapping(method = RequestMethod.GET)
    public void get() {}

    @RequestMapping(path = "/start", method = RequestMethod.POST)
    public void startExperiment(final RealizationEntity realizationEntity) {
        RealizationEntity realizationEntityDb = realizationService.getByExperimentId(realizationEntity.getExperimentId());

        if (realizationEntity == null) {
            realizationService.save(realizationEntity);
        }

        realizationService.startExperimentInDeter(realizationEntityDb.getTeamId(), realizationEntityDb.getExperimentName());
    }
}
