package sg.ncl.service.data.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import sg.ncl.service.data.data.jpa.DataResourceEntity;
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
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static sg.ncl.service.data.util.TestUtil.asJsonString;

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
    private ObjectMapper mapper;
    @Inject
    private WebApplicationContext webApplicationContext;

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
    public void testGetPublicDatasetsWithNothingInDb() throws Exception {
        mockMvc.perform(get(DataController.PATH + "?visibility=PUBLIC"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetPublicDatasets() throws Exception {
        final List<Data> dataSets = new ArrayList<>();
        DataEntity publicDataSet1 = TestUtil.getDataEntity();
        DataEntity publicDataSet2 = TestUtil.getDataEntity();
        dataSets.add(publicDataSet1);
        dataSets.add(publicDataSet2);

        when(dataService.findByVisibility(any(DataVisibility.class))).thenReturn(dataSets);

        mockMvc.perform(get(DataController.PATH + "?visibility=PUBLIC"))
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
    public void testGetAllDatasetsGoodAuthenticationPrincipal() throws Exception {
        final List<Data> dataSets = new ArrayList<>();
        DataEntity dataEntity = TestUtil.getDataEntity();
        dataSets.add(dataEntity);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.getDatasets()).thenReturn(dataSets);

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
    public void testGetAllDatasetsBadAuthenticationPrincipal() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn("");
        when(dataService.getDatasets()).thenReturn(null);

        mockMvc.perform(get(DataController.PATH))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetAllDatasetsNullAuthentication() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(null);
        when(dataService.getDatasets()).thenReturn(null);

        mockMvc.perform(get(DataController.PATH))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetDatasetByIdGoodAuthentication() throws Exception {
        final DataEntity dataEntity = TestUtil.getDataEntity();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.getDataset(anyLong())).thenReturn(dataEntity);

        mockMvc.perform(get(DataController.PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("name", is(equalTo(dataEntity.getName()))))
                .andExpect(jsonPath("description", is(equalTo(dataEntity.getDescription()))))
                .andExpect(jsonPath("contributorId", is(equalTo(dataEntity.getContributorId()))))
                .andExpect(jsonPath("accessibility", is(equalTo(dataEntity.getAccessibility().toString()))))
                .andExpect(jsonPath("visibility", is(equalTo(dataEntity.getVisibility().toString()))));
    }

    @Test
    public void testAddDatasetGoodAuthentication() throws Exception {
        final DataEntity dataEntity = TestUtil.getDataEntity();

        mapper.registerModule(new JavaTimeModule());
        final byte[] content = mapper.writeValueAsBytes(new DataInfo(dataEntity));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.createDataset(any(DataInfo.class))).thenReturn(dataEntity);

        mockMvc.perform(post(DataController.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))

                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("name", is(equalTo(dataEntity.getName()))))
                .andExpect(jsonPath("description", is(equalTo(dataEntity.getDescription()))))
                .andExpect(jsonPath("contributorId", is(equalTo(dataEntity.getContributorId()))))
                .andExpect(jsonPath("accessibility", is(equalTo(dataEntity.getAccessibility().toString()))))
                .andExpect(jsonPath("visibility", is(equalTo(dataEntity.getVisibility().toString()))));
    }

    @Test
    public void testUpdateDatasetGoodAuthentication() throws Exception {
        final DataEntity dataEntity = TestUtil.getDataEntity();

        mapper.registerModule(new JavaTimeModule());
        final byte[] content = mapper.writeValueAsBytes(new DataInfo(dataEntity));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.updateDataset(anyLong(), any(DataInfo.class), any(Claims.class))).thenReturn(dataEntity);

        mockMvc.perform(put(DataController.PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))

                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("name", is(equalTo(dataEntity.getName()))))
                .andExpect(jsonPath("description", is(equalTo(dataEntity.getDescription()))))
                .andExpect(jsonPath("contributorId", is(equalTo(dataEntity.getContributorId()))))
                .andExpect(jsonPath("accessibility", is(equalTo(dataEntity.getAccessibility().toString()))))
                .andExpect(jsonPath("visibility", is(equalTo(dataEntity.getVisibility().toString()))));
    }

    @Test
    public void testDeleteDatasetGoodAuthentication() throws Exception {
        final DataEntity dataEntity = TestUtil.getDataEntity();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.deleteDataset(anyLong(), any(Claims.class))).thenReturn(dataEntity);

        mockMvc.perform(delete(DataController.PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("name", is(equalTo(dataEntity.getName()))))
                .andExpect(jsonPath("description", is(equalTo(dataEntity.getDescription()))))
                .andExpect(jsonPath("contributorId", is(equalTo(dataEntity.getContributorId()))))
                .andExpect(jsonPath("accessibility", is(equalTo(dataEntity.getAccessibility().toString()))))
                .andExpect(jsonPath("visibility", is(equalTo(dataEntity.getVisibility().toString()))));
    }

    @Test
    public void testGetResourceGoodAuthentication() throws Exception {
        final DataResourceEntity dataResourceEntity =TestUtil.getDataResourceEntity();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.findResourceById(anyLong(), anyLong(), any(Claims.class))).thenReturn(dataResourceEntity);

        mockMvc.perform(get(DataController.PATH + "/1/resources/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("uri", is(equalTo(dataResourceEntity.getUri()))));
    }

    @Test
    public void testAddResourceGoodAuthentication() throws Exception {
        final DataEntity dataEntity = TestUtil.getDataEntityWithResources();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.createResource(anyLong(), any(DataResourceInfo.class), any(Claims.class))).thenReturn(dataEntity);

        mockMvc.perform(post(DataController.PATH + "/1/resources")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dataEntity.getResources().get(0))))

                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("name", is(equalTo(dataEntity.getName()))))
                .andExpect(jsonPath("description", is(equalTo(dataEntity.getDescription()))))
                .andExpect(jsonPath("contributorId", is(equalTo(dataEntity.getContributorId()))))
                .andExpect(jsonPath("accessibility", is(equalTo(dataEntity.getAccessibility().toString()))))
                .andExpect(jsonPath("visibility", is(equalTo(dataEntity.getVisibility().toString()))))

                .andExpect(jsonPath("resources.[0].uri", is(equalTo(dataEntity.getResources().get(0).getUri()))));
    }

    @Test
    public void testDeleteDataResourceGoodAuthentication() throws Exception {
        final DataEntity dataEntity = TestUtil.getDataEntity();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.deleteResource(anyLong(), anyLong(), any(Claims.class))).thenReturn(dataEntity);

        mockMvc.perform(delete(DataController.PATH + "/1/resources/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("name", is(equalTo(dataEntity.getName()))))
                .andExpect(jsonPath("description", is(equalTo(dataEntity.getDescription()))))
                .andExpect(jsonPath("contributorId", is(equalTo(dataEntity.getContributorId()))))
                .andExpect(jsonPath("accessibility", is(equalTo(dataEntity.getAccessibility().toString()))))
                .andExpect(jsonPath("visibility", is(equalTo(dataEntity.getVisibility().toString()))))

                .andExpect(jsonPath("resources", hasSize(0)));
    }

}
