package sg.ncl.service.data.logic;

import org.apache.commons.lang3.RandomStringUtils;
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
import xyz.capybara.clamav.commands.scan.result.ScanResult;
import xyz.capybara.clamav.exceptions.*;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doThrow;
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
    @Mock
    private ScanResult scanResult;

    private AvScannerService avScannerService;

    @Before
    public void before() {
        assertThat(mockingDetails(avScannerProperties).isMock()).isTrue();
        assertThat(mockingDetails(clamavClient).isMock()).isTrue();
        assertThat(mockingDetails(clamavFactory).isMock()).isTrue();
        assertThat(mockingDetails(directoryProperties).isMock()).isTrue();
        assertThat(mockingDetails(scanResult).isMock()).isTrue();

        when(clamavFactory.createClamavClient(anyString(), anyInt())).thenReturn(clamavClient);

        // invoke the factory design pattern constructor when unit testing instead of the ordinary one
        avScannerService = new AvScannerImpl(avScannerProperties, directoryProperties, clamavFactory);
    }

    @Test
    public void testScanCleanFile() throws ClamavException {
        when(clamavClient.scan(any(Path.class), eq(false))).thenReturn(scanResult);
        when(scanResult.getStatus()).thenReturn(ScanResult.Status.OK);
        assertThat(avScannerService.scan("data", "dataset", "fileName")).isFalse();
    }

    @Test
    public void testScanMaliciousFile() throws ClamavException {
        when(clamavClient.scan(any(Path.class), eq(false))).thenReturn(scanResult);
        when(scanResult.getStatus()).thenReturn(ScanResult.Status.VIRUS_FOUND);
        assertThat(avScannerService.scan("data", "dataset", "fileName")).isTrue();
    }

    @Test
    public void testScanFileNullResult() throws ClamavException {
        when(clamavClient.scan(any(Path.class), eq(false))).thenReturn(scanResult);
        when(scanResult.getStatus()).thenReturn(null);
        assertThat(avScannerService.scan("data", "dataset", "fileName")).isFalse();
    }

    @Test
    public void testScanFileClamAvException() throws ClamavException {
        when(clamavClient.version()).thenReturn(RandomStringUtils.randomAlphanumeric(20));
        doThrow(new ClamavException(new Throwable("error_message"))).when(clamavClient).scan(any(Path.class), eq(false));
        when(scanResult.getStatus()).thenReturn(ScanResult.Status.VIRUS_FOUND);

        assertThat(avScannerService.scan("data", "dataset", "fileName")).isFalse();
    }

    @Test
    public void testScanCommunicationException() throws Exception {
        doThrow(new CommunicationException(new Throwable("error_message"))).when(clamavClient).scan(any(Path.class), eq(false));

        assertThat(avScannerService.scan("data", "dataset", "fileName")).isFalse();
    }

    @Test
    public void testScanInvalidResponseException() throws Exception {
        doThrow(new InvalidResponseException("error_message")).when(clamavClient).scan(any(Path.class), eq(false));

        assertThat(avScannerService.scan("data", "dataset", "fileName")).isFalse();
    }

    @Test
    public void testScanScanFailureException() throws Exception {
        doThrow(new ScanFailureException("error_message")).when(clamavClient).scan(any(Path.class), eq(false));

        assertThat(avScannerService.scan("data", "dataset", "fileName")).isFalse();
    }

    @Test
    public void testScanUnknownCommandException() throws Exception {
        doThrow(new UnknownCommandException("error_message")).when(clamavClient).scan(any(Path.class), eq(false));

        assertThat(avScannerService.scan("data", "dataset", "fileName")).isFalse();
    }

    @Test
    public void testScanUnsupportedCommandException() throws Exception {
        doThrow(new UnsupportedCommandException("error_message")).when(clamavClient).scan(any(Path.class), eq(false));

        assertThat(avScannerService.scan("data", "dataset", "fileName")).isFalse();
    }

    @Test
    public void testGetScheduleCronExpression() throws Exception {
        String one = RandomStringUtils.randomAlphanumeric(20);
        when(avScannerProperties.getCron()).thenReturn(one);

        assertThat(avScannerService.getScheduleCronExpression()).isEqualTo(one);
    }
}
