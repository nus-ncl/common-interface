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
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.service.data.AbstractTest;
import sg.ncl.service.data.data.jpa.DataEntity;
import sg.ncl.service.data.data.jpa.DataRepository;
import sg.ncl.service.data.domain.DataService;
import sg.ncl.service.data.domain.DataVisibility;
import sg.ncl.service.data.exceptions.DataNameInUseException;
import sg.ncl.service.data.exceptions.DataNotFoundException;
import sg.ncl.service.data.util.TestUtil;

import java.util.ArrayList;
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

}
