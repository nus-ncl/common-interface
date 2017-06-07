package sg.ncl.service.data.logic;

import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.common.exception.base.NotFoundException;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.data.data.jpa.DataEntity;
import sg.ncl.service.data.data.jpa.DataRepository;
import sg.ncl.service.data.data.jpa.DataResourceEntity;
import sg.ncl.service.data.domain.*;
import sg.ncl.service.data.exceptions.DataNameAlreadyExistsException;
import sg.ncl.service.data.exceptions.DataNotFoundException;
import sg.ncl.service.data.exceptions.DataResourceNotFoundException;
import sg.ncl.service.data.util.TestUtil;
import sg.ncl.service.transmission.domain.DownloadService;
import sg.ncl.service.transmission.domain.UploadService;
import sg.ncl.service.transmission.domain.UploadStatus;
import sg.ncl.service.transmission.web.ResumableInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by jng on 17/10/16.
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class DataServiceImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private DataRepository dataRepository;
    @Mock
    private UploadService uploadService;
    @Mock
    private DownloadService downloadService;
    @Mock
    private AnalyticsService analyticsService;
    @Mock
    private AsyncAvScannerService asyncAvScannerService;
    @Mock
    private Claims claims;
    @Mock
    private HttpServletResponse response;

    private DataService dataService;

    @Before
    public void before() {
        assertThat(mockingDetails(dataRepository).isMock()).isTrue();
        assertThat(mockingDetails(uploadService).isMock()).isTrue();
        assertThat(mockingDetails(downloadService).isMock()).isTrue();
        assertThat(mockingDetails(analyticsService).isMock()).isTrue();
        assertThat(mockingDetails(asyncAvScannerService).isMock()).isTrue();
        dataService = new DataServiceImpl(dataRepository, uploadService, downloadService, analyticsService, asyncAvScannerService);
    }

    @Test
    public void testGetPublicDatasets() {
        dataService.findByVisibility(DataVisibility.PUBLIC);
        verify(dataRepository, times(1)).findByVisibility(any(DataVisibility.class));
    }

    @Test
    public void testGetDatasets() {
        dataService.getDatasets();
        verify(dataRepository, times(1)).findAll();
    }

    @Test
    public void testGetDatasetUnknownId() {
        when(dataRepository.getOne(anyLong())).thenReturn(null);
        exception.expect(DataNotFoundException.class);
        dataService.getDataset(1L);
    }

    @Test
    public void testGetDatasetknownId() {
        when(dataRepository.getOne(anyLong())).thenReturn(TestUtil.getDataEntity());
        dataService.getDataset(1L);
        verify(dataRepository, times(1)).getOne(anyLong());
    }

    @Test
    public void testSaveNewDatasetWithExistingName() {
        List<DataEntity> dataEntityList = new ArrayList<>();
        DataEntity dataEntity = TestUtil.getDataEntity();
        dataEntityList.add(dataEntity);

        when(dataRepository.findByName(anyString())).thenReturn(dataEntityList);
        exception.expect(DataNameAlreadyExistsException.class);
        dataService.createDataset(dataEntity);
    }

    @Test
    public void testSaveNewDatasetWithNewName() {
        List<DataEntity> dataEntityList = new ArrayList<>();
        DataEntity dataEntity = TestUtil.getDataEntity();
        dataEntity.setName("Old Name");
        dataEntityList.add(dataEntity);

        when(dataRepository.findByName(anyString())).thenReturn(dataEntityList);
        dataService.createDataset(TestUtil.getDataEntity());
        verify(dataRepository, times(1)).save(any(DataEntity.class));
    }

    @Test
    public void testSaveUpdatedDatasetDeniedPermission() {
        DataEntity dataEntity = TestUtil.getDataEntity();

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        exception.expect(ForbiddenException.class);

        dataService.updateDataset(dataEntity.getId(), dataEntity, claims);
    }

    @Test
    public void testSaveUpdatedDatasetGood() {
        DataEntity dataEntity = TestUtil.getDataEntity();
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn(dataEntity.getContributorId());

        dataService.updateDataset(dataEntity.getId(), dataEntity, claims);
        verify(dataRepository, times(1)).save(any(DataEntity.class));
    }

    @Test
    public void testDeleteDatasetGood() throws UnsupportedEncodingException {
        DataEntity dataEntity = TestUtil.getDataEntity();
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn(dataEntity.getContributorId());
        doNothing().when(uploadService).deleteDirectory(anyString(), anyString());

        dataService.deleteDataset(dataEntity.getId(), claims);
        verify(dataRepository, times(1)).delete(anyLong());
    }

    @Test
    public void testDeleteDatasetAdmin() throws UnsupportedEncodingException {
        DataEntity dataEntity = TestUtil.getDataEntity();
        final List<String> roles = Collections.singletonList(Role.ADMIN.getAuthority());

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn("id");
        doNothing().when(uploadService).deleteDirectory(anyString(), anyString());

        dataService.deleteDataset(dataEntity.getId(), claims);
        verify(dataRepository, times(1)).delete(anyLong());
    }

    @Test
    public void testFindResourceByIdNotAccessible() {
        DataEntity dataEntity = TestUtil.getDataEntityWithResources();
        dataEntity.setAccessibility(DataAccessibility.RESTRICTED);
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn("UnknownId");

        exception.expect(ForbiddenException.class);
        dataService.findResourceById(dataEntity.getId(), dataEntity.getResources().get(0).getId(), claims);
    }

    @Test
    public void testFindResourceByIdNotFound() {
        DataEntity dataEntity = TestUtil.getDataEntity();
        exception.expect(DataNotFoundException.class);
        dataService.findResourceById(dataEntity.getId(), 2L, claims);
    }

    @Test
    public void testFindResourceByIdFound() {
        DataEntity dataEntity = TestUtil.getDataEntity();
        List<DataResourceEntity> dataResourceList = new ArrayList<>();
        DataResourceEntity dataResourceEntity = TestUtil.getDataResourceEntity();
        dataResourceEntity.setId(1L);
        dataResourceList.add(dataResourceEntity);
        dataEntity.setResources(dataResourceList);

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);

        DataResource dataResource = dataService.findResourceById(dataEntity.getId(), dataEntity.getResources().get(0).getId(), claims);
        assertThat(dataResource).isEqualTo(dataEntity.getResources().get(0));
    }

    @Test
    public void testFindResourceNull() {
        DataEntity dataEntity = TestUtil.getDataEntity();
        List<DataResourceEntity> dataResourceList = new ArrayList<>();
        DataResourceEntity dataResourceEntity = TestUtil.getDataResourceEntity();
        dataResourceEntity.setId(1L);
        dataResourceList.add(dataResourceEntity);
        dataEntity.setResources(dataResourceList);

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);

        exception.expect(DataResourceNotFoundException.class);
        dataService.findResourceById(dataEntity.getId(), 2L, claims);
    }

    @Test
    public void testSaveResource() {
        DataEntity dataEntity = TestUtil.getDataEntity();
        DataResourceEntity dataResourceEntity = TestUtil.getDataResourceEntity();
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn(dataEntity.getContributorId());

        dataService.createResource(dataEntity.getId(), dataResourceEntity, claims);
        verify(dataRepository, times(1)).save(any(DataEntity.class));
    }

    @Test
    public void testDeleteResource() throws IOException {
        DataEntity dataEntity = TestUtil.getDataEntity();
        dataEntity.setId(1L);
        List<DataResourceEntity> dataResourceList = new ArrayList<>();
        DataResourceEntity dataResourceEntity = TestUtil.getDataResourceEntity();
        dataResourceEntity.setId(1L);
        dataResourceList.add(dataResourceEntity);
        dataEntity.setResources(dataResourceList);
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn(dataEntity.getContributorId());
        when(uploadService.deleteUpload(anyString(), anyString(), anyString())).thenReturn(true);

        dataService.deleteResource(dataEntity.getId(), dataEntity.getResources().get(0).getId(), claims);
        verify(dataRepository, times(1)).save(any(DataEntity.class));
    }

    @Test
    public void testCheckChunkUploaded() {
        String resumableIdentifier = "identifier";
        String resumableChunkNumber = "1";

        when(uploadService.checkChunk(anyString(), anyInt())).thenReturn(UploadStatus.UPLOADED);

        String result = dataService.checkChunk(resumableIdentifier, resumableChunkNumber);
        assertThat(result).isEqualTo("Uploaded.");
    }

    @Test
    public void testCheckChunkNotFound() {
        String resumableIdentifier = "identifier";
        String resumableChunkNumber = "1";

        when(uploadService.checkChunk(anyString(), anyInt())).thenReturn(UploadStatus.NOT_FOUND);

        String result = dataService.checkChunk(resumableIdentifier, resumableChunkNumber);
        assertThat(result).isEqualTo("Not found");
    }

    @Test
    public void  testAddChunkDataNotFound() throws UnsupportedEncodingException {
        ResumableInfo resumableInfo = TestUtil.getResumableInfo();

        when(uploadService.addChunk(any(ResumableInfo.class), anyInt(), anyString(), anyString())).thenReturn(UploadStatus.FINISHED);
        exception.expect(DataNotFoundException.class);

        dataService.addChunk(resumableInfo, "1", 1L, claims);
    }

    @Test
    public void testAddChunkFinished() throws UnsupportedEncodingException {
        ResumableInfo resumableInfo = TestUtil.getResumableInfo();
        DataEntity dataEntity = TestUtil.getDataEntity();
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn(dataEntity.getContributorId());
        when(uploadService.addChunk(any(ResumableInfo.class), anyInt(), anyString(), anyString())).thenReturn(UploadStatus.FINISHED);

        String result = dataService.addChunk(resumableInfo, "1", 1L, claims);
        assertThat(result).isEqualTo("All finished.");
    }

    @Test
    public void testAddChunkUpload() throws UnsupportedEncodingException {
        ResumableInfo resumableInfo = TestUtil.getResumableInfo();
        DataEntity dataEntity = TestUtil.getDataEntity();

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(uploadService.addChunk(any(ResumableInfo.class), anyInt(), anyString(), anyString())).thenReturn(UploadStatus.UPLOAD);

        String result = dataService.addChunk(resumableInfo, "1", 1L, claims);
        assertThat(result).isEqualTo("Upload");
    }

    @Test
    public void testDownloadResource() {
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());
        DataEntity dataEntity = TestUtil.getDataEntity();
        List<DataResourceEntity> dataResourceList = new ArrayList<>();
        DataResourceEntity dataResourceEntity = TestUtil.getDataResourceEntity();
        dataResourceEntity.setId(1L);
        dataResourceList.add(dataResourceEntity);
        dataEntity.setResources(dataResourceList);

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn(dataEntity.getContributorId());
        try {
            doThrow(new IOException()).when(downloadService).getChunks(any(HttpServletResponse.class), anyString(), anyString(), anyString());
        } catch (IOException e) {
            throw new RuntimeException();
        }

        exception.expect(NotFoundException.class);
        dataService.downloadResource(response, 1L, 1L, claims);
    }

    @Test
    public void testScheduleDataResourceScan() throws UnsupportedEncodingException {
        DataEntity dataEntity = TestUtil.getDataEntity();
        List<DataEntity> dataList = new ArrayList<>();
        List<DataResourceEntity> dataResourceList = new ArrayList<>();

        DataResourceEntity dataResourceEntity = TestUtil.getDataResourceEntity();
        dataResourceEntity.setId(1L);
        dataResourceEntity.setMalicious(false);
        dataResourceList.add(dataResourceEntity);
        dataEntity.setResources(dataResourceList);

        dataList.add(dataEntity);

        when(dataRepository.findAll().stream().collect(Collectors.toList())).thenReturn(dataList);
        when(dataRepository.findOne(anyLong())).thenReturn(dataEntity);
        when(asyncAvScannerService.scanResource(any(DataEntity.class), any(DataResource.class), anyString(), anyString())).thenReturn(dataEntity);

        dataService.scheduledDataResourceScan();

        verify(asyncAvScannerService, times(1)).scanResource(any(DataEntity.class), any(DataResource.class), anyString(), anyString());
    }

    // async scanner service in schedule scan throws unsupported encoding exception
    @Test
    public void testScheduleDataResourceScanException() throws UnsupportedEncodingException {
        DataEntity dataEntity = TestUtil.getDataEntity();
        List<DataEntity> dataList = new ArrayList<>();
        List<DataResourceEntity> dataResourceList = new ArrayList<>();

        DataResourceEntity dataResourceEntity = TestUtil.getDataResourceEntity();
        dataResourceEntity.setId(1L);
        dataResourceEntity.setMalicious(false);
        dataResourceList.add(dataResourceEntity);
        dataEntity.setResources(dataResourceList);

        dataList.add(dataEntity);

        when(dataRepository.findAll().stream().collect(Collectors.toList())).thenReturn(dataList);
        when(dataRepository.findOne(anyLong())).thenReturn(dataEntity);
        when(asyncAvScannerService.scanResource(any(DataEntity.class), any(DataResource.class), anyString(), anyString())).thenThrow(new UnsupportedEncodingException());

        dataService.scheduledDataResourceScan();

        verify(asyncAvScannerService, times(1)).scanResource(any(DataEntity.class), any(DataResource.class), anyString(), anyString());
    }

}
