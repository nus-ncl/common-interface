package sg.ncl.service.authentication.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.exception.GlobalExceptionHandler;
import sg.ncl.service.authentication.domain.AuthenticationService;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AuthenticationController.class, secure = false)
@ContextConfiguration(classes = {AuthenticationController.class, ExceptionAutoConfiguration.class, GlobalExceptionHandler.class})
public class AuthenticationControllerTest {

    public static final String AUTHENTICATION = "/authentication";
    @Inject
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;

    @Before
    public void before() {
        assertThat(mockingDetails(authenticationService).isMock()).isTrue();
    }

    @Test
    public void testLoginGood() throws Exception {
        final List<Role> roles = Arrays.asList(Role.USER);
        final String username = "username";
        final String password = "password";
        final String s = Base64Utils.encodeToString((username + ":" + password).getBytes());

        when(authenticationService.login(eq(username), eq(password))).thenReturn(new AuthorizationInfo("id", "jwt token", roles));

        mockMvc.perform(post(AUTHENTICATION).header(HttpHeaders.AUTHORIZATION, "Basic " + s))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(equalTo("id"))))
                .andExpect(jsonPath("$.token", is(equalTo("jwt token"))))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0]", is(equalTo("USER"))));
    }

    @Test
    public void testLoginMissingAuthorization() throws Exception {
        mockMvc.perform(post(AUTHENTICATION))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginEmptyAuthorization() throws Exception {
        mockMvc.perform(post(AUTHENTICATION).header(HttpHeaders.AUTHORIZATION, ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testLoginInvalidBasicAuthentication() throws Exception {
        mockMvc.perform(post(AUTHENTICATION).header(HttpHeaders.AUTHORIZATION, "Basic authentication"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testLoginUnknownAuthorizationScheme() throws Exception {
        mockMvc.perform(post(AUTHENTICATION).header(HttpHeaders.AUTHORIZATION, "Unknown scheme"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

}
