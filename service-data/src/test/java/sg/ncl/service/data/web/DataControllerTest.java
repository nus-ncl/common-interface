package sg.ncl.service.data.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.exception.GlobalExceptionHandler;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.data.data.jpa.*;
import sg.ncl.service.data.domain.*;
import sg.ncl.service.data.util.TestUtil;
import sg.ncl.service.transmission.web.ResumableInfo;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static sg.ncl.service.data.util.TestUtil.asJsonString;
import static sg.ncl.service.data.util.TestUtil.getDataCategoryEntity;

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
    @MockBean
    private DataAccessRequestService dataAccessRequestService;

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
        assertThat(mockingDetails(dataAccessRequestService).isMock()).isTrue();
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSearchDatasetsGoodAuthentication() throws Exception {
        final Set<Data> dataSets = new HashSet<>();
        DataEntity dataEntity = TestUtil.getDataEntity();
        dataEntity.setCategoryId(getDataCategoryEntity().getId());
        dataSets.add(dataEntity);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.searchDatasets(any(String[].class))).thenReturn(dataSets);

        mockMvc.perform(get(DataController.PATH + "/search?keyword=dataname"))
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
    public void testSearchDatasetsBadAuthentication() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn("");
        when(dataService.searchDatasets(any(String[].class))).thenReturn(null);

        mockMvc.perform(get(DataController.PATH + "/search?keyword=dataname"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetPublicDatasetsWithNothingInDb() throws Exception {
        mockMvc.perform(get(DataController.PATH + "?visibility=PUBLIC"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetPublicDatasetsNotPublic() throws Exception {
        final List<Data> dataSets = new ArrayList<>();
        DataEntity publicDataSet1 = TestUtil.getDataEntity();
        DataEntity publicDataSet2 = TestUtil.getDataEntity();
        dataSets.add(publicDataSet1);
        dataSets.add(publicDataSet2);

        when(dataService.findByVisibility(any(DataVisibility.class))).thenReturn(dataSets);

        mockMvc.perform(get(DataController.PATH + "?visibility=PRIVATE"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetPublicDatasets() throws Exception {
        final List<Data> dataSets = new ArrayList<>();
        DataEntity publicDataSet1 = TestUtil.getDataEntity();
        DataEntity publicDataSet2 = TestUtil.getDataEntity();
        publicDataSet1.setCategoryId(getDataCategoryEntity().getId());
        publicDataSet2.setCategoryId(getDataCategoryEntity().getId());
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
    public void testGetPublicDatasetById() throws Exception {
        final DataEntity dataEntity = TestUtil.getDataEntity();
        dataEntity.setCategoryId(getDataCategoryEntity().getId());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.getDataset(anyLong())).thenReturn(dataEntity);

        mockMvc.perform(get(DataController.PATH + "/1?visibility=PUBLIC"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("name", is(equalTo(dataEntity.getName()))))
                .andExpect(jsonPath("description", is(equalTo(dataEntity.getDescription()))))
                .andExpect(jsonPath("contributorId", is(equalTo(dataEntity.getContributorId()))))
                .andExpect(jsonPath("accessibility", is(equalTo(dataEntity.getAccessibility().toString()))))
                .andExpect(jsonPath("visibility", is(equalTo(dataEntity.getVisibility().toString()))));
    }

    @Test
    public void testGetPublicDatasetByIdParameterPrivate() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(null);
        when(dataService.getDataset(anyLong())).thenReturn(null);

        mockMvc.perform(get(DataController.PATH + "/1?visibility=PRIVATE"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetPublicDatasetByIdVisibilityPrivate() throws Exception {
        final DataEntity dataEntity = TestUtil.getDataEntity();
        dataEntity.setCategoryId(getDataCategoryEntity().getId());
        dataEntity.setVisibility(DataVisibility.PRIVATE);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(null);
        when(dataService.getDataset(anyLong())).thenReturn(dataEntity);

        mockMvc.perform(get(DataController.PATH + "/1?visibility=PUBLIC"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetAllDatasetsGoodAuthenticationPrincipal() throws Exception {
        final List<Data> dataSets = new ArrayList<>();
        DataEntity dataEntity = TestUtil.getDataEntity();
        dataEntity.setCategoryId(getDataCategoryEntity().getId());
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
        dataEntity.setCategoryId(getDataCategoryEntity().getId());

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
    public void testGetDatasetByIdBadAuthentication() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn("");
        when(dataService.getDataset(anyLong())).thenReturn(null);

        mockMvc.perform(get(DataController.PATH + "/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddDatasetGoodAuthentication() throws Exception {
        final DataEntity dataEntity = TestUtil.getDataEntity();
        dataEntity.setCategoryId(getDataCategoryEntity().getId());

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
        dataEntity.setCategoryId(getDataCategoryEntity().getId());

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
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
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
        dataEntity.setCategoryId(getDataCategoryEntity().getId());

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
        dataEntity.setCategoryId(getDataCategoryEntity().getId());

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

    @Test
    public void testUpdateResourceBadAuthentication() throws Exception {
        final DataResourceEntity dataResourceEntity = TestUtil.getDataResourceEntity();
        final byte[] content = mapper.writeValueAsBytes(dataResourceEntity);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getPrincipal()).thenReturn("");
        when(dataService.updateResource(anyLong(), any(DataResource.class), any())).thenReturn(null);

        mockMvc.perform(put(DataController.PATH + "/1/resources/1").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateResourceForbidden() throws Exception {
        final DataResourceEntity dataResourceEntity = TestUtil.getDataResourceEntity();
        final byte[] content = mapper.writeValueAsBytes(dataResourceEntity);
        final List<String> roles = new ArrayList<>();

        roles.add(Role.USER.getAuthority());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);

        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(dataService.updateResource(anyLong(), any(DataResource.class), any())).thenReturn(null);

        mockMvc.perform(put(DataController.PATH + "/1/resources/1").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateResourceGoodAuthentication() throws Exception {
        final DataEntity dataEntity = TestUtil.getDataEntity();
        final DataResourceEntity dataResourceEntity = TestUtil.getDataResourceEntity();
        List<DataResourceEntity> dataResourceEntityList = new ArrayList<>();

        dataResourceEntity.setId(1L);
        dataResourceEntity.setMalicious(false);
        dataResourceEntity.setScanned(true);

        dataResourceEntityList.add(dataResourceEntity);
        dataEntity.setResources(dataResourceEntityList);

        final byte[] content = mapper.writeValueAsBytes(dataResourceEntity);
        final List<String> roles = new ArrayList<>();

        roles.add(Role.ADMIN.getAuthority());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);

        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(dataService.updateResource(anyLong(), any(DataResource.class), any())).thenReturn(dataEntity);

        mockMvc.perform(put(DataController.PATH + "/1/resources/1").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.resources", hasSize(1)))
                .andExpect(jsonPath("$.resources[0].malicious", is(equalTo(false))))
                .andExpect(jsonPath("$.resources[0].scanned", is(equalTo(true))));
    }

    @Test
    public void testCheckUpload() throws Exception {
        when(dataService.checkChunk(anyString(), anyString())).thenReturn("Uploaded.");
        MvcResult result = mockMvc.perform(get(DataController.PATH + "/1/chunks/1/files/identifier"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertThat(content).isEqualTo("Uploaded.");
    }

    @Test
    public void testFileUploadUnauthorized() throws Exception {
        final ResumableInfo resumableInfo = TestUtil.getResumableInfo();
        final byte[] content = mapper.writeValueAsBytes(resumableInfo);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(null);

        mockMvc.perform(post(DataController.PATH + "/1/chunks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testFileUploadAuthorized() throws Exception {
        final ResumableInfo resumableInfo = TestUtil.getResumableInfo();
        final byte[] content = mapper.writeValueAsBytes(resumableInfo);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.addChunk(any(ResumableInfo.class), anyString(), anyLong(), any(Claims.class))).thenReturn("Upload");

        MvcResult result = mockMvc.perform(post(DataController.PATH + "/1/chunks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isAccepted())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo("Upload");
    }

    @Test
    public void testDownloadResourceUnauthorized() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(null);
        mockMvc.perform(get(DataController.PATH + "/1/resources/1/download"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDownloadResourceAuthorized() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);

        mockMvc.perform(get(DataController.PATH + "/1/resources/1/download"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddRequest() throws Exception {
        DataAccessRequestEntity entity = TestUtil.getDataAccessRequestEntity();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataAccessRequestService.createRequest(anyLong(), asJsonString(anyString()), any(Claims.class))).thenReturn(entity);

        JSONObject object = new JSONObject();
        object.put("reason", "test");

        mockMvc.perform(post(DataController.PATH + "/1/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object.toString()))
                .andExpect(status().isCreated());
    }

    @Test
    public void testProcessRequest() throws Exception {
        DataAccessRequestEntity entity = TestUtil.getDataAccessRequestEntity();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataAccessRequestService.approveRequest(anyLong(), anyLong(), any(Claims.class))).thenReturn(entity);

        mockMvc.perform(put(DataController.PATH + "/1/requests/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRequest() throws Exception {
        DataAccessRequestEntity entity = TestUtil.getDataAccessRequestEntity();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataAccessRequestService.getRequest(anyLong(), anyLong(), any(Claims.class))).thenReturn(entity);

        mockMvc.perform(get(DataController.PATH + "/1/requests/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCategoriesGoodAuthentication() throws Exception {
        List<DataCategory> dataCategoryEntities = new ArrayList<>();
        DataCategoryEntity dataCategoryEntity1 = TestUtil.getDataCategoryEntity();
        DataCategoryEntity dataCategoryEntity2 = TestUtil.getDataCategoryEntity();
        dataCategoryEntities.add(dataCategoryEntity1);
        dataCategoryEntities.add(dataCategoryEntity2);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.getCategories()).thenReturn(dataCategoryEntities);

        mockMvc.perform(get(DataController.PATH + "/categories"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCategoriesBadAuthentication() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(null);

        mockMvc.perform(get(DataController.PATH + "/categories"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetCategory() throws Exception {
        DataCategoryEntity dataCategoryEntity = TestUtil.getDataCategoryEntity();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.getCategory(anyLong())).thenReturn(dataCategoryEntity);

        mockMvc.perform(get(DataController.PATH + "/categories/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetLicensesGoodAuthentication() throws Exception {
        List<DataLicense> dataLicenseEntities = new ArrayList<>();
        DataLicenseEntity dataLicenseEntity1 = TestUtil.getDataLicenseEntity();
        DataLicenseEntity dataLicenseEntity2 = TestUtil.getDataLicenseEntity();
        dataLicenseEntities.add(dataLicenseEntity1);
        dataLicenseEntities.add(dataLicenseEntity2);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.getLicenses()).thenReturn(dataLicenseEntities);

        mockMvc.perform(get(DataController.PATH + "/licenses"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetLicensesBadAuthentication() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(null);

        mockMvc.perform(get(DataController.PATH + "/licenses"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetLicence() throws Exception {
        DataLicenseEntity dataLicenseEntity = TestUtil.getDataLicenseEntity();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(dataService.getLicense(anyLong())).thenReturn(dataLicenseEntity);

        mockMvc.perform(get(DataController.PATH + "/licenses/1"))
                .andExpect(status().isOk());
    }

}
