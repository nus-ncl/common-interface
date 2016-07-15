package sg.ncl.service.authentication.web;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.authentication.AbstractTest;
import sg.ncl.service.authentication.logic.AuthenticationService;

import javax.inject.Inject;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Christopher Zhong
 */
@ActiveProfiles("mock-authentication-service")
public class AuthenticationControllerTest extends AbstractTest {

    private final MockHttpServletRequestBuilder post = post("/authentication");

    @Inject
    private WebApplicationContext webApplicationContext;
    @Inject
    private AuthenticationService authenticationService;

    private MockMvc mockMvc;

    @Before
    public void before() {
        assertThat(mockingDetails(authenticationService).isMock(), is(true));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testLoginGood() throws Exception {
        final String username = "username";
        final String password = "password";
        final String s = Base64Utils.encodeToString((username + ":" + password).getBytes());

        when(authenticationService.login(eq(username), eq(password))).thenReturn(new AuthorizationInfo("id", "jwt token"));

        mockMvc.perform(post.header(HttpHeaders.AUTHORIZATION, "Basic " + s))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(equalTo("id"))))
                .andExpect(jsonPath("$.token", is(equalTo("jwt token"))));
    }

    @Test
    public void testLoginMissingAuthorization() throws Exception {
        mockMvc.perform(post)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginEmptyAuthorization() throws Exception {
        mockMvc.perform(post.header(HttpHeaders.AUTHORIZATION, ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginInvalidBasicAuthentication() throws Exception {
        mockMvc.perform(post.header(HttpHeaders.AUTHORIZATION, "Basic authentication"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginUnknownAuthorizationScheme() throws Exception {
        mockMvc.perform(post.header(HttpHeaders.AUTHORIZATION, "Unknown scheme"))
                .andExpect(status().isBadRequest());
    }

}
