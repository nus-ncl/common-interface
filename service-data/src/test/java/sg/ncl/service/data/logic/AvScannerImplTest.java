package sg.ncl.service.data.logic;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.common.AvScannerProperties;
import sg.ncl.service.data.domain.AvScannerService;
import sg.ncl.service.transmission.DirectoryProperties;
import xyz.capybara.clamav.ClamavClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

/**
 * Created by dcsyeoty on 05-Jun-17.
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class AvScannerImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private AvScannerProperties avScannerProperties;
    @Mock
    private DirectoryProperties directoryProperties;
    @Mock
    private AvScannerImpl.ClamavFactory clamavFactory;
    @Mock
    private ClamavClient clamavClient;

    private AvScannerService avScannerService;

    @Before
    public void before() {
        assertThat(mockingDetails(avScannerProperties).isMock()).isTrue();
        assertThat(mockingDetails(clamavClient).isMock()).isTrue();
        assertThat(mockingDetails(clamavFactory).isMock()).isTrue();
        assertThat(mockingDetails(directoryProperties).isMock()).isTrue();

        when(clamavFactory.createClamavClient(anyString(), anyInt())).thenReturn(clamavClient);

        // invoke the factory design pattern constructor when unit testing instead of the ordinary one
        avScannerService = new AvScannerImpl(avScannerProperties, directoryProperties, clamavFactory);
    }

    @Test
    public void testScan() {
        assertThat(avScannerService.scan("data", "dataset", "fileName")).isFalse();
    }
}
