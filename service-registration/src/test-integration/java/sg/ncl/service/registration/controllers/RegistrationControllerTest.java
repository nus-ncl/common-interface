package sg.ncl.service.registration.controllers;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.registration.AbstractTest;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Te Ye on 16-Jun-16.
 */
public class RegistrationControllerTest extends AbstractTest {

    private MockMvc mockMvc;
    @Inject
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void registerNewUserTest() {
        // password
        // UserEntity
        // TeamEntity

    }
}
