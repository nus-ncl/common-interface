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
import sg.ncl.service.analytics.data.jpa.DataDownloadEntity;
import sg.ncl.service.analytics.data.jpa.DataDownloadRepository;
import sg.ncl.service.analytics.domain.AnalyticsService;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private AnalyticsService analyticsService;

    @Before
    public void before() {
        assertThat(mockingDetails(dataDownloadRepository).isMock()).isTrue();
        analyticsService = new AnalyticsServiceImpl(dataDownloadRepository);
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
}
