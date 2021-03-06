package sg.ncl.service.experiment.domain;

import io.jsonwebtoken.Claims;
import sg.ncl.service.experiment.web.StatefulExperiment;

import java.util.List;

/**
 * @Authors: Desmond, Tran Ly Vu
 */
public interface ExperimentService {

    Experiment get(Long id);

    Experiment save(Experiment experiment);

    List<Experiment> getAll();

    List<Experiment> findByUser(String userId);

    List<Experiment> findByTeam(String teamId);

    Experiment deleteExperiment(Long id, String teamId, Claims claims);

    String getExperimentDetails(String teamId, Long expId);

    String getTopology(String teamId, Long expId);

    Experiment requestInternet(String teamId, Long expId, String reason, Claims claims);

    Experiment updateExperiment(Long expId, String teamId, Experiment experiment, Claims claims);

    List<StatefulExperiment> getStatefulExperimentsByTeam(String teamId, String userId);

    StatefulExperiment getStatefulExperiment(Long expId, String userId);
}
