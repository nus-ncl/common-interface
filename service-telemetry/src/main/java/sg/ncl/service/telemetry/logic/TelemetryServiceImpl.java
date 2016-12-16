package sg.ncl.service.telemetry.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
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

    public String getFreeNodes() {
        log.info("Telemetry getting free nodes at: {}", connectionProperties.getFreeNodes());
        return ;
    }
}
