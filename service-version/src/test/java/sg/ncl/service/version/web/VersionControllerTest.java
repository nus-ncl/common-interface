package sg.ncl.service.version.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sg.ncl.service.version.TestConfig;

import javax.inject.Inject;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Christopher Zhong
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = VersionController.class, secure = false)
@Import(TestConfig.class)
public class VersionControllerTest {

    @Inject
    private MockMvc mockMvc;

    @Test
    public void testVersion() throws Exception {
        final ResultActions resultActions = mockMvc.perform(get("/version"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.major", is(equalTo(1))))
                .andExpect(jsonPath("$.minor", is(equalTo(2))))
                .andExpect(jsonPath("$.build", is(equalTo("build"))));
    }

}
