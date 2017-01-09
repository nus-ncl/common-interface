package sg.ncl.service.telemetry.domain;

/**
 * Created by dcsyeoty on 16-Dec-16.
 */
@FunctionalInterface
public interface TelemetryService {

    String getNodes(NodeType nodeType);
}
