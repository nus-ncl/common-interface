package sg.ncl.service.analytics.logic;

import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.analytics.AnalyticsProperties;
import sg.ncl.service.analytics.data.jpa.DataDownloadEntity;
import sg.ncl.service.analytics.data.jpa.DataDownloadRepository;
import sg.ncl.service.analytics.data.jpa.DataDownloadStatistics;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.analytics.exceptions.StartDateAfterEndDateException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @Author dcszwang, Tran Ly Vu
 */
@RunWith(JMockit.class)
@TestPropertySource(properties = "flyway.enabled=false")
@EnableConfigurationProperties(AnalyticsProperties.class)
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
    @Mock
    private AnalyticsProperties analyticsProperties;

    @Before
    public void before() {
        assertThat(mockingDetails(dataDownloadRepository).isMock()).isTrue();
        assertThat(mockingDetails(adapterDeterLab).isMock()).isTrue();
        assertThat(mockingDetails(analyticsProperties).isMock()).isTrue();
        analyticsService = new AnalyticsServiceImpl(dataDownloadRepository, adapterDeterLab, analyticsProperties);
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

    @Test
    public void testGetEnergyStatisticsStartDateAfterEndDateException() {

        ZonedDateTime startDate = ZonedDateTime.now();
        ZonedDateTime endDate = startDate.minusDays(1);
        exception.expect(StartDateAfterEndDateException.class);
        List<Double> actual = analyticsService.getEnergyStatistics(startDate, endDate);
    }

    @Test
    public void testGetEnergyStatisticsEmptyEnergyList() throws Exception{

        Random rand = new Random();
        int randomNumberOfDays = rand.nextInt(10) + 1;;
        ZonedDateTime startDate =  ZonedDateTime.now();
        ZonedDateTime endDate = startDate.plusDays(randomNumberOfDays);

        new MockUp<AnalyticsServiceImpl>() {
            @mockit.Mock
            List<AnalyticsServiceImpl.Energy> getEnergyList (String start, String end) {
                List<AnalyticsServiceImpl.Energy> emptyList = new ArrayList<>();
                return emptyList;
            }
        };

        List<Double> expected = new ArrayList<>();
        for (int i = 0; i < randomNumberOfDays + 1; i++) {
            expected.add(0.00);
        }

        List<Double> actual =  analyticsService.getEnergyStatistics(startDate, endDate);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testGetEnergyStatisticsListof3() throws Exception{

        ZoneId zoneId = ZoneId.of("Asia/Singapore");
        ZonedDateTime startDate =  ZonedDateTime.of(2017, 03, 28, 0, 0, 0, 0, zoneId);
        ZonedDateTime endDate = ZonedDateTime.of(2017, 03, 29, 0, 0, 0, 0, zoneId);


        new MockUp<AnalyticsServiceImpl>() {
            @mockit.Mock
            List<AnalyticsServiceImpl.Energy> getEnergyList (String start, String end) {
                List<AnalyticsServiceImpl.Energy> energyList = new ArrayList<>();

                AnalyticsServiceImpl.Energy energy1 = new AnalyticsServiceImpl.Energy("nclenergy.201703280000.out");
                energy1.setUsage(200.0);
                energyList.add(energy1);

                AnalyticsServiceImpl.Energy energy2 = new AnalyticsServiceImpl.Energy("nclenergy.201703290000.out");
                energy2.setUsage(300.0);
                energyList.add(energy2);

                AnalyticsServiceImpl.Energy energy3 = new AnalyticsServiceImpl.Energy("nclenergy.201703300000.out");
                energy3.setUsage(450.0);
                energyList.add(energy3);

                return energyList;
            }
        };

        List<Double> expected = new ArrayList<>();
        expected.add(100.0);
        expected.add(150.0);

        List<Double> actual =  analyticsService.getEnergyStatistics(startDate, endDate);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.equals(actual));
    }
}
