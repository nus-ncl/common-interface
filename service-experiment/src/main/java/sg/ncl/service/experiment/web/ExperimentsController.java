package sg.ncl.service.experiment.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.domain.ExperimentService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Desmond Lim
 */
@RestController
@RequestMapping(path = ExperimentsController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class ExperimentsController {

    static final String PATH = "/experiments";

    private final ExperimentService experimentService;

    @Inject
    ExperimentsController(@NotNull final ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    // return all experiments (for use by administration)
    @GetMapping(path = "/experiments")
    // FIXME: this path is wrong "/experiments/experiments" should be "/experiments"
    @ResponseStatus(HttpStatus.OK)
    public List<Experiment> get() {
        return experimentService.getAll().stream().map(ExperimentInfo::new).collect(Collectors.toList());
    }

    // returns experiments that the user is part of
    @GetMapping(path = "/users/{id}")
    // FIXME: path is wrong "/experiments/users/{id}" should be "/users/{id}/experiments"
    @ResponseStatus(HttpStatus.OK)
    public List<Experiment> getByUser(@PathVariable String id) {
        return experimentService.findByUser(id).stream().map(ExperimentInfo::new).collect(Collectors.toList());
    }

    // returns experiments that are part of the team
    @GetMapping(path = "/teams/{id}")
    // FIXME: path is wrong "/experiments/teams/{id}" should be "/teams/{id}/experiments"
    @ResponseStatus(HttpStatus.OK)
    public List<Experiment> getByTeamId(@PathVariable String id) {
        return experimentService.findByTeam(id).stream().map(ExperimentInfo::new).collect(Collectors.toList());
    }

    // create new experiment
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Experiment addExperiment(@RequestBody @Valid ExperimentInfo experiment) {
        return new ExperimentInfo(experimentService.save(experiment));
    }

    // delete experiment
    @PostMapping(path = "/delete/{expId}")
    // FIXME: should be DELETE instead of POST and path should be "/experiments/{id}"
    @ResponseStatus(HttpStatus.OK)
    public void deleteExperiment(@PathVariable String expId) {
        experimentService.deleteExperiment(Long.parseLong(expId));
    }
}
