package sg.ncl.service.version.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Christopher Zhong
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = VersionController.class, secure = false)
public class VersionControllerTest {

    @Inject
    private MockMvc mockMvc;

    @Test
    public void testVersion() throws Exception {
//        final int major = 1;
//        given(version.getMajor()).willReturn(major);

//        final int minor = 2;
//        given(version.getMinor()).willReturn(minor);

//        final String build = "build";
//        given(version.getBuild()).willReturn(build);

//        final ZonedDateTime now = ZonedDateTime.now();
//        given(version.getDate()).willReturn(null);

        final ResultActions resultActions = mockMvc.perform(get("/version"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//                .andExpect(jsonPath("$.major", is(equalTo(major))))
//                .andExpect(jsonPath("$.minor", is(equalTo(minor))))
//                .andExpect(jsonPath("$.build", is(equalTo(build))));
//                .andExpect(jsonPath("$.date", is(equalTo(now))));
    }


}
