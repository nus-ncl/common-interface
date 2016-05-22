package sg.ncl.service.realization.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.realization.data.jpa.repositories.RealizationRepository;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/realizations", produces = MediaType.APPLICATION_JSON_VALUE)
public class RealizationsController {

    private final RealizationRepository realizationRepository;

    @Inject
    protected RealizationsController(final RealizationRepository realizationRepository) {
        this.realizationRepository = realizationRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void get() {}

}
