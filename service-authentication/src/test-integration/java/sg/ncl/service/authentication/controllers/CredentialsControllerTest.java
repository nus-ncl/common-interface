package sg.ncl.service.authentication.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.authentication.AbstractTest;
import sg.ncl.service.authentication.data.jpa.entities.CredentialsEntity;
import sg.ncl.service.authentication.services.CredentialsService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockingDetails;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sg.ncl.service.authentication.Util.getCredentialsEntity;

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
    public void testAddCredentials() throws Exception {

    }

}
