package sg.ncl.service.telemetry.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.telemetry.domain.NodeType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

/**
 * Created by dcsyeoty on 16-Dec-16.
 */
public class TelemetryServiceImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private AdapterDeterLab adapterDeterLab;

    private TelemetryServiceImpl telemetryServiceImpl;

    @Before
    public void setup() {
        assertThat(mockingDetails(adapterDeterLab).isMock()).isTrue();
        telemetryServiceImpl = new TelemetryServiceImpl(adapterDeterLab);
    }

    @Test
    public void getFreeNodes() throws Exception {
        String freeNodes = RandomStringUtils.randomNumeric(3);

        when(adapterDeterLab.getFreeNodes()).thenReturn(freeNodes);

        String result = telemetryServiceImpl.getNodes(NodeType.FREE);
        assertThat(result).isEqualTo(freeNodes);
    }

    @Test
    public void getTotalNodes() throws Exception {
        String totalNodes = RandomStringUtils.randomNumeric(3);

        when(adapterDeterLab.getTotalNodes()).thenReturn(totalNodes);

        String result = telemetryServiceImpl.getNodes(NodeType.TOTAL);
        assertThat(result).isEqualTo(totalNodes);
    }

    @Test
    public void getNodesStatus() throws Exception {
        String status = "{ \"type\" : [ { \"status\" : \"up\" } ] }";

        when(adapterDeterLab.getNodesStatus()).thenReturn(status);

        String result = telemetryServiceImpl.getNodesStatus();
        assertThat(result).isEqualTo(status);
    }

    @Test
    public void getLoggedInUsersCount() throws Exception {
        String one = RandomStringUtils.randomNumeric(3);

        when(adapterDeterLab.getLoggedInUsersCount()).thenReturn(one);

        String result = telemetryServiceImpl.getLoggedInUsersCount();
        assertThat(result).isEqualTo(one);
    }

    @Test
    public void getRunningExperimentsCount() throws Exception {
        String one = RandomStringUtils.randomNumeric(3);

        when(adapterDeterLab.getRunningExperimentsCount()).thenReturn(one);

        String result = telemetryServiceImpl.getRunningExperimentsCount();
        assertThat(result).isEqualTo(one);
    }
}
