package sg.ncl.service.analytics.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.analytics.data.jpa.DataDownloadEntity;
import sg.ncl.service.analytics.data.jpa.DataDownloadRepository;
import sg.ncl.service.analytics.data.jpa.DataDownloadStatistics;
import sg.ncl.service.analytics.domain.AnalyticsService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by dcszwang on 12/28/2016.
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class AnalyticsServiceImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private DataDownloadRepository dataDownloadRepository;
    @Mock
    private AdapterDeterLab adapterDeterLab;

    private AnalyticsService analyticsService;

    @Before
    public void before() {
        assertThat(mockingDetails(dataDownloadRepository).isMock()).isTrue();
        assertThat(mockingDetails(adapterDeterLab).isMock()).isTrue();
        analyticsService = new AnalyticsServiceImpl(dataDownloadRepository, adapterDeterLab);
    }

    @Test
    public void testAddDataDownloadRecordGood() {
        Long dataId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        Long resourceId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        ZonedDateTime date = ZonedDateTime.now();
        String userId = RandomStringUtils.randomAlphanumeric(20);

        DataDownloadEntity entity = new DataDownloadEntity();
        entity.setId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setDataId(dataId);
        entity.setResourceId(resourceId);
        entity.setDownloadDate(date);
        entity.setHashedUserId(userId);

        when(dataDownloadRepository.save(any(DataDownloadEntity.class))).thenReturn(entity);
        analyticsService.addDataDownloadRecord(dataId, resourceId, date, userId);
        verify(dataDownloadRepository, times(1)).save(any(DataDownloadEntity.class));
    }

    @Test
    public void testGetDataDownloadCountAllNull() {
        List<DataDownloadStatistics> statisticsList = new ArrayList<>();
        when(dataDownloadRepository.findDownloadCount()).thenReturn(statisticsList);
        analyticsService.getDataDownloadCount(null, null, null);
        verify(dataDownloadRepository, times(1)).findDownloadCount();
    }

    @Test
    public void testGetDataDownloadCountEndDate() {
        List<DataDownloadStatistics> statisticsList = new ArrayList<>();
        when(dataDownloadRepository.findDownloadCountByDownloadDateBefore(any(ZonedDateTime.class))).thenReturn(statisticsList);
        analyticsService.getDataDownloadCount(null, null, ZonedDateTime.now());
        verify(dataDownloadRepository, times(1)).findDownloadCountByDownloadDateBefore(any(ZonedDateTime.class));
    }

    @Test
    public void testGetDataDownloadCountStartDate() {
        List<DataDownloadStatistics> statisticsList = new ArrayList<>();
        when(dataDownloadRepository.findDownloadCountByDownloadDateAfter(any(ZonedDateTime.class))).thenReturn(statisticsList);
        analyticsService.getDataDownloadCount(null, ZonedDateTime.now(), null);
        verify(dataDownloadRepository, times(1)).findDownloadCountByDownloadDateAfter(any(ZonedDateTime.class));
    }

    @Test
    public void testGetDataDownloadCountStartDateEndDate() {
        List<DataDownloadStatistics> statisticsList = new ArrayList<>();
        when(dataDownloadRepository.findDownloadCountByDownloadDateBetween(any(ZonedDateTime.class), any(ZonedDateTime.class))).thenReturn(statisticsList);
        analyticsService.getDataDownloadCount(null, ZonedDateTime.now(), ZonedDateTime.now());
        verify(dataDownloadRepository, times(1)).findDownloadCountByDownloadDateBetween(any(ZonedDateTime.class), any(ZonedDateTime.class));
    }

    @Test
    public void testGetDataDownloadCountDataId() {
        List<DataDownloadStatistics> statisticsList = new ArrayList<>();
        when(dataDownloadRepository.findDownloadCountByDataId(anyLong())).thenReturn(statisticsList);
        analyticsService.getDataDownloadCount(Long.parseLong(RandomStringUtils.randomNumeric(10)), null, null);
        verify(dataDownloadRepository, times(1)).findDownloadCountByDataId(anyLong());
    }

    @Test
    public void testGetDataDownloadCountDataIdEndDate() {
        List<DataDownloadStatistics> statisticsList = new ArrayList<>();
        when(dataDownloadRepository.findDownloadCountByDataIdAndDownloadDateBefore(anyLong(), any(ZonedDateTime.class))).thenReturn(statisticsList);
        analyticsService.getDataDownloadCount(Long.parseLong(RandomStringUtils.randomNumeric(10)), null, ZonedDateTime.now());
        verify(dataDownloadRepository, times(1)).findDownloadCountByDataIdAndDownloadDateBefore(anyLong(), any(ZonedDateTime.class));
    }

    @Test
    public void testGetDataDownloadCountDataIdStartDate() {
        List<DataDownloadStatistics> statisticsList = new ArrayList<>();
        when(dataDownloadRepository.findDownloadCountByDataIdAndDownloadDateAfter(anyLong(), any(ZonedDateTime.class))).thenReturn(statisticsList);
        analyticsService.getDataDownloadCount(Long.parseLong(RandomStringUtils.randomNumeric(10)), ZonedDateTime.now(), null);
        verify(dataDownloadRepository, times(1)).findDownloadCountByDataIdAndDownloadDateAfter(anyLong(), any(ZonedDateTime.class));
    }

    @Test
    public void testGetDataDownloadCountDataIdStartDateEndDate() {
        List<DataDownloadStatistics> statisticsList = new ArrayList<>();
        when(dataDownloadRepository.findDownloadCountByDataIdAndDownloadDateBetween(anyLong(), any(ZonedDateTime.class), any(ZonedDateTime.class))).thenReturn(statisticsList);
        analyticsService.getDataDownloadCount(Long.parseLong(RandomStringUtils.randomNumeric(10)), ZonedDateTime.now(), ZonedDateTime.now());
        verify(dataDownloadRepository, times(1)).findDownloadCountByDataIdAndDownloadDateBetween(anyLong(), any(ZonedDateTime.class), any(ZonedDateTime.class));
    }
}
