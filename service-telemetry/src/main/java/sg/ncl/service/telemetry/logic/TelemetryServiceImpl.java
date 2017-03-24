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
     * Retrieves the list of testbed nodes status, including node id, status, machine type.
     * Status inlclude "free", "in_use", "reload", "reserved"
     * "free" - particular node free to be allocated
     * "in_use" - particular node is currently used by a project
     * "reload" - particular node is reloading or maybe dead
     * "reserved" - particular node is pre-reserved by a project
     * @return a json dump of the nodes status in the format { type : [ { id : A, status : B, type : C }, ... {} ]  }
     */
    @Override
    public String getNodesStatus() {
        return adapterDeterLab.getNodesStatus();
    }

}
