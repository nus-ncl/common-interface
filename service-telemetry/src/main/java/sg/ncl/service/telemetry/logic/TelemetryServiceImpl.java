package sg.ncl.service.telemetry.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.telemetry.domain.NodeType;
import sg.ncl.service.telemetry.domain.TelemetryService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Created by dcsyeoty on 26-Oct-16.
 */
@Service
@Slf4j
public class TelemetryServiceImpl implements TelemetryService {

    private final AdapterDeterLab adapterDeterLab;

    @Inject
    TelemetryServiceImpl(@NotNull final AdapterDeterLab adapterDeterLab) {
        this.adapterDeterLab = adapterDeterLab;
    }

    @Override
    public String getNodes(NodeType nodeType) {
        if (nodeType.equals(NodeType.FREE)) {
            return adapterDeterLab.getFreeNodes();
        } else {
            return adapterDeterLab.getTotalNodes();
        }
    }

    /**
     * Retrieves the number of logged in users at the current time
     * @return 0 if the number of logged in users is empty or error
     */
    @Override
    public String getLoggedInUsersCount() {
        return adapterDeterLab.getLoggedInUsersCount();
    }

    /**
     * Retrieves the number of running experiments at the current time
     * @return 0 if the number of running experiments is empty or error
     */
    @Override
    public String getRunningExperimentsCount() {
        return adapterDeterLab.getRunningExperimentsCount();
    }
}
