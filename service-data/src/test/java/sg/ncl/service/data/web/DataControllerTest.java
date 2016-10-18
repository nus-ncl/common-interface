package sg.ncl.service.data.web;

import io.jsonwebtoken.Claims;
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
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.exception.GlobalExceptionHandler;
import sg.ncl.service.data.data.jpa.DataEntity;
import sg.ncl.service.data.domain.Data;
import sg.ncl.service.data.domain.DataService;
import sg.ncl.service.data.domain.DataVisibility;
import sg.ncl.service.data.util.TestUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by jng on 18/10/16.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = DataController.class)
@ContextConfiguration(classes = {DataController.class, ExceptionAutoConfiguration.class, GlobalExceptionHandler.class})
public class DataControllerTest {

    @Mock
    private Claims claims;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @MockBean
    private DataService dataService;

    @Inject
    private WebApplicationContext webApplicationContext;
    @Inject
    private MockMvc mockMvc;

    @Before
    public void before() {
        assertThat(mockingDetails(claims).isMock()).isTrue();
        assertThat(mockingDetails(securityContext).isMock()).isTrue();
        assertThat(mockingDetails(authentication).isMock()).isTrue();
        assertThat(mockingDetails(dataService).isMock()).isTrue();
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetPublicDataSetsWithNothingInDb() throws Exception {
        mockMvc.perform(get(DataController.PATH + "/public"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetPublicDataSets() throws Exception {
        final List<Data> dataSets = new ArrayList<>();
        DataEntity publicDataSet1 = TestUtil.getDataEntity();
        DataEntity publicDataSet2 = TestUtil.getDataEntity();
        dataSets.add(publicDataSet1);
        dataSets.add(publicDataSet2);

        when(dataService.findByVisibility(any(DataVisibility.class))).thenReturn(dataSets);

        mockMvc.perform(get(DataController.PATH + "/public"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].name", is(equalTo(publicDataSet1.getName()))))
                .andExpect(jsonPath("$[0].description", is(equalTo(publicDataSet1.getDescription()))))
                .andExpect(jsonPath("$[0].contributorId", is(equalTo(publicDataSet1.getContributorId()))))
                .andExpect(jsonPath("$[0].accessibility", is(equalTo(publicDataSet1.getAccessibility().toString()))))
                .andExpect(jsonPath("$[0].visibility", is(equalTo(publicDataSet1.getVisibility().toString()))))

                .andExpect(jsonPath("$[1].name", is(equalTo(publicDataSet2.getName()))))
                .andExpect(jsonPath("$[1].description", is(equalTo(publicDataSet2.getDescription()))))
                .andExpect(jsonPath("$[1].contributorId", is(equalTo(publicDataSet2.getContributorId()))))
                .andExpect(jsonPath("$[1].accessibility", is(equalTo(publicDataSet2.getAccessibility().toString()))))
                .andExpect(jsonPath("$[1].visibility", is(equalTo(publicDataSet2.getVisibility().toString()))));
    }

    @Test
    public void testGetAllDataSetsGoodAuthenticationPrincipal() throws Exception {
        final List<Data> dataSets = new ArrayList<>();
        DataEntity dataEntity = TestUtil.getDataEntity();
        dataSets.add(dataEntity);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.getAll()).thenReturn(dataSets);

        mockMvc.perform(get(DataController.PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))

                .andExpect(jsonPath("$[0].name", is(equalTo(dataEntity.getName()))))
                .andExpect(jsonPath("$[0].description", is(equalTo(dataEntity.getDescription()))))
                .andExpect(jsonPath("$[0].contributorId", is(equalTo(dataEntity.getContributorId()))))
                .andExpect(jsonPath("$[0].accessibility", is(equalTo(dataEntity.getAccessibility().toString()))))
                .andExpect(jsonPath("$[0].visibility", is(equalTo(dataEntity.getVisibility().toString()))));
    }

    @Test
    public void testGetAllDataSetsBadAuthenticationPrincipal() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn("");
        when(dataService.getAll()).thenReturn(null);

        mockMvc.perform(get(DataController.PATH))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetAllDataSetsNullAuthentication() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(null);
        when(dataService.getAll()).thenReturn(null);

        mockMvc.perform(get(DataController.PATH))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

}
