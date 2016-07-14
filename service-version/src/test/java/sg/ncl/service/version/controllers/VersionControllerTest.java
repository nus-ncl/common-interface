package sg.ncl.service.version.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.version.AbstractTest;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Christopher Zhong
 */
@WebAppConfiguration
public class VersionControllerTest extends AbstractTest {

    private MockMvc mockMvc;
    @Inject
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testVersion() throws Exception {
        final ResultActions resultActions = mockMvc.perform(get("/version"));

        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        // TODO complete the test
    }

}
