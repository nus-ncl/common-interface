package sg.ncl.service.experiment.domain;

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

    void createExperimentInDeter(Experiment experiment);

    String deleteExperiment(final Long id);

}