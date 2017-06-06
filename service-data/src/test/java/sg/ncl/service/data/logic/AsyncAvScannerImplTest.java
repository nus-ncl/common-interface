package sg.ncl.service.data.logic;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import sg.ncl.service.data.data.jpa.DataEntity;
import sg.ncl.service.data.data.jpa.DataRepository;
import sg.ncl.service.data.data.jpa.DataResourceEntity;
import sg.ncl.service.data.domain.AsyncAvScannerService;
import sg.ncl.service.data.domain.AvScannerService;
import sg.ncl.service.data.domain.Data;
import sg.ncl.service.data.domain.DataResource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static sg.ncl.service.data.util.TestUtil.getDataResourceEntity;

/**
 * Created by dcsyeoty on 06-Jun-17.
 */
public class AsyncAvScannerImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private AvScannerService avScannerService;
    @Mock
    private DataRepository dataRepository;

    private AsyncAvScannerService asyncAvScannerService;

    @Before
    public void before() {
        assertThat(mockingDetails(avScannerService).isMock()).isTrue();
        assertThat(mockingDetails(dataRepository).isMock()).isTrue();
        asyncAvScannerService = new AsyncAvScannerImpl(avScannerService, dataRepository);
    }

    @Test
    public void testScanResource() throws Exception {
        DataEntity dataEntity = new DataEntity();
        DataResourceEntity dataResourceEntity = getDataResourceEntity();

        dataEntity.addResource(dataResourceEntity);

        when(avScannerService.scan(anyString(), anyString(), anyString())).thenReturn(true);
        when(dataRepository.save(any(DataEntity.class))).thenReturn(dataEntity);

        Data result = asyncAvScannerService.scanResource(dataEntity, dataResourceEntity, "data", "utf8");

        verify(dataRepository, times(1)).save(dataEntity);
        assertThat(result.getResources().stream().anyMatch(DataResource::isMalicious));
    }
}
