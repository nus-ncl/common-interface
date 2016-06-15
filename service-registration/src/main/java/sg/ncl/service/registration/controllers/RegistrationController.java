package sg.ncl.service.registration.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.registration.RegistrationService;
import sg.ncl.service.team.TeamService;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.dtos.TeamInfo;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Te Ye & Desmond
 */
@RestController
@RequestMapping(path = "/registrations", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {

    private final RegistrationService registrationService;

    @Inject
    protected RegistrationController(final RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void abc() {

    }
}
