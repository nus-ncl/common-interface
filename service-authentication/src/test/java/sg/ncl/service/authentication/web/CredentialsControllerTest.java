package sg.ncl.service.authentication.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.common.exception.ExceptionProperties;
import sg.ncl.service.authentication.AbstractTest;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.logic.CredentialsService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
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
@ActiveProfiles("mock-credentials-service")
public class CredentialsControllerTest extends AbstractTest {

    private final MockHttpServletRequestBuilder get = get(CredentialsController.PATH);
    private final MockHttpServletRequestBuilder post = post(CredentialsController.PATH);
    private final MockHttpServletRequestBuilder put = put(CredentialsController.PATH);

    @Inject
    private WebApplicationContext webApplicationContext;
    @Inject
    private CredentialsService credentialsService;
    @Inject
    private ExceptionProperties properties;
    @Inject
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    @Before
    public void before() {
        assertThat(mockingDetails(credentialsService).isMock(), is(true));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id", is(equalTo(entity1.getId()))))
                .andExpect(jsonPath("$[0].username", is(equalTo(entity1.getUsername()))))
                .andExpect(jsonPath("$[0].password", is(equalTo("*****"))))
                .andExpect(jsonPath("$[0].status", is(equalTo(entity1.getStatus().name()))))

                .andExpect(jsonPath("$[1].id", is(equalTo(entity2.getId()))))
                .andExpect(jsonPath("$[1].username", is(equalTo(entity2.getUsername()))))
                .andExpect(jsonPath("$[1].password", is(equalTo("*****"))))
                .andExpect(jsonPath("$[1].status", is(equalTo(entity2.getStatus().name()))))

                .andExpect(jsonPath("$[2].id", is(equalTo(entity3.getId()))))
                .andExpect(jsonPath("$[2].username", is(equalTo(entity3.getUsername()))))
                .andExpect(jsonPath("$[2].password", is(equalTo("*****"))))
                .andExpect(jsonPath("$[2].status", is(equalTo(entity3.getStatus().name()))));
    }

    @Test
    public void testAddCredentialsGoodIdAndUsernameAndPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "username", "password", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);
        final CredentialsEntity credentialsEntity = new CredentialsEntity();
        credentialsEntity.setId("id");
        credentialsEntity.setUsername("username");
        credentialsEntity.setPassword("password");

        when(credentialsService.addCredentials(any(Credentials.class))).thenReturn(credentialsEntity);

        MvcResult mvcResult = mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.id", is(equalTo(credentialsInfo.getId()))))
                .andExpect(jsonPath("$.username", is(equalTo(credentialsInfo.getUsername()))))
                .andExpect(jsonPath("$.password", is(equalTo("*****"))))
                .andExpect(jsonPath("$.status", is(equalTo(CredentialsStatus.ACTIVE.name())))).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testAddCredentialsGoodIdAndUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "username", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsGoodIdAndUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "username", "", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsGoodIdAndNullUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", null, "password", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsGoodIdAndEmptyUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "", "password", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsNullIdAndGoodUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "username", "password", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsEmptyIdAndGoodUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", "username", "password", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
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
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, null, null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsNullIdAndUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, null, "", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsNullIdAndEmptyUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsNullIdAndEmptyUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "", "", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsEmptyIdAndNullUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", null, null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsEmptyIdAndNullUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", null, "", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsEmptyIdAndEmptyUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", "", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCredentialsEmptyIdAndEmptyUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", "", "", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(post(CredentialsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test(expected = CredentialsNotFoundException.class)
    public void testUpdateCredentialsGoodUsernameAndPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "username", "password", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        when(credentialsService.updateCredentials(anyString(), any(Credentials.class))).thenReturn(credentialsInfo);

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testUpdateCredentialsGoodUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "username", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        when(credentialsService.updateCredentials(anyString(), any(Credentials.class))).thenReturn(credentialsInfo);

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testUpdateCredentialsGoodUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "username", "", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        when(credentialsService.updateCredentials(anyString(), any(Credentials.class))).thenReturn(credentialsInfo);

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isAccepted());
    }

    @Test(expected = CredentialsNotFoundException.class)
    public void testUpdateCredentialsNullUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, null, "password", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        when(credentialsService.updateCredentials(anyString(), any(Credentials.class))).thenReturn(credentialsInfo);

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testUpdateCredentialsEmptyUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "", "password", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateCredentialsNullUsernameAndPassword() throws Exception {
        final Credentials credentialsInfo = new CredentialsInfo(null, null, null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotModified());
    }

    @Test
    public void testUpdateCredentialsNullUsernameAndEmptyPassword() throws Exception {
        final Credentials credentialsInfo = new CredentialsInfo(null, null, "", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotModified());
    }

    @Test
    public void testUpdateCredentialsEmptyUsernameAndNullPassword() throws Exception {
        final Credentials credentialsInfo = new CredentialsInfo(null, "", null, null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotModified());
    }

    @Test
    public void testUpdateCredentialsEmptyUsernameAndPassword() throws Exception {
        final Credentials credentialsInfo = new CredentialsInfo(null, "", "", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotModified());
    }

    @Test
    public void testUpdatePasswordCredentialsNotFound() throws Exception {
        final Credentials credentialsInfo = new CredentialsInfo("id", "username", "password", null);
        final byte[] content = mapper.writeValueAsBytes(credentialsInfo);

        doThrow(new CredentialsNotFoundException("id")).when(credentialsService).updateCredentials(anyString(), any(Credentials.class));

        mockMvc.perform(put(CredentialsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotFound());
    }

}
