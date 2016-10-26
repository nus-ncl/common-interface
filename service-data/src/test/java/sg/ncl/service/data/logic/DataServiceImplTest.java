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
import sg.ncl.service.data.AbstractTest;
import sg.ncl.service.data.data.jpa.DataEntity;
import sg.ncl.service.data.data.jpa.DataRepository;
import sg.ncl.service.data.data.jpa.DataResourceEntity;
import sg.ncl.service.data.domain.DataAccessibility;
import sg.ncl.service.data.domain.DataResource;
import sg.ncl.service.data.domain.DataService;
import sg.ncl.service.data.domain.DataVisibility;
import sg.ncl.service.data.exceptions.DataNameInUseException;
import sg.ncl.service.data.exceptions.DataNotFoundException;
import sg.ncl.service.data.util.TestUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by jng on 17/10/16.
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class DataServiceImplTest extends AbstractTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private DataRepository dataRepository;
    @Mock
    private Claims claims;

    private DataService dataService;

    @Before
    public void before() {
        assertThat(mockingDetails(dataRepository).isMock()).isTrue();
        dataService = new DataServiceImpl(dataRepository);
    }

    @Test
    public void testGetPublicDatasets() {
        dataService.findByVisibility(DataVisibility.PUBLIC);
        verify(dataRepository, times(1)).findByVisibility(any(DataVisibility.class));
    }

    @Test
    public void testGetDatasets() {
        dataService.getAll();
        verify(dataRepository, times(1)).findAll();
    }

    @Test
    public void testGetDatasetUnknownId() {
        when(dataRepository.getOne(anyLong())).thenReturn(null);
        exception.expect(DataNotFoundException.class);
        dataService.getOne(1L);
    }

    @Test
    public void testGetDatasetknownId() {
        when(dataRepository.getOne(anyLong())).thenReturn(TestUtil.getDataEntity());
        dataService.getOne(1L);
        verify(dataRepository, times(1)).getOne(anyLong());
    }

    @Test
    public void testSaveNewDatasetWithExistingName() {
        List<DataEntity> dataEntityList = new ArrayList<>();
        DataEntity dataEntity = TestUtil.getDataEntity();
        dataEntityList.add(dataEntity);

        when(dataRepository.findByName(anyString())).thenReturn(dataEntityList);
        exception.expect(DataNameInUseException.class);
        dataService.save(dataEntity);
    }

    @Test
    public void testSaveNewDatasetWithNewName() {
        List<DataEntity> dataEntityList = new ArrayList<>();
        DataEntity dataEntity = TestUtil.getDataEntity();
        dataEntity.setName("Old Name");
        dataEntityList.add(dataEntity);

        when(dataRepository.findByName(anyString())).thenReturn(dataEntityList);
        dataService.save(TestUtil.getDataEntity());
        verify(dataRepository, times(1)).save(any(DataEntity.class));
    }

    @Test
    public void testSaveUpdatedDatasetDeniedPermission() {
        DataEntity dataEntity = TestUtil.getDataEntity();

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        exception.expect(ForbiddenException.class);

        dataService.save(dataEntity.getId(), dataEntity, claims);
    }

    @Test
    public void testSaveUpdatedDatasetGood() {
        DataEntity dataEntity = TestUtil.getDataEntity();
        final List<Role> roles = Collections.singletonList(Role.USER);

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn(dataEntity.getContributorId());

        dataService.save(dataEntity.getId(), dataEntity, claims);
        verify(dataRepository, times(1)).save(any(DataEntity.class));
    }

    @Test
    public void testDeleteDatasetGood() {
        DataEntity dataEntity = TestUtil.getDataEntity();
        final List<Role> roles = Collections.singletonList(Role.USER);

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn(dataEntity.getContributorId());

        dataService.delete(dataEntity.getId(), claims);
        verify(dataRepository, times(1)).delete(anyLong());
    }

    @Test
    public void testFindResourceByIdNotAccessible() {
        DataEntity dataEntity = TestUtil.getDataEntityWithResources();
        dataEntity.setAccessibility(DataAccessibility.RESTRICTED);
        final List<Role> roles = Collections.singletonList(Role.USER);

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn("UnknownId");

        exception.expect(ForbiddenException.class);
        dataService.findResourceById(dataEntity.getId(), dataEntity.getResources().get(0).getId(), claims);
    }

    @Test
    public void testFindResourceByIdNotFound() {
        DataEntity dataEntity = TestUtil.getDataEntity();
        exception.expect(NotFoundException.class);
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
    public void testSaveResource() {
        DataEntity dataEntity = TestUtil.getDataEntity();
        DataResourceEntity dataResourceEntity = TestUtil.getDataResourceEntity();
        final List<Role> roles = Collections.singletonList(Role.USER);

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn(dataEntity.getContributorId());

        dataService.saveResource(dataEntity.getId(), dataResourceEntity, claims);
        verify(dataRepository, times(1)).save(any(DataEntity.class));
    }

    @Test
    public void testDeleteResource() {
        DataEntity dataEntity = TestUtil.getDataEntity();
        DataResourceEntity dataResourceEntity = TestUtil.getDataResourceEntity();
        final List<Role> roles = Collections.singletonList(Role.USER);

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn(dataEntity.getContributorId());

        dataService.deleteResource(dataEntity.getId(), dataResourceEntity.getId(), claims);
        verify(dataRepository, times(1)).save(any(DataEntity.class));
    }

}
