package sg.ncl.service.experiment.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.domain.ExperimentService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static sg.ncl.common.validation.Validator.checkClaimsType;

/**
 * @Authors: Desmond Lim, Tran Ly Vu
 */
@RestController
@RequestMapping(path = ExperimentsController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ExperimentsController {

    static final String PATH = "/experiments";

    private final ExperimentService experimentService;

    @Inject
    ExperimentsController(@NotNull final ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    // return all experiments (for use by administration)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Experiment> get() {
        return experimentService.getAll().stream().map(ExperimentInfo::new).collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  Experiment getExperiment(@PathVariable Long id) {
        return new ExperimentInfo(experimentService.get(id));
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
    @DeleteMapping(path = "/teams/{teamId}/experiments/{expId}")
    // FIXME: should be DELETE instead of POST and path should be "/experiments/{id}"
    @ResponseStatus(HttpStatus.OK)
    public Experiment deleteExperiment(@PathVariable Long expId, @PathVariable String teamId, @AuthenticationPrincipal Object claims) {
        log.info("Delete experiment User principal: " + claims);
        checkClaimsType(claims);
        return new ExperimentInfo(experimentService.deleteExperiment(expId, teamId, (Claims) claims));
    }

    @PutMapping(path = "/teams/{teamId}/experiments/{expId}")
    @ResponseStatus(HttpStatus.OK)
    public Experiment updateExperiment(@PathVariable Long expId, @PathVariable String teamId) {
        return new ExperimentInfo(experimentService.updateExperiment(expId, teamId));
    }

    @GetMapping(path = "/teams/{teamId}/experiments/{expId}/experimentDetails")
    @ResponseStatus(HttpStatus.OK)
    public String getExperimentDetails(@PathVariable String teamId, @PathVariable Long expId) {
        return experimentService.getExperimentDetails(teamId, expId);
    }

    @GetMapping(path = "/teams/{teamId}/experiments/{expId}/topology")
    @ResponseStatus(HttpStatus.OK)
    public String getTopology(@PathVariable String teamId, @PathVariable Long expId) {
        return experimentService.getTopology(teamId, expId);
    }

    @PostMapping(path= "/teams/{teamId}/experiments/{expId}/internet")
    @ResponseStatus(HttpStatus.CREATED)
    public Experiment requestInternet(@PathVariable String teamId, @PathVariable Long expId, @RequestBody String reason, @AuthenticationPrincipal Object claims) {
        log.debug("Authentication principal for internet access request: " + claims);
        checkClaimsType(claims);
        final JSONObject jsonObject = new JSONObject(reason);
        return new ExperimentInfo(experimentService.requestInternet(teamId, expId, jsonObject.getString("reason"), (Claims) claims));
    }
}
