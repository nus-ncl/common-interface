package sg.ncl.service.authentication.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.authentication.AbstractTest;
import sg.ncl.service.authentication.services.AuthenticationService;

import javax.inject.Inject;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testLoginGood() throws Exception {
        final String username = "username";
        final String password = "password";
        final String s = Base64Utils.encodeToString((username + ":" + password).getBytes());

        when(authenticationService.login(eq(username), eq(password))).thenReturn("jwt token");

        mockMvc.perform(post.header(HttpHeaders.AUTHORIZATION, "Basic " + s))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt token"));
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
