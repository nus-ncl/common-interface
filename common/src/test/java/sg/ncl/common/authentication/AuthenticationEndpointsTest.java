package sg.ncl.common.authentication;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Te Ye
 */
public class AuthenticationEndpointsTest {

//    @Inject
//    private WebApplicationContext webApplicationContext;
//
//    private MockMvc mockMvc;
//
//    @Before
//    public void before() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//    @Test
//    public void testGetUsers() throws Exception {
//        mockMvc.perform(post("/authentication")).andExpect(status().isOk());
//    }
}
