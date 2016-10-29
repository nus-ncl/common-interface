package sg.ncl.service.experiment.domain;

import io.jsonwebtoken.Claims;

import java.util.List;

/**
 * Created by Desmond.
 */
public interface ExperimentService {

    Experiment save(Experiment experiment);

    List<Experiment> getAll();

    List<Experiment> findByUser(String userId);

    List<Experiment> findByTeam(String teamId);

//  String createNsFile(String filename, String contents);

    Experiment deleteExperiment(Long id, String teamId, Claims claims);

    String getTopology(String teamId, Long expId);

}
