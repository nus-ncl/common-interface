package sg.ncl.service.experiment.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.logic.ExperimentService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desmond Lim
 */
@RestController
@RequestMapping(path = "/experiments", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExperimentsController {

    private final ExperimentService experimentService;

    @Inject
    protected ExperimentsController(final ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<ExperimentEntity> get() {
        return experimentService.get();
    }

    @RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<ExperimentInfo> getByUser(@PathVariable String id) {
        List<ExperimentInfo> infoList = new ArrayList<>();
        List<ExperimentEntity> entityList = experimentService.findByUser(id);

        for (ExperimentEntity entity : entityList) {
            infoList.add(new ExperimentInfo(entity));
        }

        return infoList;
    }
}
