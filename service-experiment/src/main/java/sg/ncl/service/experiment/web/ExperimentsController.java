package sg.ncl.service.experiment.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/experiments", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExperimentsController {

    private final ExperimentRepository experimentRepository;

    @Inject
    protected ExperimentsController(final ExperimentRepository experimentRepository) {
        this.experimentRepository = experimentRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void get() { }

}
