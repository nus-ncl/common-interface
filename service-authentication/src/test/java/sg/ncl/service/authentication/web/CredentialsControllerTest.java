package sg.ncl.service.authentication.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.exception.GlobalExceptionHandler;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsService;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.PasswordNullOrEmptyException;
import sg.ncl.service.authentication.exceptions.PasswordResetRequestNotFoundException;
import sg.ncl.service.authentication.exceptions.PasswordResetRequestTimeoutException;
import sg.ncl.service.authentication.exceptions.UsernameNullOrEmptyException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sg.ncl.service.authentication.util.TestUtil.getCredentialsEntity;

/**
 * @author Christopher Zhong
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CredentialsController.class, secure = true)
@ContextConfiguration(classes = {CredentialsController.class, ExceptionAutoConfiguration.class, GlobalExceptionHandler.class})
public class CredentialsControllerTest {

    @Inject
    private ObjectMapper mapper;
    @Inject
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Mock
    private Claims claims;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @MockBean
    private CredentialsService credentialsService;

    @Before
    public void before() {
        assertThat(mockingDetails(claims).isMock()).isTrue();
        assertThat(mockingDetails(securityContext).isMock()).isTrue();
        assertThat(mockingDetails(authentication).isMock()).isTrue();
        assertThat(mockingDetails(credentialsService).isMock()).isTrue();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
    }

    @Test
    public void testGetAll() throws Exception {
        final List<CredentialsEntity> list = new ArrayList<>();
        final CredentialsEntity entity1 = getCredentialsEntity();
        final CredentialsEntity entity2 = getCredentialsEntity();
        final CredentialsEntity entity3 = getCredentialsEntity();
        list.add(entity1);
        list.add(entity2);
        list.add(entity3);

        doReturn(list).when(credentialsService).getAll();

        mockMvc.perform(get(CredentialsController.PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id", is(equalTo(entity1.getId()))))
                .andExpect(jsonPath("$[0].username", is(equalTo(entity1.getUsername()))))
                .andExpect(jsonPath("$[0].password", is(equalTo("*****"))))
                .andExpect(jsonPath("$[0].status", is(equalTo(entity1.getStatus().name()))))
                .andExpect(jsonPath("$[0].roles", hasSize(1)))
                .andExpect(jsonPath("$[0].roles[0]", is(equalTo("USER"))))

                .andExpect(jsonPath("$[1].id", is(equalTo(entity2.getId()))))
                .andExpect(jsonPath("$[1].username", is(equalTo(entity2.getUsername()))))
                .andExpect(jsonPath("$[1].password", is(equalTo("*****"))))
                .andExpect(jsonPath("$[1].status", is(equalTo(entity2.getStatus().name()))))
                .andExpect(jsonPath("$[1].roles", hasSize(1)))
                .andExpect(jsonPath("$[1].roles[0]", is(equalTo("USER"))))

                .andExpect(jsonPath("$[2].id", is(equalTo(entity3.getId()))))
                .andExpect(jsonPath("$[2].username", is(equalTo(entity3.getUsername()))))
                .andExpect(jsonPath("$[2].password", is(equalTo("*****"))))
                .andExpect(jsonPath("$[2].status", is(equalTo(entity3.getStatus().name()))))
                .andExpect(jsonPath("$[2].roles", hasSize(1)))
                .andExpect(jsonPath("$[2].roles[0]", is(equalTo("USER"))));

    }

    @Test
    public void testAddCredentialsGoodIdAndUsernameAndPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "username", "password", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);
        final CredentialsEntity credentialsEntity = new CredentialsEntity();
        credentialsEntity.setId("id");
        credentialsEntity.setUsername("username");
        credentialsEntity.setPassword("password");

        when(credentialsService.addCredentials(any(Credentials.class))).thenReturn(credentialsEntity);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(equalTo(credentialsInfo.getId()))))
                .andExpect(jsonPath("$.username", is(equalTo(credentialsInfo.getUsername()))))
                .andExpect(jsonPath("$.password", is(equalTo("*****"))))
                .andExpect(jsonPath("$.status", is(equalTo(CredentialsStatus.ACTIVE.name()))))
                .andExpect(jsonPath("$.roles", hasSize(0)))
        ;
    }

    @Test
    public void testAddCredentialsGoodIdAndUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "username", null, null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCredentialsGoodIdAndUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "username", "", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCredentialsGoodIdAndNullUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", null, "password", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCredentialsGoodIdAndEmptyUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "", "password", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCredentialsNullIdAndGoodUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "username", "password", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCredentialsEmptyIdAndGoodUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", "username", "password", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCredentialsNullBody() throws Exception {
        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsEmptyBody() throws Exception {
        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsNullIdAndUsernameAndPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, null, null, null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCredentialsNullIdAndUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, null, "", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCredentialsNullIdAndEmptyUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "", null, null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCredentialsNullIdAndEmptyUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "", "", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCredentialsEmptyIdAndNullUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", null, null, null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCredentialsEmptyIdAndNullUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", null, "", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCredentialsEmptyIdAndEmptyUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", "", null, null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCredentialsEmptyIdAndEmptyUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", "", "", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateCredentialsGoodUsernameAndPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "username", "password", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        when(credentialsService.updateCredentials(anyString(), any(Credentials.class), any(Claims.class))).thenReturn(credentialsInfo);
        when(claims.getSubject()).thenReturn("id");

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateCredentialsGoodUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "username", null, null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        when(credentialsService.updateCredentials(anyString(), any(Credentials.class), any(Claims.class))).thenReturn(credentialsInfo);
        when(claims.getSubject()).thenReturn("id");

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateCredentialsGoodUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "username", "", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        when(credentialsService.updateCredentials(anyString(), any(Credentials.class), any(Claims.class))).thenReturn(credentialsInfo);
        when(claims.getSubject()).thenReturn("id");

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateCredentialsNullUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, null, "password", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        when(credentialsService.updateCredentials(anyString(), any(Credentials.class), any(Claims.class))).thenReturn(credentialsInfo);
        when(claims.getSubject()).thenReturn("id");

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateCredentialsEmptyUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "", "password", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        when(credentialsService.updateCredentials(anyString(), any(Credentials.class), any(Claims.class))).thenReturn(credentialsInfo);
        when(claims.getSubject()).thenReturn("id");

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateCredentialsNullUsernameAndPassword() throws Exception {
        final Credentials credentialsInfo = new CredentialsInfo(null, null, null, null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotModified())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateCredentialsNullUsernameAndEmptyPassword() throws Exception {
        final Credentials credentialsInfo = new CredentialsInfo(null, null, "", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotModified())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateCredentialsEmptyUsernameAndNullPassword() throws Exception {
        final Credentials credentialsInfo = new CredentialsInfo(null, "", null, null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotModified())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateCredentialsEmptyUsernameAndPassword() throws Exception {
        final Credentials credentialsInfo = new CredentialsInfo(null, "", "", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotModified())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdatePasswordCredentialsNotFound() throws Exception {
        final Credentials credentialsInfo = new CredentialsInfo("id", "username", "password", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        doThrow(new CredentialsNotFoundException("id")).when(credentialsService).updateCredentials(anyString(), any(Credentials.class), any(Claims.class));
        when(claims.getSubject()).thenReturn("id");

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddPasswordResetRequestUnknownUsername() throws Exception {
        final String username = "alice@nus.edu.sg";
        final String jsonString = "{\"username\": " + "\"" + username +"\"}" ;
        final byte[] content = mapper.writeValueAsBytes(jsonString);

        doThrow(new CredentialsNotFoundException(username)).when(credentialsService).addPasswordResetRequest(anyString());

        mockMvc.perform(post(CredentialsController.PATH + "/password/resets").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddPasswordResetRequestNullUsername() throws Exception {
        final String username = "";
        final String jsonString = "{\"username\": " + "\"" + username +"\"}" ;
        final byte[] content = mapper.writeValueAsBytes(jsonString);

        doThrow(new UsernameNullOrEmptyException()).when(credentialsService).addPasswordResetRequest(anyString());

        mockMvc.perform(post(CredentialsController.PATH + "/password/resets").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testAddPasswordResetRequestGood() throws Exception {
        final String username = "alice@nus.edu.sg";
        final String jsonString = "{\"username\": " + "\"" + username +"\"}" ;
        final byte[] content = mapper.writeValueAsBytes(jsonString);

        mockMvc.perform(post(CredentialsController.PATH + "/password/resets").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testResetPasswordGood() throws Exception {
        final Credentials credentialsInfo = new CredentialsInfo("id", "username", "password", null, null);
        final String id = RandomStringUtils.randomAlphanumeric(20);
        final String password = "password";
        final String jsonString = "{\"id\": " + "\"" + id +"\", \"new\": \"" + password + "\"}" ;
        final byte[] content = mapper.writeValueAsBytes(jsonString);

        doReturn(credentialsInfo).when(credentialsService).resetPassword(anyString());

        mockMvc.perform(put(CredentialsController.PATH + "/password").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testResetPasswordRequestNotFound() throws Exception {
        final String id = RandomStringUtils.randomAlphanumeric(20);
        final String password = "password";
        final String jsonString = "{\"id\": " + "\"" + id +"\", \"new\": \"" + password + "\"}" ;
        final byte[] content = mapper.writeValueAsBytes(jsonString);

        doThrow(new PasswordResetRequestNotFoundException(id)).when(credentialsService).resetPassword(anyString());

        mockMvc.perform(put(CredentialsController.PATH + "/password").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testResetPasswordRequestTimeout() throws Exception {
        final String id = RandomStringUtils.randomAlphanumeric(20);
        final String password = "password";
        final String jsonString = "{\"id\": " + "\"" + id +"\", \"new\": \"" + password + "\"}" ;
        final byte[] content = mapper.writeValueAsBytes(jsonString);

        doThrow(new PasswordResetRequestTimeoutException("timeout")).when(credentialsService).resetPassword(anyString());

        mockMvc.perform(put(CredentialsController.PATH + "/password").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testResetPasswordNullPassword() throws Exception {
        final Credentials credentialsInfo = new CredentialsInfo("id", "username", "password", null, null);
        final String id = RandomStringUtils.randomAlphanumeric(20);
        final String password = "";
        final String jsonString = "{\"id\": " + "\"" + id +"\", \"new\": \"" + password + "\"}" ;
        final byte[] content = mapper.writeValueAsBytes(jsonString);

        doThrow(new PasswordNullOrEmptyException()).when(credentialsService).resetPassword(anyString());

        mockMvc.perform(put(CredentialsController.PATH + "/password").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

}
